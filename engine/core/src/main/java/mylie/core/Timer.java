package mylie.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.component.CoreComponent;
import mylie.util.versioned.AutoIncremented;
import mylie.util.versioned.Versioned;

public abstract class Timer implements CoreComponent {

    @Getter
    private final Versioned<Time> time = new AutoIncremented<>();

    @Getter(AccessLevel.PROTECTED)
    private final Settings settings;

    protected Timer(Settings settings) {
        this.settings = settings;
    }

    Time update(long version) {
        time.value(getTime(version));
        return time.value();
    }

    protected abstract Time getTime(long version);

    @Setter(AccessLevel.PROTECTED)
    @Getter
    public static class Time {
        long version;
        double delta;
        double deltaMod;
    }

    @Getter
    @Setter
    public abstract static class Settings {
        float timeModifier;

        protected abstract Timer build();
    }
}
