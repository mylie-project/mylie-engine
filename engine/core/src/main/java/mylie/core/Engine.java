package mylie.core;

import static mylie.core.EngineConfiguration.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.application.Application;
import mylie.async.Async;
import mylie.async.SchedulerSettings;
import mylie.async.Target;
import mylie.core.components.Timers;
import mylie.util.BuildInfo;
import mylie.util.Exceptions;
import mylie.util.configuration.Option;
import mylie.util.properties.Property;

@Slf4j
public class Engine {
    public static final BuildInfo buildInfo = new BuildInfo();
    public static final Target Target = new Target("Engine");
    private static final ShutdownReason RestartReason = new ShutdownReason.User("Restart");

    public interface Options {
        Option<Boolean, EngineConfiguration> HandleRestarts = Factory.option(true);
        Option<SchedulerSettings, EngineConfiguration> Scheduler = Factory.option(SchedulerSettings.VirtualThreads);
        Option<mylie.core.Timer.Settings, EngineConfiguration> Timer = Factory.option(new Timers.NanoTimer.Settings());
        Option<Application, EngineConfiguration> Application = Factory.option(null);
    }

    public interface Properties {
        Property<Boolean, Core> MultiThreaded = Core.PropertiesFactory.property();
    }

    public static ShutdownReason start(EngineConfiguration configuration) {
        Async.registerThread(Target);
        buildInfo.logBuildInfo(log);
        Core core;
        boolean restart = false;
        ShutdownReason shutdownReason;
        do {
            core = new Core(configuration);
            shutdownReason = core.onStart();
            if (shutdownReason instanceof ShutdownReason.User) {
                log.info("Engine shutdown: {}", shutdownReason.reason());
                break;
            }
            if (shutdownReason == RestartReason && Options.HandleRestarts.get(configuration)) {
                restart = true;
                log.info("Engine restart");
            }
        } while (restart);
        return null;
    }

    @Getter
    @AllArgsConstructor
    public static class ShutdownReason {
        final String reason;

        public static class User extends ShutdownReason {
            public User(String reason) {
                super(reason);
            }
        }

        @Getter
        public static class Error extends ShutdownReason {
            private final Throwable throwable;

            public Error(String reason, Throwable throwable) {
                super(reason);
                this.throwable = Exceptions.getRootCause(throwable);
            }
        }
    }
}
