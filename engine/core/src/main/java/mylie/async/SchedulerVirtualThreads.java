package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class SchedulerVirtualThreads extends SchedulerThreading implements Scheduler.TaskExecutor {
    final ExecutorService executorService;

    public SchedulerVirtualThreads() {
        super(new GlobalCacheMapConcurrent());
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
        registerTarget(Target.Background, this);
    }

    @Override
    public <R> Result<R> executeTask(int hash, long version, Supplier<R> task) {
        CompletableFuture<R> future = CompletableFuture.supplyAsync(task, executorService);
        Result<R> result = Results.CompletableFuture(hash, version, future, task, Target.Background);
        // Results.CompletableFutureResult<R> result = Results.CompletableFuture(hash, version, new
        // CompletableFuture<>(), task, Target.Background);
        // executorService.submit(result::execute);
        return result;
    }
}
