package mylie.async;

public abstract class GlobalCache {
	protected abstract <R> Result<R> get(int hash);

	protected abstract <R> void set(int hash, Result<R> result);

	protected abstract void remove(int hash);
}
