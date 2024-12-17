package mylie.examples.tests;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import mylie.application.Application;
import mylie.async.SchedulerSettings;
import mylie.component.AppComponent;
import mylie.core.Engine;
import mylie.core.EngineConfiguration;
import mylie.core.Platform;
import mylie.core.Timer;
import mylie.platform.desktop.Desktop;

@Slf4j
public class HelloEngine implements Application {

    public static void main(String[] args) {
        EngineConfiguration configuration = Platform.initialize(new Desktop());
        Engine.Options.Application.set(configuration, new HelloEngine());
        Engine.Options.Scheduler.set(configuration, SchedulerSettings.VirtualThreads);
        Engine.ShutdownReason shutdownReason = Engine.start(configuration);
    }

    @Override
    public void onInitialize(Consumer<? extends AppComponent> initializer) {
        log.trace("HelloEngine initialized.");
    }

    @Override
    public void onUpdate(Timer.Time time) {
        log.trace("HelloEngine updated.");
    }

    @Override
    public void onShutdown() {
        log.trace("HelloEngine shutdown.");
    }
}
