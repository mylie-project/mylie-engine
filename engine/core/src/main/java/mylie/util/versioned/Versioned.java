package mylie.util.versioned;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a versioned entity that maintains a version number alongside its value.
 * This interface provides methods to get and set the value while tracking changes
 * through versioning.
 *
 * @param <T> The type of the value being versioned.
 */
public sealed interface Versioned<T> permits AutoIncremented {

    /**
     * Retrieves the current value of the entity, ensuring it is up-to-date
     * based on the tracked version.
     *
     * @return The current value of type T, potentially updated based on version changes.
     */
    T value();

    /**
     * Updates the value of the versioned entity and returns the updated instance.
     *
     * @param value The new value of type T to be assigned to the versioned entity.
     * @return The updated instance of Versioned containing the new value.
     */
    Versioned<T> value(T value);

    /**
     * Retrieves the current version number of the versioned entity.
     * The version is used to track changes and ensure the consistency of the entity's value.
     *
     * @return The version number as a long that represents the current state of the entity.
     */
    long version();

    /**
     * Retrieves a reference to the versioned entity. The reference ensures that
     * the value remains consistent with its parent versioned entity by synchronizing
     * the version and value when necessary.
     *
     * @return A {@code Reference<T>} object that tracks the versioned value and
     * maintains consistency with the parent entity.
     */
    default Reference<T> reference() {
        return new Reference<>(this, value(), version());
    }

    /**
     * Represents a reference to a versioned value, allowing the value to
     * remain consistent with its parent versioned entity. The reference ensures
     * that the value is only updated when there is a version mismatch between
     * the referenced value and the parent.
     *
     * @param <T> The type of the value tracked by this reference.
     */
    @Getter
    @AllArgsConstructor
    class Reference<T> {
        @Getter(AccessLevel.NONE)
        final Versioned<T> parent;

        T value;
        long version;

        /**
         * Retrieves the value, ensuring it is consistent with the parent versioned entity.
         * If the version of the parent entity does not match the current version, the value
         * is updated from the parent, and the version is synchronized.
         *
         * @return The current value of type T, updated if a version mismatch is detected.
         */
        public T value() {
            if (parent.version() != version) {
                value = parent.value();
                version = parent.version();
            }
            return value;
        }

        /**
         * Checks whether the version of the parent entity differs from the current version
         * of the reference. This indicates if the value may need to be updated to remain
         * consistent with the parent.
         *
         * @return true if the current version differs from the parent's version, false otherwise.
         */
        public boolean changed() {
            return parent.version() != version;
        }
    }
}
