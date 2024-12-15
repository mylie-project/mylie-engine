package mylie.async;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public abstract class Result<T> {
    private final int hash;
    private final long version;
    private Function<T, Result<?>> onCompletion;

    public Result(int hash, long version) {
        this.hash = hash;
        this.version = version;
    }

    public abstract T result();

    public abstract <R> Result<R> onCompletion(Function<T, Result<R>> onCompletion);
}
