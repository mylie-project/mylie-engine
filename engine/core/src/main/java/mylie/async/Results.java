package mylie.async;

import lombok.Getter;

public final class Results {
    private Results() {}

    public static <R> Result<R> fixed(int hash, long version, R result) {
        return new Fixed<>(hash, version, result);
    }

    @Getter
    private static class Fixed<T> extends Result<T> {
        final T result;

        public Fixed(int hashCode, long version, T result) {
            super(hashCode, version);
            this.result = result;
        }
    }
}
