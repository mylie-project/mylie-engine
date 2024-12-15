package mylie.util.properties;

import java.util.function.Function;
import mylie.util.versioned.Versioned;

/**
 * Represents a property that associates a value of type {@code T} with a context or domain of type {@code S}.
 * A property can be used to set and retrieve versioned values stored in a corresponding {@link Properties} container.
 *
 * @param <T> The type of the value associated with the property.
 * @param <S> The type representing the context or domain where the property is used.
 */
public class Property<T, S> {
    private final Function<S, Properties<S>> storeSupplier;

    /**
     * Constructs a Property instance using the provided store supplier function.
     * The store supplier is responsible for retrieving the {@link Properties} instance
     * associated with a specific context or domain.
     *
     * @param storeSupplier A function that supplies a {@link Properties} container for a given
     *                      context or domain of type {@code S}.
     */
    Property(Function<S, Properties<S>> storeSupplier) {
        this.storeSupplier = storeSupplier;
    }

    /**
     * Sets the specified value for the given property in the context or domain represented by the target.
     * This operation associates the provided value with the property within the scope of the target object.
     *
     * @param target The context or domain of type {@code S} where the property value is to be set.
     * @param value  The value of type {@code T} to associate with the property in the specified context.
     */
    public void set(S target, T value) {
        storeSupplier.apply(target).set(this, value);
    }

    /**
     * Retrieves the versioned value associated with the property for the specified context or domain.
     *
     * @param target The context or domain of type {@code S} for which the versioned value is to be retrieved.
     *               This determines the scope in which the property value is associated.
     * @return A {@code Versioned<T>} object that contains the associated value of the property
     *         and its version information in the given context or domain.
     */
    public Versioned<T> get(S target) {
        return storeSupplier.apply(target).get(this);
    }
}
