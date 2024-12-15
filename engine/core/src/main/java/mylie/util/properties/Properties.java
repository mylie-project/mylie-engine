package mylie.util.properties;

import mylie.util.versioned.Versioned;

/**
 * An abstract class representing a container for managing properties and their associated values
 * within a specified context or domain. This class provides mechanisms to set and retrieve
 * versioned values tied to specific properties.
 *
 * @param <S> The type representing the context or domain for which properties are managed.
 */
public abstract class Properties<S> {
    Properties() {}

    /**
     * Sets a value to the specified property within the context or domain.
     *
     * @param <T> The type of the value to be set for the property.
     * @param property The property to which the value will be associated.
     * @param value The value to assign to the given property.
     */
    abstract <T> void set(Property<T, S> property, T value);

    /**
     * Retrieves the versioned value associated with the specified property.
     *
     * @param <T> The type of the value associated with the property.
     * @param property The property for which the versioned value is to be retrieved.
     *                 This specifies the property and its context or domain.
     * @return A {@code Versioned<T>} object representing the value of the property,
     *         along with its version information.
     */
    abstract <T> Versioned<T> get(Property<T, S> property);
}
