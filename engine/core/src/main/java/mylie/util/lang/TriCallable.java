package mylie.util.lang;

public interface TriCallable<U, V, T> {
    void apply(U u, V v, T t);
}
