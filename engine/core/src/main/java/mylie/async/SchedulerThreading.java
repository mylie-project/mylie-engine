package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchedulerThreading extends Scheduler {
    public SchedulerThreading(GlobalCache globalCache) {
        super(globalCache);
    }

    @Override
    public void registerTarget(Target target, Consumer<Runnable> drain) {
        registerTarget(target, new TaskExecutor(drain, target));
    }

    record TaskExecutor(Consumer<Runnable> drain, Target target) implements Scheduler.TaskExecutor {
        @Override
        public <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode) {
            Results.CompletableFutureResult<R> result =
                    Results.CompletableFuture(hash, version, new CompletableFuture<>(), task, target);
            drain.accept(result::execute);
            return null;
        }
    }
}
