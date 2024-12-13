package mylie.util.configuration;

import java.util.function.Function;

public abstract class ConfigurationFactory<S> {
    final Function<S, Configuration<S>> storeSupplier;

    ConfigurationFactory(Function<S, Configuration<S>> storeSupplier) {
        this.storeSupplier = storeSupplier;
    }

    public abstract Configuration<S> configuration();

    public abstract <T> Configuration.Option<T, S> option();

    public abstract <T> Configuration.Option<T, S> option(T defaultValue);
}
