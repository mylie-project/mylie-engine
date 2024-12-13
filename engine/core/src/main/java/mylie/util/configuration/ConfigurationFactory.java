package mylie.util.configuration;

import java.util.function.Function;

/**
 * An abstract factory to create and manage configurations for a given target type.
 * Provides mechanisms for creating configurations and defining options within those configurations
 * that can be associated with a target object.
 *
 * @param <S> The type of the target object associated with the configuration.
 */
public abstract class ConfigurationFactory<S> {
    final Function<S, Configuration<S>> storeSupplier;

    /**
     * Constructs a ConfigurationFactory instance with the provided supplier function
     * for obtaining a configuration store specific to a target.
     *
     * @param storeSupplier A function that takes a target object of type {@code S}
     *                      and returns a {@link Configuration}{@code <S>} instance
     *                      to manage configurations associated with that target.
     */
    ConfigurationFactory(Function<S, Configuration<S>> storeSupplier) {
        this.storeSupplier = storeSupplier;
    }

    /**
     * Provides the configuration instance associated with the current context.
     *
     * @return A {@link Configuration} instance of type {@code S} for managing configuration options
     *         associated with the target object.
     */
    public abstract Configuration<S> configuration();

    /**
     * Creates and returns a new option instance for the configuration associated
     * with the target type {@code S}. The option does not have a default value set.
     *
     * @param <T> The type of the value that can be associated with the created option.
     * @return A new {@link Option} instance of type {@code T, S}, which
     *         can be used to associate values of type {@code T} with a configuration
     *         for the target type {@code S}.
     */
    public abstract <T> Option<T, S> option();

    /**
     * Creates and returns a new option instance for the configuration associated
     * with the target type {@code S}, initialized with a specified default value.
     *
     * @param <T> The type of the value that can be associated with the created option.
     * @param defaultValue The default value of type {@code T} to be associated with
     *                     the created option. This value will be used when no
     *                     specific value is set for the option.
     * @return A new {@link Option} instance of type {@code T, S},
     *         which can be used to associate values of type {@code T} with a
     *         configuration for the target type {@code S}.
     */
    public abstract <T> Option<T, S> option(T defaultValue);
}
