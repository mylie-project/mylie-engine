package mylie.async;

import static mylie.async.Async.async;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import mylie.util.Exceptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class SchedulerTest {

    static Stream<Scheduler> schedulerProvider() {

        return Stream.of(
                new SchedulerNoThreading(),
                new SchedulerExecutor(Executors.newVirtualThreadPerTaskExecutor()),
                new SchedulerExecutor(Executors.newFixedThreadPool(1)),
                new SchedulerExecutor(Executors.newScheduledThreadPool(16)),
                new SchedulerExecutor(ForkJoinPool.commonPool()));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithoutCache(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);
        Async.await(
                async(executionMode, 0, atomicIntegerIncrease, integer),
                async(executionMode, 0, atomicIntegerIncrease, integer),
                async(executionMode, 0, atomicIntegerIncrease, integer));
        assertEquals(3, integer.get());
        Async.await(async(executionMode, 0, atomicIntegerDecrease, integer));
        assertEquals(2, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithCache(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);
        Async.await(
                async(executionMode, 0, atomicIntegerIncrease, integer),
                async(executionMode, 0, atomicIntegerIncrease, integer),
                async(executionMode, 0, atomicIntegerIncrease, integer));
        assertEquals(1, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteMixedCachePolicies(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Never = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);
        ExecutionMode OneFrame = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);
        Async.await(
                async(Never, 0, atomicIntegerIncrease, integer),
                async(OneFrame, 0, atomicIntegerIncrease, integer),
                async(Never, 0, atomicIntegerIncrease, integer),
                async(OneFrame, 0, atomicIntegerIncrease, integer));
        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithDynamicCachingAndFrameId(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Versioned = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);
        Async.await(
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 2, atomicIntegerIncrease, integer));
        assertEquals(2, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithDynamicCachingAndFrameId2(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Versioned = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);
        Async.await(
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 2, atomicIntegerIncrease, integer),
                async(Versioned, 2, atomicIntegerIncrease, integer));
        assertEquals(2, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithDynamicCachingAndFrameId3(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Versioned = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);
        Async.await(
                async(Versioned, 2, atomicIntegerIncrease, integer),
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 2, atomicIntegerIncrease, integer));
        assertEquals(1, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTaskWithMultipleVersions(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Versioned = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);
        Async.await(
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 1, atomicIntegerIncrease, integer),
                async(Versioned, 3, atomicIntegerIncrease, integer));
        assertEquals(2, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldExecuteTasksWithMixedExecutionModes(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode Never = new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, Caches.No);
        ExecutionMode AsyncOneFrame = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);
        Async.await(
                async(Never, 0, atomicIntegerIncrease, integer),
                async(AsyncOneFrame, 0, atomicIntegerIncrease, integer),
                async(Never, 0, atomicIntegerIncrease, integer));
        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldRespectCacheEvictionPolicy(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode OneFrame = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);
        Async.await(
                async(OneFrame, 0, atomicIntegerIncrease, integer),
                async(OneFrame, 1, atomicIntegerIncrease, integer),
                async(OneFrame, 1, atomicIntegerIncrease, integer));
        assertEquals(1, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleConcurrentTaskExecution(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);
        Thread thread1 = new Thread(() -> Async.await(async(executionMode, 0, atomicIntegerIncrease, integer)));
        Thread thread2 = new Thread(() -> Async.await(async(executionMode, 0, atomicIntegerIncrease, integer)));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(2, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleTaskExecutionWithMultipleThreads(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        // Create multiple threads to execute tasks simultaneously
        Thread thread1 = new Thread(() -> Async.await(async(executionMode, 0, atomicIntegerIncrease, integer)));
        Thread thread2 = new Thread(() -> Async.await(async(executionMode, 0, atomicIntegerIncrease, integer)));
        Thread thread3 = new Thread(() -> Async.await(async(executionMode, 0, atomicIntegerIncrease, integer)));

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldTestTaskExecutionWithDifferentExecutionModes(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode Direct = new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, Caches.No);
        ExecutionMode AsyncModeCache = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);

        Async.await(
                async(Direct, 0, atomicIntegerIncrease, integer),
                async(AsyncModeCache, 1, atomicIntegerIncrease, integer),
                async(Direct, 0, atomicIntegerIncrease, integer));

        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldTestCacheBehaviorWithMultipleFrameVersions(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode VersionedCache = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.Versioned);

        Async.await(
                async(VersionedCache, 1, atomicIntegerIncrease, integer),
                async(VersionedCache, 1, atomicIntegerIncrease, integer),
                async(VersionedCache, 2, atomicIntegerIncrease, integer),
                async(VersionedCache, 3, atomicIntegerIncrease, integer));

        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldRespectCacheEvictionAndInvalidateAppropriately(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode OneFrameCache = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);

        Async.await(
                async(OneFrameCache, 0, atomicIntegerIncrease, integer),
                async(OneFrameCache, 1, atomicIntegerIncrease, integer),
                async(OneFrameCache, 2, atomicIntegerIncrease, integer));

        assertEquals(1, integer.get());
    }

    @SuppressWarnings("DataFlowIssue")
    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleNullExecutionMode(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        Assertions.assertThrows(
                NullPointerException.class, () -> Async.await(Async.async(null, 1, atomicIntegerIncrease, integer)));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleNullSupplier(Scheduler scheduler) {
        Async.scheduler(scheduler);
        Assertions.assertThrows(
                NullPointerException.class,
                () -> scheduler.executeTask(
                        new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No), 1, 1, null));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleNullCacheInExecutionMode(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, null);
        Assertions.assertThrows(
                NullPointerException.class,
                () -> Async.await(Async.async(executionMode, 1, atomicIntegerIncrease, integer)));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleExceptionInTaskExecution(Scheduler scheduler) {
        Async.scheduler(scheduler);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        Functions.F0<Boolean, AtomicInteger> faultyFunction = new Functions.F0<>("FaultyFunction") {
            @Override
            public Boolean run(AtomicInteger o) {
                throw new RuntimeException("Simulated exception");
            }
        };

        Assertions.assertThrows(
                RuntimeException.class,
                () -> Async.await(Async.async(executionMode, 1, faultyFunction, new AtomicInteger(0))));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleNullTargetInExecutionMode(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, null, Caches.No);
        Assertions.assertThrows(
                NullPointerException.class,
                () -> Async.await(Async.async(executionMode, 1, atomicIntegerIncrease, integer)));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldThrowExceptionDuringTaskExecution(Scheduler scheduler) {
        Async.scheduler(scheduler);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        Assertions.assertThrows(
                RuntimeException.class,
                () -> Async.await(Async.async(executionMode, 1, throwException, new AtomicInteger(0))));
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldThrowCorrectExceptionWhenTaskFails(Scheduler scheduler) {
        Async.scheduler(scheduler);
        ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> Async.await(Async.async(executionMode, 1, throwException, new AtomicInteger(0))));

        assertEquals("Simulated exception", Exceptions.getRootCause(exception).getMessage());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleNestedFunctions(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode directMode = new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, Caches.No);
        ExecutionMode asyncMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        Async.await(
                Async.async(asyncMode, 0, Nested3, integer),
                Async.async(directMode, 0, atomicIntegerIncrease, integer),
                Async.async(directMode, 0, atomicIntegerDecrease, integer));

        assertEquals(3, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldHandleDeeplyNestedOperations(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode asyncMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        Functions.F0<Boolean, AtomicInteger> deepNestedFunction = new Functions.F0<>("DeepNestedFunction") {
            @Override
            public Boolean run(AtomicInteger o) {
                Async.await(
                        async(asyncMode, 0, Nested3, o),
                        async(asyncMode, 0, atomicIntegerIncrease, o),
                        async(asyncMode, 0, atomicIntegerDecrease, o),
                        async(asyncMode, 0, Nested2, o));
                o.incrementAndGet();
                return true;
            }
        };

        Async.await(
                async(asyncMode, 0, deepNestedFunction, integer), async(asyncMode, 0, atomicIntegerIncrease, integer));

        assertEquals(7, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldProperlyHandleInterleavedNestedOperations(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode asyncMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);
        ExecutionMode directMode = new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, Caches.No);

        Async.await(
                async(asyncMode, 0, Nested3, integer),
                async(directMode, 0, atomicIntegerIncrease, integer),
                async(asyncMode, 0, Nested2, integer),
                async(directMode, 0, atomicIntegerDecrease, integer),
                async(asyncMode, 0, atomicIntegerIncrease, integer));

        assertEquals(6, integer.get());
    }

    @ParameterizedTest
    @MethodSource("schedulerProvider")
    void shouldTestAsyncChainingWithOnCompletion(Scheduler scheduler) {
        Async.scheduler(scheduler);
        AtomicInteger integer = new AtomicInteger(0);

        ExecutionMode firstMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);
        ExecutionMode secondMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No);

        //noinspection unused
        Result<Boolean> result = Async.async(firstMode, 0, atomicIntegerIncrease, integer)
                .onCompletion(v -> Async.async(secondMode, 0, atomicIntegerDecrease, integer)
                        .onCompletion(w -> Async.async(firstMode, 0, atomicIntegerIncrease, integer)
                                .onCompletion(x -> Async.async(secondMode, 0, Nested2, integer)
                                        .onCompletion(y -> Async.async(firstMode, 0, Nested3, integer)))));

        Async.await(result);

        assertEquals(6, integer.get());
    }

    private static final Functions.F0<Boolean, AtomicInteger> atomicIntegerIncrease =
            new Functions.F0<>("AtomicIntegerIncrease") {
                @Override
                public Boolean run(AtomicInteger o) {
                    o.incrementAndGet();
                    return true;
                }
            };

    private static final Functions.F0<Boolean, AtomicInteger> atomicIntegerDecrease =
            new Functions.F0<>("AtomicIntegerDecrease") {
                @Override
                public Boolean run(AtomicInteger o) {
                    o.decrementAndGet();
                    return true;
                }
            };

    private static final Functions.F0<Boolean, AtomicInteger> Nested2 = new Functions.F0<>("AtomicIntegerDecrease") {
        @Override
        public Boolean run(AtomicInteger o) {
            o.incrementAndGet();
            Async.await(async(
                    new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No),
                    0,
                    atomicIntegerIncrease,
                    o));
            return true;
        }
    };

    private static final Functions.F0<Boolean, AtomicInteger> Nested3 = new Functions.F0<>("AtomicIntegerDecrease") {
        @Override
        public Boolean run(AtomicInteger o) {
            Async.await(
                    async(new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.No), 0, Nested2, o));
            o.incrementAndGet();
            return true;
        }
    };

    private static final Functions.F0<Boolean, AtomicInteger> throwException = new Functions.F0<>("ThrowException") {
        @Override
        public Boolean run(AtomicInteger o) {
            throw new RuntimeException("Simulated exception");
        }
    };
}
