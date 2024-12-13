package mylie.async;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
abstract class Function {
    private final String name;

    protected Function(String name) {
        this.name = name;
    }
}
