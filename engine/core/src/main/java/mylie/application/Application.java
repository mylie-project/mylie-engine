package mylie.application;

import java.util.function.Consumer;
import mylie.async.Target;
import mylie.component.AppComponent;
import mylie.core.Timer;

public interface Application extends AppComponent {
    Target Target = new Target("Application");

    void onInitialize(Consumer<? extends AppComponent> initializer);

    void onUpdate(Timer.Time time);

    void onShutdown();
}
