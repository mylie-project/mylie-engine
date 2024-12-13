package mylie.util.configuration;

import java.util.function.Function;

/**
 * Represents an abstract configuration mechanism that allows storing and retrieving
 * options associated with a specific target type. This class is designed to be extended
 * for implementation of custom storage mechanisms.
 *
 * @param <S> The type of the target object associated with configuration options.
 */
public abstract class Configuration<S> {
    Configuration() {}

    /**
     * Sets the specified value for a given option within a configuration. The value
     * is stored in association with the provided option.
     *
     * @param <T> The type of the value being set.
     * @param option The option representing the key under which the value should be stored.
     * @param value The value to be associated with the given option.
     */
    abstract <T> void set(Option<T, S> option, T value);

    /**
     * Retrieves the value associated with the specified option from the configuration.
     *
     * @param <T> The type of the value expected to be retrieved.
     * @param option The option representing the key for which the associated value is to be fetched.
     * @return The value associated with the given option, or null if no value is set for the option.
     */
    abstract <T> T get(Option<T, S> option);

    /**
     * Represents an abstract framework for defining configurable options. This class allows
     * options to be associated with a configuration and provides methods to retrieve or set
     * their values for a specific target.
     *
     * @param <T> The type of value associated with this option.
     * @param <S> The type of the target associated with the configuration context.
     */
    public abstract static class Option<T, S> {
        private final Function<S, Configuration<S>> storeSupplier;

        /**
         * Constructs an option with a supplier to provide the configuration store for a specific target.
         *
         * @param storeSupplier A function that takes a target of type {@code S} and returns
         *                      a corresponding {@link Configuration<S>} to manage this option.
         */
        Option(Function<S, Configuration<S>> storeSupplier) {
            this.storeSupplier = storeSupplier;
        }

        /**
         * Retrieves the value associated with this option for the given target. The value is fetched
         * from the configuration store returned by the supplier function associated with this option.
         *
         * @param target The target object of type {@code S} for which the option's value is to be retrieved.
         * @return The value of type {@code T} associated with this option for the specified target,
         *         or {@code null} if no value has been set for the option.
         */
        public T get(S target) {
            return storeSupplier.apply(target).get(this);
        }

        /**
         * Sets the specified value for this option in the configuration store associated with the given target.
         *
         * @param target The target object of type {@code S} for which the option's value is to be set.
         * @param value The value of type {@code T} to be associated with this option for the specified target.
         */
        public void set(S target, T value) {
            storeSupplier.apply(target).set(this, value);
        }
    }
}
