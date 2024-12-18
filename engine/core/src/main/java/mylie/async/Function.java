package mylie.async;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Represents an abstract base class for defining custom functional behaviors.
 * This class encapsulates a name attribute, which can be used to identify or
 * describe the function. Concrete implementations should extend this class and
 * provide specific functionality.
 *
 * Key Features: - Maintains a name for the function instance. - Designed to be
 * extended by other functional classes to implement specific behavior.
 *
 * Accessibility: - The name property is accessible within the same package. -
 * The constructor is protected and intended for subclasses to initialize the
 * function with a specific name.
 */
@Getter(AccessLevel.PACKAGE)
abstract class Function {
	private final String name;

	protected Function(String name) {
		this.name = name;
	}
}
