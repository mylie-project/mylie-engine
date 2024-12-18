package mylie.input;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputModule extends BaseCoreComponent implements Lifecycle.Update, Lifecycle.AddRemove {
    private final InputManager inputManager;
    @Getter(AccessLevel.PACKAGE)
    private final List<InputProvider> inputProviders=new CopyOnWriteArrayList<>();
    public InputModule() {
        inputManager = new InputManager(this);
    }

    @Override
    public void onUpdate(Timer.Time time) {
        List<InputEvent> events=new LinkedList<>();
        for (InputProvider inputProvider : inputProviders) {
            events.addAll(inputProvider.inputEvents().result());
        }
    }

    @Override
    public void onAdd() {
        component(inputManager);
    }

    @Override
    public void onRemove() {

    }
}
