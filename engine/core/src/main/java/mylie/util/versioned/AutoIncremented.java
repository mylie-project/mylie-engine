package mylie.util.versioned;

/**
 * Represents an auto-incrementing versioned entity that tracks changes
 * to its value by updating a version counter each time the value is modified.
 *
 * @param <T> The type of the value to be stored and tracked by the entity.
 */
public final class AutoIncremented<T> implements Versioned<T> {
    long version = 0;
    T value;

    /**
     * Initializes the `AutoIncremented` entity with the specified initial value.
     * The version is set to its default initial state.
     *
     * @param value The initial value of type T to be assigned to the entity.
     */
    public AutoIncremented(T value) {
        this.value = value;
    }

    /**
     * Default constructor for the `AutoIncremented` class.
     * Initializes an empty instance where the value is uninitialized and
     * the version is set to its default initial state (zero).
     */
    public AutoIncremented() {}

    /**
     * Retrieves the current value of the entity.
     *
     * @return The current value of type T.
     */
    @Override
    public T value() {
        return value;
    }

    /**
     * Updates the value of the versioned entity, increments the version,
     * and returns the updated instance.
     *
     * @param value The new value of type T to be assigned to the versioned entity.
     * @return The updated instance of Versioned containing the new value.
     */
    @Override
    public Versioned<T> value(T value) {
        this.value = value;
        version++;
        return this;
    }

    /**
     * Retrieves the current version number of the entity.
     * The version reflects the internal state changes and increments with each update to the entity's value.
     *
     * @return The current version number as a long, representing the state of the entity.
     */
    @Override
    public long version() {
        return version;
    }
}
