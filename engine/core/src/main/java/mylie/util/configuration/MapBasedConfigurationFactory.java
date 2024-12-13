package mylie.util.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Getter;

public class MapBasedConfigurationFactory<S> extends ConfigurationFactory<S> {
    public MapBasedConfigurationFactory(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier) {
        super(storeSupplier);
    }

    @Override
    public Configuration<S> configuration() {
        return new MapConfiguration<>();
    }

    @Override
    public <T> mylie.util.configuration.Configuration.Option<T, S> option() {
        return new Option<>(storeSupplier);
    }

    @Override
    public <T> mylie.util.configuration.Configuration.Option<T, S> option(T defaultValue) {
        return new Option<>(storeSupplier, defaultValue);
    }

    private static final class Option<T, S> extends mylie.util.configuration.Configuration.Option<T, S> {
        @Getter(AccessLevel.PACKAGE)
        private final T defaultValue;

        private Option(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier) {
            super(storeSupplier);
            defaultValue = null;
        }

        public Option(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier, T defaultValue) {
            super(storeSupplier);
            this.defaultValue = defaultValue;
        }
    }

    private static final class MapConfiguration<S> extends mylie.util.configuration.Configuration<S> {
        private final Map<Option<?, S>, Object> store = new HashMap<>();

        private MapConfiguration() {}

        @Override
        <T> void set(Option<T, S> option, T value) {
            store.put(option, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        <T> T get(Option<T, S> option) {
            T o = (T) store.get(option);
            if (o == null) {
                o = (((MapBasedConfigurationFactory.Option<T, S>) option).defaultValue());
            }
            return o;
        }
    }
}
