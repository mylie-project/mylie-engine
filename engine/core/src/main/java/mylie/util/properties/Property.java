package mylie.util.properties;

import mylie.util.versioned.Versioned;

public abstract class Property<O,T> {
    private final String name;

    public Property(String name) {
        this.name = name;
    }
}
