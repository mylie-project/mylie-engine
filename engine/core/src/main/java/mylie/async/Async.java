package mylie.async;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Async {
    public static final Void VOID = new Void();

    @Setter(AccessLevel.PACKAGE)
    private static Scheduler scheduler;

    private static final ThreadLocal<Target> ThreadLocalTarget = new ThreadLocal<>();

    public static <R, O> Result<R> async(
            ExecutionMode executionMode, long version, Functions.F0<R, O> function, O object) {
        int hashCode = hash(function, object);
        Result<R> result = executionMode.cache().get(hashCode, version);
        log.trace("Function<{}>({}) Hash={} Cache={}", function.name(), object, hashCode, result != null);
        if (result == null) {
            result = executeTask(executionMode, hashCode, version, () -> function.run(object));
        }
        return result;
    }

    public static <R, O, P0> Result<R> async(
            ExecutionMode executionMode, long version, Functions.F1<R, O, P0> function, O object, P0 param0) {
        int hashCode = hash(function, object);
        Result<R> result = executionMode.cache().get(hashCode, version);

        log.trace("Function<{}>({}) Hash={} Cache={}", function.name(), object, hashCode, result != null);
        if (result == null) {
            result = executeTask(executionMode, hashCode, version, () -> function.run(object, param0));
        }
        return result;
    }

    public static <R, O, P0, P1> Result<R> async(
            ExecutionMode executionMode,
            long version,
            Functions.F2<R, O, P0, P1> function,
            O object,
            P0 param0,
            P1 param1) {
        int hashCode = hash(function, object);
        Result<R> result = executionMode.cache().get(hashCode, version);
        log.trace("Function<{}>({}) Hash={} Cache={}", function.name(), object, hashCode, result != null);
        if (result == null) {
            result = executeTask(executionMode, hashCode, version, () -> function.run(object, param0, param1));
        }
        return result;
    }

    public static <T, O> Iterable<Result<T>> async(
            List<?> objects, Class<? extends O> type, java.util.function.Function<O, Result<T>> function) {
        List<Result<T>> results = new ArrayList<>();
        for (Object object : objects) {
            if (type.isInstance(object)) {
                results.add(function.apply(type.cast(object)));
            }
        }
        return results;
    }

    public static <T, O, P0> Iterable<Result<T>> async(
            List<?> objects, Class<? extends O> type, BiFunction<O, P0, Result<T>> function, P0 param) {
        List<Result<T>> results = new ArrayList<>();
        for (Object object : objects) {
            if (type.isInstance(object)) {
                results.add(function.apply(type.cast(object), param));
            }
        }
        return results;
    }

    private static int hash(Function function, Object... objects) {
        return Objects.hash(function, Arrays.hashCode(objects));
    }

    public static <R> Result<R> executeTask(ExecutionMode executionMode, int hash, long version, Supplier<R> supplier) {
        Result<R> result;
        if (direct(executionMode)) {
            result = Results.fixed(hash, version, supplier.get());
            executionMode.cache().set(result);
        } else {
            result = scheduler.executeTask(executionMode, hash, version, supplier);
        }
        return result;
    }

    static boolean direct(ExecutionMode executionMode) {
        ExecutionMode.Mode mode = executionMode.mode();
        if (mode == ExecutionMode.Mode.Async) {
            return false;
        }
        if (executionMode.target() == Target.Background) {
            return true;
        }
        if (isCurrentThread(executionMode.target())) {
            return true;
        }
        return false;
    }

    public static void registerThread(Target target) {
        ThreadLocalTarget.set(target);
        Thread.currentThread().setName(target.name());
    }

    public static boolean isCurrentThread(Target target) {
        return ThreadLocalTarget.get() == target;
    }

    public static class Void {
        private Void() {}
    }
}
