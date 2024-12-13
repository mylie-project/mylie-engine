package mylie.async;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class Caches {
    private Caches() {}

    public static final Cache No = new Cache("NoCaching") {
        @Override
        <R> Result<R> get(int hashCode, long version) {
            return null;
        }

        @Override
        <R> void set(Result<R> result) {}

        @Override
        void clear(long version) {}
    };

    public static final Cache OneFrame = new Cache("OneFrame") {
        private final Map<Integer, Result<?>> results = new HashMap<>();

        @SuppressWarnings("unchecked")
        @Override
        <R> Result<R> get(int hashCode, long version) {
            Result<R> result = (Result<R>) results.get(hashCode);
            if (result == null) {
                result = globalCache().get(hashCode);
            }
            return result;
        }

        @Override
        <R> void set(Result<R> result) {
            results.put(result.hash(), result);
            globalCache().set(result.hash(), result);
        }

        @Override
        void clear(long version) {
            for (Integer i : results.keySet()) {
                globalCache().remove(i);
            }
            results.clear();
        }
    };

    public static final Cache Versioned = new Cache("Versioned") {
        @Override
        <R> Result<R> get(int hashCode, long version) {
            Result<R> result = globalCache().get(hashCode);
            if (result != null && result.version() < version) {
                globalCache().remove(hashCode);
                result = null;
            }
            return result;
        }

        @Override
        <R> void set(Result<R> result) {
            globalCache().set(result.hash(), result);
        }

        @Override
        void clear(long version) {}
    };

    public static final Cache Unlimited = new Cache("Unlimited") {

        @Override
        <R> Result<R> get(int hashCode, long version) {
            return globalCache().get(hashCode);
        }

        @Override
        <R> void set(Result<R> result) {
            globalCache().set(result.hash(), result);
        }

        @Override
        void clear(long version) {}
    };
}
