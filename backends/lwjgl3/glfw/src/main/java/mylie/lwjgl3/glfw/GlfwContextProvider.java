package mylie.lwjgl3.glfw;

import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.component.ComponentManager;
import mylie.graphics.ContextProvider;
import mylie.graphics.Display;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallbackI;

@Slf4j
@Getter
public class GlfwContextProvider extends ContextProvider implements GLFWErrorCallbackI {
    private final List<Display> displays = new ArrayList<>();
    private Display display;

    public void onInitialize(ComponentManager componentManager) {
        log.debug("Glfw initialization");
        GLFW.glfwSetErrorCallback(this);
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }
        displays.addAll(GlfwUtil.getDisplays());
        display = GlfwUtil.getDisplay(GLFW.glfwGetPrimaryMonitor());
    }

    @Override
    public void invoke(int i, long l) {
        String description = memUTF8(l);
        log.error("Glfw error: {} - {}", i, description);
    }
}
