package mylie.util.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import mylie.util.versioned.Versioned;

/**
 * A factory implementation of {@link PropertiesFactory} that utilizes a map-based approach
 * to manage properties and their associated versioned values. This implementation uses a supplier
 * to provide versioned containers for property values and ensures that each property is managed
 * within a map structure.
 *
 * @param <S> The type representing the context or domain for which the properties are managed.
 */
public class MapBasedPropertiesFactory<S> extends PropertiesFactory<S> {
    private final Supplier<Versioned<?>> versionedSupplier;

    /**
     * Constructs a {@code MapBasedPropertiesFactory} instance with the specified store supplier
     * and a versioned supplier. This factory utilizes a map-based approach to manage properties
     * and their associated versioned values within a specified context or domain.
     *
     * @param storeSupplier A function that provides a {@link Properties} instance
     *                      for a given context or domain of type {@code S}.
     * @param versionedSupplier A supplier that generates {@link Versioned} objects
     *                          to manage the versioned state of property values.
     */
    public MapBasedPropertiesFactory(
            Function<S, Properties<S>> storeSupplier, Supplier<Versioned<?>> versionedSupplier) {
        super(storeSupplier);
        this.versionedSupplier = versionedSupplier;
    }

    /**
     * Creates and retrieves a new {@link Properties} instance for managing properties
     * within the specified context or domain of type {@code S}. This implementation
     * utilizes a map-based approach to manage properties and their associated versioned values.
     *
     * The {@link Properties} instance provides mechanisms to set, retrieve, and store
     * versioned values tied to specific properties, ensuring thread-safe and consistent
     * property management.
     *
     * @return A {@link Properties} instance of type {@code S} that allows
     *         managing properties and their associated versioned values.
     */
    @Override
    public Properties<S> properties() {
        return new MapBasedProperties<>(versionedSupplier);
    }

    /**
     * Creates and returns a new {@link Property} instance of the specified type {@code T} that
     * is associated with the context or domain of type {@code S}. The property is used to
     * manage and retrieve versioned values in the corresponding {@link Properties} container
     * for the given context.
     *
     * @param <T> The type of the value associated with the property.
     * @return A {@link Property} instance of type {@code T} and associated with the context
     *         or domain of type {@code S}.
     */
    @Override
    public <T> Property<T, S> property() {
        return new Property<>(storeSupplier);
    }

    /**
     * A map-based implementation of {@link Properties}, which utilizes a {@link HashMap}
     * to store properties and their associated versioned values. This class provides mechanisms
     * to manage versioned properties within a specific context or domain.
     *
     * @param <S> The type representing the context or domain for which properties are managed.
     */
    private static class MapBasedProperties<S> extends Properties<S> {
        private final Map<Property<?, S>, Versioned<?>> store = new HashMap<>();
        private final Supplier<Versioned<?>> versionedSupplier;

        /**
         * Constructs a new instance of {@code MapBasedProperties} using the provided supplier
         * of {@link Versioned} objects. This constructor initializes the internal versionedSupplier
         * which is used to generate default versioned instances for handling properties.
         *
         * @param versionedSupplier A {@link Supplier} that provides {@link Versioned} instances.
         *                          This supplier is utilized to create default versioned values
         *                          when a property does not already have an associated versioned value.
         */
        private MapBasedProperties(Supplier<Versioned<?>> versionedSupplier) {
            this.versionedSupplier = versionedSupplier;
        }

        /**
         * Sets a value for the specified property in the context or domain represented by this properties container.
         * Updates the versioned value associated with the given property.
         *
         * @param <T> The type of the value to assign to the property.
         * @param property The property for which the value will be set. This associates the property with a value in the specific domain or context.
         * @param value The value to assign to the specified property.
         */
        @Override
        <T> void set(Property<T, S> property, T value) {
            getVersioned(property).value(value);
        }

        /**
         * Retrieves the versioned value associated with the specified property.
         * This method ensures that the returned value is consistent with the state
         * of the property in the given context or domain.
         *
         * @param <T> The type of the value associated with the property.
         * @param property The property whose versioned value is to be retrieved.
         *                 This represents the key for accessing the associated value.
         * @return A {@code Versioned<T>} object representing the value and its version information
         *         associated with the specified property.
         */
        @Override
        <T> Versioned<T> get(Property<T, S> property) {
            return getVersioned(property);
        }

        /**
         * Retrieves a versioned instance associated with the specified property. If no versioned
         * instance exists for the property, a new instance is created using the versioned supplier
         * and stored for future access.
         *
         * @param <T> The type of the value associated with the property.
         * @param property The property whose versioned instance is to be retrieved or created.
         *                 Represents the key for accessing or storing a versioned value.
         * @return A {@code Versioned<T>} object that contains the value and its version information
         *         associated with the specified property. If no existing versioned instance is found,
         *         a new one is created and returned.
         */
        @SuppressWarnings("unchecked")
        private <T> Versioned<T> getVersioned(Property<T, S> property) {
            Versioned<T> objectVersioned = (Versioned<T>) store.get(property);
            if (objectVersioned == null) {
                objectVersioned = (Versioned<T>) versionedSupplier.get();
                store.put(property, objectVersioned);
            }
            return objectVersioned;
        }
    }
}
