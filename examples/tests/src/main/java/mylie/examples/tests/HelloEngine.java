package mylie.examples.tests;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import mylie.application.BaseApplication;
import mylie.async.SchedulerSettings;
import mylie.component.AppComponent;
import mylie.core.*;
import mylie.lwjgl3.opengl.Lwjgl3OpenGlSettings;
import mylie.platform.desktop.Desktop;

@Slf4j
public class HelloEngine extends BaseApplication {

    public static void main(String[] args) {
        EngineConfiguration configuration = Platform.initialize(new Desktop());
        configuration.option(Engine.Options.Application,new HelloEngine());
        configuration.option(Engine.Options.GraphicsApi,new Lwjgl3OpenGlSettings());
        Engine.ShutdownReason shutdownReason = Engine.start(configuration);
    }

    @Override
    public void onInitialize(Consumer<? extends AppComponent> initializer) {
        log.trace("HelloEngine initialized.");
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
