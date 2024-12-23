package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class SchedulerExecutor extends SchedulerThreading implements Scheduler.TaskExecutor {
	final ExecutorService executorService;

	public SchedulerExecutor(ExecutorService executorService, String name) {
		super(new GlobalCacheMapConcurrent(), name);
		this.executorService = executorService;
		registerTarget(Target.Background, this);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		executorService.shutdown();
	}

	@Override
	public <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode) {
		// CompletableFuture<R> future = CompletableFuture.supplyAsync(task,
		// executorService);
		// Result<R> result = Results.CompletableFuture(hash, version, future, task,
		// Target.Background);
		Results.CompletableFutureResult<R> result = Results.completableFuture(hash, version, new CompletableFuture<>(),
				task, Target.Background);
		executionMode.cache().set(result);
		executorService.submit(result::execute);
		return result;
	}
}
