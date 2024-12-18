package mylie.graphics;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.async.*;
import mylie.core.components.Scheduler;
import mylie.math.Vector2i;
import mylie.math.Vector2ic;
import mylie.util.configuration.Configurable;
import mylie.util.properties.PropertiesAA;
import mylie.util.properties.Property;
import mylie.util.versioned.AutoIncremented;

@Getter(AccessLevel.PACKAGE)
public abstract class GraphicsContext
        implements Configurable<GraphicsContext, GraphicsContext.Option<?>>,
                PropertiesAA<GraphicsContext, GraphicsContext.Properties<?>> {
    private static int contextCount = -1;

    @Getter(AccessLevel.PUBLIC)
    private final mylie.util.properties.Properties<GraphicsContext, GraphicsContext.Properties<?>> properties =
            new mylie.util.properties.Properties.Map<>(AutoIncremented::new);

    @Getter(AccessLevel.PUBLIC)
    private final GraphicsContextConfiguration configuration;

    @Getter(AccessLevel.PROTECTED)
    private final ExecutionMode executionMode;

    @Getter(AccessLevel.PROTECTED)
    private final GraphicsContext primaryContext;

    private final ManagedThread contextThread;
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private final Target target = new Target("GraphicsContext<" + ++contextCount + ">");

    public GraphicsContext(
            GraphicsContextConfiguration configuration, GraphicsContext primaryContext, Scheduler scheduler) {
        this.configuration = configuration;
        this.configuration.context(this);
        this.primaryContext = primaryContext;
        scheduler.registerTarget(target, tasks::add);
        this.contextThread = scheduler.createThread(target, tasks);
        executionMode = new ExecutionMode(ExecutionMode.Mode.Async, target, Caches.No);
    }

    protected abstract <T> void onOptionChanged(mylie.util.configuration.Option<GraphicsContext, T> option, T value);

    /**
     * Destroys the current graphics context, releasing any associated resources
     * and performing necessary cleanup operations. This abstract method should
     * be implemented by subclasses to define specific destruction logic for a
     * particular graphics context implementation.
     *
     * @return a Result<Boolean> indicating the success or failure of the destroy operation.
     */
    protected abstract Result<Async.Void> destroy();

    /**
     * Swaps the rendering buffers to display the current drawing frame.
     * This operation typically involves flipping the back buffer to become the visible front buffer
     * and vice versa. This method is intended to be implemented by subclasses to
     * provide specific logic for buffer swapping based on the rendering context being used.
     *
     * @return a Result<Boolean> indicating the success or failure of the buffer swap operation.
     */
    public abstract Result<Async.Void> swapBuffers();

    public static class Option<T> extends mylie.util.configuration.Option<GraphicsContext, T> {
        public Option(String name, T defaultValue) {
            super(name, defaultValue);
        }

        public static Option<Boolean> Resizable;
        public static Option<Boolean> VSync;
        public static Option<Boolean> Transparent;
        public static Option<Boolean> AlwaysOnTop;
        public static Option<Boolean> Srgb;
        public static Option<Boolean> Decorated;
        public static Option<Integer> Multisampling;
        public static Option<String> Title;
        public static Option<VideoMode> VideoMode;
        public static Option<Icons> Icons;
    }

    public static class Properties<T> extends Property<GraphicsContext, T> {
        public Properties(String name) {
            super(name);
        }

        public static final Properties<Vector2ic> Size = new Properties<>("Size");
        public static final Properties<Boolean> Focus = new Properties<>("Focus");
        public static final Properties<Boolean> Maximized = new Properties<>("Maximized");
        public static final Properties<Boolean> Close = new Properties<>("ShouldClose");
        public static final Properties<Vector2ic> Position = new Properties<>("Position");
        public static final Properties<Vector2ic> FrameBufferSize = new Properties<>("FrameBufferSize");
    }

    /**
     * The VideoMode interface represents different display modes that can be used
     * within a graphical application. It provides several implementations for
     * specifying how the content should be displayed on the screen.
     */
    public interface VideoMode {

        /**
         * The Fullscreen record represents a full-screen video mode within a display,
         * implementing the VideoMode interface. It encompasses a specific display and
         * its particular video mode settings, which dictate how the graphical content is
         * rendered when in full-screen mode.
         *
         * @param display The display on which the full-screen mode is to be set.
         * @param videoMode The specific video mode configuration to use for full-screen rendering.
         */
        record Fullscreen(Display display, Display.VideoMode videoMode) implements VideoMode {}

        /**
         * The Windowed record represents a windowed video mode within a graphical
         * application, offering flexibility in terms of window size and position on
         * the screen, while implementing the VideoMode interface.
         *
         * @param display The display on which the windowed mode is to be set.
         * @param size The dimensions of the window, specified by a Vector2ic object.
         * @param position The position of the window on the screen, specified by
         *                 a Vector2ic object. The static field Centered provides a
         *                 default positioning at the screen center.
         */
        record Windowed(Display display, Vector2ic size, Vector2ic position) implements VideoMode {
            public static final Vector2ic Centered = new Vector2i(0, 0);
        }

        /**
         * The WindowedFullscreen record represents a hybrid video mode within a graphical
         * application. It combines aspects of both windowed and fullscreen modes, allowing
         * content to be displayed in what is effectively fullscreen, but within windowed
         * constraints. This mode is useful for applications that require the immersion of
         * fullscreen while retaining some aspects of windowed operation, such as window
         * controls or task switching.
         *
         * @param display The display on which the windowed fullscreen mode is to be set.
         */
        record WindowedFullscreen(Display display) implements VideoMode {}
    }

    public record Icons(String... paths) {}

    @Override
    public String toString() {
        return target.name();
    }
}
