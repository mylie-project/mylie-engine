package mylie.util.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * A concrete implementation of {@link ConfigurationFactory} that uses a {@link Map}-based storage
 * mechanism for managing configurations and associated options for a given target type.
 * This factory provides mechanisms to create configurations and options using an in-memory
 * hash-based map to store the data.
 *
 * @param <S> The type of the target object associated with this configuration factory.
 */
public class MapBasedConfigurationFactory<S> extends ConfigurationFactory<S> {
    public MapBasedConfigurationFactory(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier) {
        super(storeSupplier);
    }

    /**
     * Provides a new instance of {@link Configuration} that uses a map-based storage mechanism
     * for managing configuration options associated with the target type {@code S}.
     *
     * @return A {@link Configuration} instance of type {@code S} backed by an in-memory map
     *         for storing and retrieving configuration options.
     */
    @Override
    public Configuration<S> configuration() {
        return new MapConfiguration<>();
    }

    /**
     * Creates a new option instance for the configuration storage. This option does not have
     * a default value associated with it. The returned option can be used to set or retrieve
     * values in the configuration for a specific target.
     *
     * @param <T> The type of the value associated with the option.
     * @return A newly created option of type {@link mylie.util.configuration.Option}
     *         with no default value set.
     */
    @Override
    public <T> mylie.util.configuration.Option<T, S> option() {
        return new Option<>(storeSupplier);
    }

    /**
     * Creates a new configuration option with a specified default value.
     * This option can be used to manage configuration values for a given target.
     *
     * @param <T> The type of the default value to be associated with this option.
     * @param defaultValue The default value of type {@code T} to be associated with the option.
     * @return A new instance of {@link mylie.util.configuration.Option} initialized
     *         with the specified default value and a store supplier.
     */
    @Override
    public <T> mylie.util.configuration.Option<T, S> option(T defaultValue) {
        return new Option<>(storeSupplier, defaultValue);
    }

    /**
     * A specialized implementation of {@link mylie.util.configuration.Option}
     * with support for an optional default value. This class associates a configuration option
     * with a specific type and allows the option to either have a default value or to be initialized without one.
     *
     * @param <T> The type of the value stored in this option.
     * @param <S> The type of the target associated with the configuration context.
     */
    private static final class Option<T, S> extends mylie.util.configuration.Option<T, S> {
        @Getter(AccessLevel.PACKAGE)
        private final T defaultValue;

        /**
         * Constructs an option without an associated default value. This option is linked
         * to a configuration store supplier, which defines the storage mechanism for retrieving
         * and storing the option's value within a specific target's configuration context.
         *
         * @param storeSupplier A function that takes a target of type {@code S} and returns
         *                      a corresponding {@link Configuration<S>}
         *                      to manage this option.
         */
        private Option(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier) {
            super(storeSupplier);
            defaultValue = null;
        }

        /**
         * Constructs a new {@code Option} instance with a default value associated with it.
         * This option is linked to a configuration store supplier, which defines the storage
         * mechanism for retrieving and storing the option's value within a specific target's
         * configuration context.
         *
         * @param storeSupplier A function that takes a target of type {@code S} and returns
         *                      a corresponding {@link Configuration<S>}
         *                      to manage this option.
         * @param defaultValue The default value of type {@code T} to be associated with the option.
         */
        public Option(Function<S, mylie.util.configuration.Configuration<S>> storeSupplier, T defaultValue) {
            super(storeSupplier);
            this.defaultValue = defaultValue;
        }
    }

    /**
     * A concrete implementation of the {@link Configuration} class
     * that uses an in-memory map as the storage mechanism to manage configuration options
     * associated with a specific target type.
     *
     * @param <S> The type of the target object associated with configuration options.
     */
    private static final class MapConfiguration<S> extends mylie.util.configuration.Configuration<S> {
        private final Map<mylie.util.configuration.Option<?, S>, Object> store = new HashMap<>();

        /**
         * Initializes an instance of a map-based configuration used to manage configuration options.
         * This private constructor is intended to restrict instantiation and ensure that instances are
         * created only through the associated factory or enclosing class.
         */
        private MapConfiguration() {}

        /**
         * Stores the specified value for a given option within the configuration.
         * The value is associated with the provided {@code option}.
         *
         * @param <T> The type of the value being set.
         * @param option The option representing the key under which the value should be stored.
         * @param value The value to be associated with the given option.
         */
        @Override
        <T> void set(mylie.util.configuration.Option<T, S> option, T value) {
            store.put(option, value);
        }

        /**
         * Retrieves the value associated with the specified option from the internal configuration map.
         * If no value has been explicitly set for the given option, the default value defined for the option
         * will be returned.
         *
         * @param <T> The type of the value associated with the provided option.
         * @param option The option for which the associated value is to be retrieved.
         * @return The value associated with the specified option, or the default value if no value has
         *         been explicitly set in the configuration.
         */
        @SuppressWarnings("unchecked")
        @Override
        <T> T get(mylie.util.configuration.Option<T, S> option) {
            T o = (T) store.get(option);
            if (o == null) {
                o = (((MapBasedConfigurationFactory.Option<T, S>) option).defaultValue());
            }
            return o;
        }
    }
}
