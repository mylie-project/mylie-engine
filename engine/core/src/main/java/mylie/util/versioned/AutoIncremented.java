package mylie.util.versioned;

/**
 * Represents an auto-incrementing versioned entity that tracks changes to its
 * value by updating a version counter each time the value is modified.
 *
 * @param <T>
 *            The type of the value to be stored and tracked by the entity.
 */
public final class AutoIncremented<T> implements Versioned<T> {
	boolean updateIfEqual = false;
	long version = 0;
	T value;

	/**
	 * Initializes the `AutoIncremented` entity with the specified initial value.
	 * The version is set to its default initial state.
	 *
	 * @param value
	 *            The initial value of type T to be assigned to the entity.
	 */
	public AutoIncremented(T value) {
		this.value = value;
	}

	/**
	 * Initializes the `AutoIncremented` entity with the specified initial value and
	 * configuration for updating the version number when the new value is equal to
	 * the current value.
	 *
	 * @param value
	 *            The initial value of type T to be assigned to the entity.
	 * @param updateIfEqual
	 *            A boolean indicating whether the version should be updated when
	 *            the assigned value is equal to the current value.
	 */
	public AutoIncremented(T value, boolean updateIfEqual) {
		this(value);
		this.updateIfEqual = updateIfEqual;
	}

	public AutoIncremented() {
		this(null, false);
	}

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
	 * Updates the value of the versioned entity, increments the version, and
	 * returns the updated instance.
	 *
	 * @param value
	 *            The new value of type T to be assigned to the versioned entity.
	 * @return The updated instance of Versioned containing the new value.
	 */
	@Override
	public Versioned<T> value(T value) {
		boolean update = false;
		if (value == null) {
			if (this.value == null) {
				if (updateIfEqual) {
					update = true;
				}
			} else {
				update = true;
			}
		} else {
			update = updateIfEqual || !value.equals(this.value);
		}
		this.value = value;
		if (update) {
			version++;
		}
		return this;
	}

	/**
	 * Retrieves the current version number of the entity. The version reflects the
	 * internal state changes and increments with each update to the entity's value.
	 *
	 * @return The current version number as a long, representing the state of the
	 *         entity.
	 */
	@Override
	public long version() {
		return version;
	}
}
