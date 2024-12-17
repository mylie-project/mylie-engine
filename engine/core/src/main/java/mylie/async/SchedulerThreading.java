package mylie.async;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SchedulerThreading extends Scheduler {
    private final List<ManagedThread> managedThreadList = new CopyOnWriteArrayList<>();

    public SchedulerThreading(GlobalCache globalCache, String name) {
        super(globalCache, name);
    }

    @Override
    public void registerTarget(Target target, Consumer<Runnable> drain) {
        registerTarget(target, new TaskExecutor(drain, target));
    }

    @Override
    public ManagedThread createThread(Target target, BlockingQueue<Runnable> queue) {
        ManagedThreadThreading managedThread = new ManagedThreadThreading(target, queue, managedThreadList);
        managedThreadList.add(managedThread);
        managedThread.start();
        return managedThread;
    }

    @Override
    public void shutdown() {
        for (ManagedThread managedThread : managedThreadList) {
            if (managedThread instanceof ManagedThreadThreading managedThreadThreading) {
                managedThreadThreading.stop();
            }
        }
    }

    record TaskExecutor(Consumer<Runnable> drain, Target target) implements Scheduler.TaskExecutor {
        @Override
        public <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode) {
            Results.CompletableFutureResult<R> result =
                    Results.completableFuture(hash, version, new CompletableFuture<>(), task, target);
            executionMode.cache().set(result);
            drain.accept(result::execute);
            return result;
        }
    }
}
