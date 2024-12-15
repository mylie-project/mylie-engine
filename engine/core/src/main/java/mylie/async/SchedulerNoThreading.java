package mylie.async;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchedulerNoThreading extends Scheduler implements Scheduler.TaskExecutor {
    public SchedulerNoThreading() {
        super(new GlobalCacheMap(new HashMap<>()));
        registerTarget(Target.Background, this);
    }

    @Override
    public void registerTarget(Target target, Consumer<Runnable> drain) {
        registerTarget(target, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode) {
        Results.Fixed<R> fixed = (Results.Fixed<R>) Results.fixed(hash, version, null);
        executionMode.cache().set(fixed);
        fixed.result(task.get());
        return fixed;
    }
}
