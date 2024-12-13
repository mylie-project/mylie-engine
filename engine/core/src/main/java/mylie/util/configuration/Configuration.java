package mylie.util.configuration;

import java.util.function.Function;

public abstract class Configuration<S> {
    Configuration() {}

    abstract <T> void set(Option<T, S> option, T value);

    abstract <T> T get(Option<T, S> option);

    public abstract static class Option<T, S> {
        private final Function<S, Configuration<S>> storeSupplier;

        Option(Function<S, Configuration<S>> storeSupplier) {
            this.storeSupplier = storeSupplier;
        }

        public T get(S target) {
            return storeSupplier.apply(target).get(this);
        }

        public void set(S target, T value) {
            storeSupplier.apply(target).set(this, value);
        }
    }
}
