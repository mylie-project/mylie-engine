package mylie.async;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchedulerNoThreading extends Scheduler implements Scheduler.TaskExecutor {
    public SchedulerNoThreading() {
        super(new GlobalCacheMap(new HashMap<>()), "NoThreading");
        registerTarget(Target.Background, this);
    }

    @Override
    public void registerTarget(Target target, Consumer<Runnable> drain) {
        registerTarget(target, this);
    }

    @Override
    public ManagedThread createThread(Target target, BlockingQueue<Runnable> queue) {
        return new ManagedThread() {
            @Override
            public void start() {}

            @Override
            public void stop() {}

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

    @Override
    public void shutdown() {}

    @SuppressWarnings("unchecked")
    @Override
    public <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode) {
        Results.Fixed<R> fixed = (Results.Fixed<R>) Results.fixed(hash, version, null);
        executionMode.cache().set(fixed);
        fixed.result(task.get());
        return fixed;
    }
}
