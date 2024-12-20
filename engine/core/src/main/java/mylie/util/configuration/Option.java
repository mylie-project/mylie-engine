package mylie.util.configuration;

import lombok.Getter;

public abstract class Option<O, T> {
    @Getter
	final String name;
	final T defaultValue;

	public Option(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
}
