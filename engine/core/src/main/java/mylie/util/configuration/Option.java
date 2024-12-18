package mylie.util.configuration;

public abstract class Option<O, T> {
	final String name;
	final T defaultValue;

	public Option(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
}
