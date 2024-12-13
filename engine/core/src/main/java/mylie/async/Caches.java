package mylie.async;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * A utility class providing predefined caching strategies for use in scheduling
 * and other asynchronous processing scenarios. The caching strategies are implemented
 * as instances of the abstract {@code Cache} class, with each instance providing
 * specific caching behavior.
 *
 * The available caching mechanisms are as follows:
 *
 * 1. {@code No} - Disables caching; retrieves no results and does not store any results.
 * 2. {@code OneFrame} - Caches results for a single frame only, interacting with a global cache.
 * 3. {@code Versioned} - Stores results and clears outdated entries based on versioning.
 * 4. {@code Unlimited} - Provides unlimited caching with no clearing mechanism.
 *
 * These caches are statically initialized and can be registered in schedulers
 * or other asynchronous execution contexts to provide efficient task execution
 * and result reuse where appropriate.
 *
 * The `Caches` class is final and cannot be extended or instantiated. It is designed
 * to work alongside the {@code Scheduler} and potentially other systems that utilize
 * the {@code Cache} abstractions.
 */
@Slf4j
public final class Caches {
    private Caches() {}

    /**
     * A static cache instance representing a "No Caching" strategy. This cache implementation
     * performs no operations for storing, retrieving, or clearing cached data.
     *
     * This instance provides the following behaviors:
     * - The `get` method always returns `null`, indicating no data is retrieved.
     * - The `set` method does nothing, indicating no data is stored.
     * - The `clear` method performs no action, leaving the cache unchanged.
     *
     * Designed to be used where caching is not desired or needed.
     */
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

    /**
     * OneFrame is a static, final instance of a custom caching mechanism used for
     * managing and storing calculation results. It extends the abstract class Cache
     * and provides implementation for data retrieval, storage, and clearing specific
     * to single frame or layer-based caching.
     *
     * Purpose:
     * - Acts as a localized cache layer that retains results for only a specific
     *   frame or a single layer of execution, while syncing with a global cache.
     * - Prevents repeated computation by storing cached results and allows clearing
     *   when older versions are no longer needed.
     *
     * Design:
     * - The cache is backed by a HashMap that stores computation results indexed
     *   by their hash codes.
     * - Serves as a middle layer between local processing and the global cache system,
     *   enabling optimized memory usage and retrieval speeds.
     *
     * Key Features:
     * - **Get Operation:** Retrieves cached results by hash code and version. If the
     *   result is not found in the current instance's cache, it fetches from the
     *   global cache.
     * - **Set Operation:** Stores new result entries by associating them with their
     *   respective hash codes and syncs them with the global cache.
     * - **Clear Operation:** Clears all cached results in this instance while ensuring
     *   removal from the global cache.
     */
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

    /**
     * A static final cache instance designed specifically for versioned data handling.
     * The `Versioned` cache implements behavior for retrieving and storing cached
     * results while taking into account a versioning mechanism. This ensures that
     * data stored in the cache is only retained if it matches or exceeds a specified version.
     *
     * Key functionalities of the `Versioned` cache:
     * - Retrieves cached data using a hash code and version, evicting stale data that
     *   does not match the required version.
     * - Stores results in the global cache using a hash-based identifier.
     * - Includes a no-op clear operation tied to a version parameter, allowing for
     *   future extensions of version-based cache invalidation logic.
     *
     * This cache operates in conjunction with a global caching layer, where it
     * delegates the storage, updating, and removal of cache results.
     */
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

    /**
     * A singleton instance of the Cache class representing an unlimited cache.
     * This cache has no restrictions on the number of items it can store.
     *
     * In this implementation:
     * - The `get` method retrieves results from the global cache using the hash code.
     * - The `set` method inserts or updates results in the global cache using their hash value.
     * - The `clear` method is overridden but does not perform any actions specific to the unlimited cache.
     */
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
