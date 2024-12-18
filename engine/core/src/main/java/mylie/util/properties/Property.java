package mylie.util.properties;

public abstract class Property<O, T> {
	private final String name;

	public Property(String name) {
		this.name = name;
	}
}
