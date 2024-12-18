package mylie.async;

import java.util.Map;

public class GlobalCacheMap extends GlobalCache {
	private final Map<Integer, Result<?>> cache;

	public GlobalCacheMap(Map<Integer, Result<?>> cache) {
		this.cache = cache;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Result<T> get(int hash) {
		return (Result<T>) cache.get(hash);
	}

	@Override
	public void remove(int hash) {
		cache.remove(hash);
	}

	@Override
	public <R> void set(int hash, Result<R> result) {
		cache.put(hash, result);
	}
}
