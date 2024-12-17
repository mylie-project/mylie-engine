package mylie.async;

public class Wait {

    @SafeVarargs
    public static <T> void wait(Result<T>... results) {
        for (Result<?> result : results) {
            result.result();
        }
    }

    public static <T> void wait(Iterable<Result<T>> results) {
        results.forEach(Result::result);
    }
}
