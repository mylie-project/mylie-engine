package mylie.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Scheduler {
    private final String name;
    private final GlobalCache globalCache;
    private final List<Cache> caches = new CopyOnWriteArrayList<>();
    private final Map<Target, TaskExecutor> targets = new HashMap<>();

    public Scheduler(GlobalCache globalCache, String name) {
        this.globalCache = globalCache;
        registerDefaultCaches();
        Async.scheduler(this);
        this.name = name;
    }

    public void version(long version) {
        caches.forEach(c -> c.clear(version));
    }

    void registerDefaultCaches() {
        Stream.of(Caches.No, Caches.OneFrame, Caches.Versioned, Caches.Unlimited)
                .forEach(this::registerCache);
    }

    public void registerCache(Cache cache) {
        log.trace("Cache<{}> registered", cache.name());
        cache.globalCache(globalCache);
        caches.add(cache);
    }

    protected void registerTarget(Target target, TaskExecutor executor) {
        log.trace("Target<{}> registered", target.name());
        targets.put(target, executor);
    }

    public abstract void registerTarget(Target target, Consumer<Runnable> drain);

    <R> Result<R> executeTask(ExecutionMode executionMode, int hash, long version, Supplier<R> supplier) {
        if (supplier == null) {
            throw new NullPointerException("Supplier cannot be null");
        }
        TaskExecutor executor = targets.get(executionMode.target());
        if (executor == null) {
            log.error("No executor registered for target {}", executionMode.target());
            return null;
        }
        Result<R> result = executor.executeTask(hash, version, supplier, executionMode);
        if (result == null) {
            log.error("Task execution returned null");
            return null;
        }
        return result;
    }

    protected interface TaskExecutor {
        <R> Result<R> executeTask(int hash, long version, Supplier<R> task, ExecutionMode executionMode);
    }

    @Override
    public String toString() {
        return "Scheduler{" + "name='" + name + '\'' + '}';
    }
}
