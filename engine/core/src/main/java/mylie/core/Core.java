package mylie.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.application.ApplicationComponent;
import mylie.async.Scheduler;
import mylie.async.SchedulerNoThreading;
import mylie.component.ComponentManager;
import mylie.util.properties.MapBasedPropertiesFactory;
import mylie.util.properties.Properties;
import mylie.util.properties.PropertiesFactory;
import mylie.util.versioned.AutoIncremented;

@Slf4j
public class Core {
    static final PropertiesFactory<Core> PropertiesFactory =
            new MapBasedPropertiesFactory<>(Core::properties, AutoIncremented::new);

    @Getter(AccessLevel.PUBLIC)
    private final Properties<Core> properties = PropertiesFactory.properties();

    @Getter(AccessLevel.PUBLIC)
    private final EngineConfiguration configuration;

    private final ComponentManager componentManager;
    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    private Scheduler scheduler;
    private Timer timer;
    private Engine.ShutdownReason shutdownReason;
    private Thread updateThread;

    public Core(EngineConfiguration configuration) {
        this.configuration = configuration;
        this.componentManager = new ComponentManager(this);
    }

    public Engine.ShutdownReason onStart() {
        shutdownReason=null;
        componentManager.addComponent(new EngineManager(this));
        initScheduler();
        initTimer();
        initComponents();
        updateThread = new Thread(this::updateLoop);
        updateThread.setName("Update Loop");
        if (Engine.Properties.MultiThreaded.get(this).value()) {
            updateThread.start();
            while (updateThread.isAlive()) {
                try {
                    Runnable runnable = tasks.poll(10, TimeUnit.MILLISECONDS);
                    if (runnable != null) {
                        runnable.run();
                    }
                } catch (InterruptedException e) {
                    log.error("Error while executing tasks: {}", e.getMessage(), e);
                }
            }
        } else {
            updateLoop();
        }

        return shutdownReason;
    }

    private void updateLoop() {
        long frameId = -1;
        Timer.Time time = null;
        while (shutdownReason == null) {
            frameId++;
            scheduler.version(frameId);
            time = timer.update(frameId);
            componentManager.update(time);
        }
        componentManager.shutdown(time);
        scheduler.shutdown();
    }

    void onShutdown(Engine.ShutdownReason reason) {
        shutdownReason = reason;
    }

    private void initComponents() {
        componentManager.addComponent(new ApplicationComponent());
    }

    private void initTimer() {
        timer = Engine.Options.Timer.get(configuration).build();
        componentManager.addComponent(timer);
    }

    private void initScheduler() {
        scheduler = Engine.Options.Scheduler.get(configuration).build();
        Engine.Properties.MultiThreaded.set(this, !(scheduler instanceof SchedulerNoThreading));
        scheduler.registerTarget(Engine.Target, tasks::add);
        componentManager.addComponent(new mylie.core.components.Scheduler(scheduler));
    }
}
