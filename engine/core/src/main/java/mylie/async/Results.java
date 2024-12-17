package mylie.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public final class Results {
    private Results() {}

    public static <R> Result<R> fixed(int hash, long version, R result) {
        return new Fixed<>(hash, version, result);
    }

    public static <R> CompletableFutureResult<R> completableFuture(
            int hash, long version, CompletableFuture<R> future, Supplier<R> task, Target target) {
        return new CompletableFutureResult<>(hash, version, future, task, target);
    }

    @Getter
    public static class Fixed<T> extends Result<T> {
        @Setter(AccessLevel.PACKAGE)
        T result;

        public Fixed(int hashCode, long version, T result) {
            super(hashCode, version);
            this.result = result;
        }

        @Override
        public <R> Result<R> onCompletion(Function<T, Result<R>> onCompletion) {
            return onCompletion.apply(result);
        }
    }

    public static class CompletableFutureResult<T> extends Result<T> {
        private final CompletableFuture<T> future;
        private final Supplier<T> task;
        private final Target target;
        private final AtomicBoolean running = new AtomicBoolean(false);

        public CompletableFutureResult(
                int hashCode, long version, CompletableFuture<T> future, Supplier<T> task, Target target) {
            super(hashCode, version);
            this.future = future;
            this.task = task;
            this.target = target;
        }

        @Override
        public T result() {
            try {
                if (!future.isDone() && (target == Target.Background || Async.isCurrentThread(target))) {
                    if (running.compareAndSet(false, true)) {
                        future.complete(task.get());
                    }
                }
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <R> Result<R> onCompletion(Function<T, Result<R>> onCompletion) {
            return onCompletion.apply(result());
        }

        void execute() {
            if (running.compareAndSet(false, true)) {
                future.complete(task.get());
            }
        }
    }
}
