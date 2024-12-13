package mylie.async;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public abstract class Result<T> {
    private final int hash;
    private final long version;

    public Result(int hash, long version) {
        this.hash = hash;
        this.version = version;
    }

    public abstract T result();
}
