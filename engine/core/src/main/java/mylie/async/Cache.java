package mylie.async;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PACKAGE)
public abstract class Cache {
    private final String name;

    @Setter(AccessLevel.PACKAGE)
    private GlobalCache globalCache;

    protected Cache(String name) {
        this.name = name;
    }

    abstract <R> Result<R> get(int hashCode, long version);

    abstract <R> void set(Result<R> result);

    abstract void clear(long version);
}
