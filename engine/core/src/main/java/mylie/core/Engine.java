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
import mylie.graphics.ApiSettings;
import mylie.util.BuildInfo;
import mylie.util.Exceptions;

@Slf4j
public class Engine {
	public static final BuildInfo buildInfo = new BuildInfo();
	public static final Target Target = new Target("Engine");
	private static final ShutdownReason RestartReason = new ShutdownReason.User("Restart");

	public interface Options {
		EngineOption<Boolean> HandleRestarts = new EngineOption<>("HandleRestarts", true);
		EngineOption<SchedulerSettings> Scheduler = new EngineOption<>("Scheduler", SchedulerSettings.VirtualThreads);
		EngineOption<mylie.core.Timer.Settings> Timer = new EngineOption<>("Timer", new Timers.NanoTimer.Settings());
		EngineOption<Application> Application = new EngineOption<>("Application", null);
		EngineOption<ApiSettings> GraphicsApi = new EngineOption<>("GraphicsApi", null);
	}

	public static class Property<T> extends mylie.util.properties.Property<Engine, T> {
		public Property(String name) {
			super(name);
		}

		public static final Property<Boolean> MultiThreaded = new Property<>("MultiThreaded");
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
			if (shutdownReason == RestartReason && configuration.option(Options.HandleRestarts)) {
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
