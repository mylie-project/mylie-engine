package mylie.util.properties;

import java.util.function.Function;

/**
 * An abstract factory class for creating and managing properties and property containers.
 * It outlines the structure for implementing property management tied to a specific context.
 *
 * @param <S> The type representing the context or domain for which properties are managed.
 */
public abstract class PropertiesFactory<S> {
    final Function<S, Properties<S>> storeSupplier;

    /**
     * Constructs a PropertiesFactory with the specified store supplier function.
     *
     * @param storeSupplier A function that creates a {@link Properties} instance
     *                       for a given context or domain of type {@code S}.
     */
    public PropertiesFactory(Function<S, Properties<S>> storeSupplier) {
        this.storeSupplier = storeSupplier;
    }

    /**
     * Provides a {@link Properties} instance for managing properties associated with
     * a specific context or domain of type {@code S}.
     *
     * @return A {@link Properties} instance associated with the context of type {@code S}.
     */
    public abstract Properties<S> properties();

    /**
     * Creates and returns a new property instance of type {@code T} associated with the
     * context or domain of type {@code S}.
     *
     * The created property acts as a key to manage and track values in the related
     * {@link Properties} container for a given context. Each property can store and retrieve
     * values specific to the provided context.
     *
     * @param <T> The type of the value associated with the property.
     * @return A {@link Property} instance that can be used to interact with
     *         the {@link Properties} container of a given context.
     */
    public abstract <T> Property<T, S> property();
}
