package mylie.lwjgl3.glfw;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryUtil.memAllocInt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.component.ComponentManager;
import mylie.graphics.ContextProvider;
import mylie.graphics.Display;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.input.InputManager;
import mylie.math.Vector2i;
import mylie.math.Vector2ic;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

@Slf4j
@Getter
public abstract class GlfwContextProvider extends ContextProvider implements GLFWErrorCallbackI {
    private final List<Display> displays = new ArrayList<>();
    private final GlfwInputProvider inputProvider=new GlfwInputProvider();
    private Display display;

    public void onInitialize(ComponentManager componentManager) {
        log.debug("Glfw initialization");
        GLFW.glfwSetErrorCallback(this);
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }
        GraphicsContext.Option.Resizable = new GlfwContext.GlfwOption<>("Resizable", GLFW.GLFW_RESIZABLE, null, false);
        GraphicsContext.Option.Transparent =
                new GlfwContext.GlfwOption<>("TransparentFrameBuffer", GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, null, false);
        GraphicsContext.Option.AlwaysOnTop =
                new GlfwContext.GlfwOption<>("AlwaysOnTop", GLFW.GLFW_FLOATING, null, false);
        GraphicsContext.Option.Title = new GlfwContext.GlfwOption<>("Title", -1, GLFW::glfwSetWindowTitle, "Mylie");
        GraphicsContext.Option.VSync = new GlfwContext.GlfwOption<>("Vsync", -1, this::swapIntervalWrapper, true);
        GraphicsContext.Option.Decorated = new GlfwContext.GlfwOption<>("Decorated", GLFW.GLFW_DECORATED, null, true);
        GraphicsContext.Option.Multisampling = new GlfwContext.GlfwOption<>("NumSamples", GLFW.GLFW_SAMPLES, null, 0);
        GraphicsContext.Option.Srgb = new GlfwContext.GlfwOption<>("Srgb", GLFW.GLFW_SRGB_CAPABLE, null, false);
        GraphicsContext.Option.VideoMode =
                new GlfwContext.GlfwOption<>("VideoMode", -1, this::setVideoModeWrapper, null);
        GraphicsContext.Option.Icons = new GlfwContext.GlfwOption<>("Icons", -1, this::setIconsWrapper, null);

        displays.addAll(GlfwUtil.getDisplays());
        display = GlfwUtil.getDisplay(GLFW.glfwGetPrimaryMonitor());
        componentManager.getComponent(InputManager.class).addInputProvider(inputProvider);
    }

    protected void setupContext(GlfwContext context) {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GraphicsContextConfiguration configuration = context.configuration();
        for (GraphicsContext.Option<?> parameter : configuration.getOptions()) {
            if (parameter instanceof GlfwContext.GlfwOption<?> glfwParameter) {
                if (glfwParameter.windowHint() != -1) {
                    Object o = configuration.option(parameter);
                    int value = 0;
                    if (o instanceof Boolean) {
                        value = ((Boolean) o) ? 1 : 0;
                    } else if (o instanceof Integer) {
                        value = (Integer) o;
                    }
                    GLFW.glfwWindowHint(glfwParameter.windowHint(), value);
                }
            }
        }
        GraphicsContext.VideoMode videoMode = configuration.option(GraphicsContext.Option.VideoMode);
        if (videoMode instanceof GraphicsContext.VideoMode.WindowedFullscreen) {
            GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        }
    }

    protected boolean createWindow(GlfwContext contexts) {
        log.trace("Creating window");
        GraphicsContextConfiguration configuration = contexts.configuration();
        GraphicsContext.VideoMode videoMode = configuration.option(GraphicsContext.Option.VideoMode);
        Vector2i position = new Vector2i(0, 0);
        Vector2ic size = new Vector2i(16, 16);
        long display = NULL;
        long parent = contexts.primaryContext() == null
                ? NULL
                : contexts.primaryContext().handle();
        String title = configuration.option(GraphicsContext.Option.Title);
        boolean fullscreen = false;
        if (videoMode instanceof GraphicsContext.VideoMode.Windowed windowed) {
            size = windowed.size();
        } else if (videoMode instanceof GraphicsContext.VideoMode.WindowedFullscreen windowedFullscreen) {
            if (windowedFullscreen.display() != null) {
                size = windowedFullscreen.display().videoMode().resolution();
            } else {
                size = display().videoMode().resolution();
            }
        } else if (videoMode instanceof GraphicsContext.VideoMode.Fullscreen fullscreenMode) {
            fullscreen = true;
            if (fullscreenMode.display() != null) {
                display = ((GlfwDisplay) fullscreenMode.display()).handle();
                if (fullscreenMode.videoMode() != null) {
                    size = fullscreenMode.videoMode().resolution();
                } else {
                    size = fullscreenMode.display().videoMode().resolution();
                }
            } else {
                display = ((GlfwDisplay) display()).handle();
                size = display().videoMode().resolution();
            }
        }

        if (parent != NULL) {
            contexts.primaryContext().release().result();
        }
        long window = GLFW.glfwCreateWindow(size.x(), size.y(), title, fullscreen ? display : NULL, parent);
        if (parent != NULL) {
            contexts.primaryContext().makeCurrent().result();
        }
        if (videoMode instanceof GraphicsContext.VideoMode.Windowed windowed) {
            if (windowed.position() == GraphicsContext.VideoMode.Windowed.Centered || windowed.position() == null) {
                GlfwDisplay tmpDisplay = (GlfwDisplay) (windowed.display() != null ? windowed.display() : display());
                Display.VideoMode tmpVideoMode = tmpDisplay.videoMode();
                position = new Vector2i(
                        (tmpVideoMode.resolution().x() - size.x()) / 2,
                        (tmpVideoMode.resolution().y() - size.y()) / 2);
            } else {
                position.set(windowed.position());
            }
            GLFW.glfwSetWindowPos(window, position.x(), position.y());
        }
        contexts.handle(window);
        setIconsWrapper(window, configuration.option(GraphicsContext.Option.Icons));
        contexts.properties().property(GraphicsContext.Properties.Position, position);
        contexts.properties().property(GraphicsContext.Properties.Size, size);
        contexts.properties().property(GraphicsContext.Properties.FrameBufferSize, size);
        contexts.callbacks(new GlfwCallbacks(contexts,inputProvider));
        GLFW.glfwShowWindow(window);
        return true;
    }

    @Override
    public void invoke(int i, long l) {
        String description = memUTF8(l);
        log.error("Glfw error: {} - {}", i, description);
    }

    private void setVideoModeWrapper(long window, GraphicsContext.VideoMode videoMode) {
        long display = NULL;
        Vector3ic size = new Vector3i();
        Vector2ic position = new Vector2i();
        if ((videoMode instanceof GraphicsContext.VideoMode.Fullscreen fullscreen)) {
            display = fullscreen.display() != null
                    ? ((GlfwDisplay) fullscreen.display()).handle()
                    : ((GlfwDisplay) display()).handle();
            size = fullscreen.display() != null && fullscreen.videoMode() != null
                    ? new Vector3i(
                            fullscreen.videoMode().resolution(),
                            fullscreen.videoMode().refreshRate())
                    : new Vector3i(
                            display().videoMode().resolution(),
                            display().videoMode().refreshRate());
        } else if (videoMode instanceof GraphicsContext.VideoMode.Windowed windowed) {
            size = new Vector3i(windowed.size(), 0);
            if (windowed.position() == GraphicsContext.VideoMode.Windowed.Centered || windowed.position() == null) {
                GlfwDisplay tmpDisplay = (GlfwDisplay) (windowed.display() != null ? windowed.display() : display());
                Display.VideoMode tmpVideoMode = tmpDisplay.videoMode();
                position = new Vector2i(
                        (tmpVideoMode.resolution().x() - size.x()) / 2,
                        (tmpVideoMode.resolution().y() - size.y()) / 2);
            } else {
                position = windowed.position();
            }
        } else if (videoMode instanceof GraphicsContext.VideoMode.WindowedFullscreen windowedFullscreen) {
            GlfwDisplay tmpDisplay =
                    (GlfwDisplay) (windowedFullscreen.display() != null ? windowedFullscreen.display() : display());
            size = new Vector3i(
                    tmpDisplay.videoMode().resolution(), tmpDisplay.videoMode().refreshRate());
            position = new Vector2i(0, 0);
            GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        }
        GLFW.glfwSetWindowMonitor(window, display, position.x(), position.y(), size.x(), size.y(), size.z());
    }

    public void setIconsWrapper(Long handle, GraphicsContext.Icons icons) {
        if (icons == null) {
            return;
        }
        IntBuffer w = memAllocInt(1);
        IntBuffer h = memAllocInt(1);
        IntBuffer comp = memAllocInt(1);
        try (GLFWImage.Buffer iconsBuffer = GLFWImage.malloc(icons.paths().length)) {
            ByteBuffer[] buffers = new ByteBuffer[icons.paths().length];
            ByteBuffer[] imageBuffers = new ByteBuffer[icons.paths().length];
            for (int i = 0; i < icons.paths().length; i++) {
                try {
                    buffers[i] = IOUtil.ioResourceToByteBuffer(icons.paths()[i], 10000);
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                }
                imageBuffers[i] = STBImage.stbi_load_from_memory(buffers[i], w, h, comp, 4);
                assert imageBuffers[i] != null;
                iconsBuffer.position(i).width(w.get(0)).height(h.get(0)).pixels(imageBuffers[i]);
            }
            iconsBuffer.position(0);
            GLFW.glfwSetWindowIcon(handle, iconsBuffer);
            for (int i = 0; i < icons.paths().length; i++) {
                STBImage.stbi_image_free(imageBuffers[i]);
            }
        }
    }

    private void swapIntervalWrapper(long window, boolean vsync) {
        GLFW.glfwSwapInterval(vsync ? 1 : 0);
    }
}
