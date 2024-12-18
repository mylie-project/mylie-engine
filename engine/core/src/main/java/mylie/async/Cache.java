package mylie.async;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * An abstract base class for implementing caching mechanisms with the ability
 * to retrieve, store, and clear cached results. Subclasses should provide
 * specific implementations for different caching strategies.
 *
 * The caching mechanism uses a name to identify the cache instance and
 * optionally interacts with a global cache for storage and retrieval of cached
 * results.
 */
@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
	private final String name;

	@Setter(AccessLevel.PACKAGE)
	private GlobalCache globalCache;

	/**
	 * Constructs a new cache instance with the specified name. This constructor
	 * initializes the cache with a given name, which serves as an identifier for
	 * the cache instance.
	 *
	 * @param name
	 *            the name of the cache instance
	 */
	protected Cache(String name) {
		this.name = name;
	}

	/**
	 * Retrieves a cached result based on the specified hash code and version. This
	 * method looks up a previously stored result identified by the combination of
	 * provided hash code and version. Subclasses must implement the retrieval
	 * logic, which may or may not involve interacting with a global cache
	 * mechanism.
	 *
	 * @param hashCode
	 *            an integer representing the hash code of the item to retrieve
	 * @param version
	 *            a long value specifying the version of the item to retrieve
	 * @return the cached result wrapped in a {@code Result<R>} instance or
	 *         {@code null} if no matching result is found
	 */
	abstract <R> Result<R> get(int hashCode, long version);

	/**
	 * Stores the specified result in the cache. This method is responsible for
	 * persisting a given result into the cache. Subclasses must implement this
	 * method to define how the result should be stored and associated with its hash
	 * and version metadata. The cache might interact with global or specific
	 * storage mechanisms, depending on the implementation.
	 *
	 * @param result
	 *            the result to be stored in the cache, encapsulated in a
	 *            {@code Result<R>} instance containing both the result data and its
	 *            associated metadata (e.g., hash code and version).
	 */
	abstract <R> void set(Result<R> result);

	/**
	 * Clears cached entries for the specified version. This method is intended to
	 * remove all cached data associated with the provided version. Subclasses
	 * should implement the logic for clearing the relevant entries from the cache,
	 * which might include interaction with a global cache or internal data
	 * structures.
	 *
	 * @param version
	 *            a long value specifying the version of cached entries to clear
	 */
	abstract void clear(long version);
}
