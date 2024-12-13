package mylie.util.configuration;

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
}
