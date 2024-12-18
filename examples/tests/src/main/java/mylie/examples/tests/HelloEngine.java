package mylie.examples.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.application.BaseApplication;
import mylie.async.Async;
import mylie.async.Caches;
import mylie.async.ExecutionMode;
import mylie.async.Functions;
import mylie.core.*;
import mylie.examples.utils.IconFactory;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.graphics.GraphicsManager;
import mylie.lwjgl3.opengl.Lwjgl3OpenGlSettings;
import mylie.math.Vector2i;
import mylie.math.Vector2ic;
import mylie.platform.desktop.Desktop;
import mylie.util.versioned.Versioned;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class HelloEngine extends BaseApplication {
    GraphicsContext.VideoMode windowed = new GraphicsContext.VideoMode.Windowed(
            null, new Vector2i(800, 600), GraphicsContext.VideoMode.Windowed.Centered);
    GraphicsContext.VideoMode fullscreen = new GraphicsContext.VideoMode.Fullscreen(null, null);
    GraphicsContext context;

    public static void main(String[] args) {
        EngineConfiguration configuration = Platform.initialize(new Desktop());
        configuration.option(Engine.Options.Application, new HelloEngine());
        configuration.option(Engine.Options.GraphicsApi, new Lwjgl3OpenGlSettings());
        Engine.ShutdownReason shutdownReason = Engine.start(configuration);
    }

    @Override
    public void onInitialize() {
        log.trace("HelloEngine initialized.");
        GraphicsContextConfiguration gcc = new GraphicsContextConfiguration();
        gcc.option(GraphicsContext.Option.AlwaysOnTop, true);
        gcc.option(GraphicsContext.Option.Title, "Hello Engine");
        gcc.option(GraphicsContext.Option.VideoMode, windowed);
        gcc.option(GraphicsContext.Option.VSync, false);
        gcc.option(GraphicsContext.Option.Icons, IconFactory.getDefaultIcons());
        context = component(GraphicsManager.class).createContext(gcc, true);
    }

    @Override
    public void onUpdate(Timer.Time time) {
        log.trace("HelloEngine updated.");
        if (time.version() == 10) {
            component(EngineManager.class).shutdown(new Engine.ShutdownReason.User("All done"));
        }
    }

    @Override
    public void onShutdown() {
        log.trace("HelloEngine shutdown.");
    }
}
