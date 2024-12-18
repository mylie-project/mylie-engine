package mylie.core.components;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import mylie.async.ManagedThread;
import mylie.async.Target;
import mylie.component.CoreComponent;

public class Scheduler implements CoreComponent {
	private final mylie.async.Scheduler scheduler;

	public Scheduler(mylie.async.Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void registerTarget(Target target, Consumer<Runnable> drain) {
		scheduler.registerTarget(target, drain);
	}

	public ManagedThread createThread(Target target, BlockingQueue<Runnable> queue) {
		return scheduler.createThread(target, queue);
	}
}
