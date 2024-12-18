package mylie.async;

import java.util.concurrent.Executors;

public interface SchedulerSettings {
	Scheduler build();

	SchedulerSettings SingleThreaded = SchedulerNoThreading::new;
	SchedulerSettings VirtualThreads = () -> new SchedulerExecutor(Executors.newVirtualThreadPerTaskExecutor(),
			"VirtualThreadScheduler");
}
