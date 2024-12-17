package mylie.application;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.component.AppComponent;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Engine;
import mylie.core.Timer;
import mylie.core.components.Scheduler;

@Slf4j
public class ApplicationComponent extends BaseCoreComponent implements Lifecycle.InitDestroy, Lifecycle.Update {
    private Application application;
    private final ExecutionMode appExecutionMode =
            new ExecutionMode(ExecutionMode.Mode.Async, Application.Target, Caches.OneFrame);
    private final BlockingQueue<Runnable> applicationQueue = new LinkedBlockingQueue<>();
    private ManagedThread applicationThread;
    private boolean initialized;

    @Override
    public void onInit() {
        application = engineOption(Engine.Options.Application);
        addComponent(application);
        component(Scheduler.class).registerTarget(Application.Target, applicationQueue::add);
        applicationThread = component(Scheduler.class).createThread(Application.Target, applicationQueue);
    }

    @Override
    public void onDestroy() {
        if (initialized) {
            initialized = false;
            Async.async(appExecutionMode, Integer.MAX_VALUE, ShutdownApplication, application);
        }
    }

    @Override
    public void onUpdate(Timer.Time time) {
        if (!initialized) {
            initialized = true;
            Async.async(appExecutionMode, time.version(), InitApplication, application, this::addComponent);
        }
        Wait.wait(Async.async(appExecutionMode, time.version(), UpdateApplication, application, time));
    }

    @Override
    public String toString() {
        return "ApplicationComponent";
    }

    private static final Functions.F1<Async.Void, Application, Consumer<? extends AppComponent>> InitApplication =
            new Functions.F1<>("InitApplication") {
                @Override
                protected Async.Void run(Application application, Consumer<? extends AppComponent> initializer) {
                    application.onInitialize(initializer);
                    return Async.VOID;
                }
            };

    private static final Functions.F1<Async.Void, Application, Timer.Time> UpdateApplication =
            new Functions.F1<>("UpdateApplication") {
                @Override
                protected Async.Void run(Application application, Timer.Time time) {
                    application.onUpdate(time);
                    return Async.VOID;
                }
            };

    private static final Functions.F0<Async.Void, Application> ShutdownApplication =
            new Functions.F0<>("ShutdownApplication") {
                @Override
                protected Async.Void run(Application application) {
                    application.onShutdown();
                    return Async.VOID;
                }
            };
}
