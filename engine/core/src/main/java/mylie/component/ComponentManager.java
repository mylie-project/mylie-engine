package mylie.component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.Async;
import mylie.async.Wait;
import mylie.core.Core;
import mylie.core.Timer;

@Slf4j
public final class ComponentManager {
    @Getter(AccessLevel.PACKAGE)
    private final Core core;

    private final List<Component> components = new CopyOnWriteArrayList<>();

    public ComponentManager(Core core) {
        this.core = core;
    }

    public ComponentManager addComponent(Component component) {
        log.trace("Component<{}> added", component.getClass().getSimpleName());
        components.add(component);
        if (component instanceof BaseComponent baseComponent) {
            baseComponent.componentManager(this);
        }
        if (component instanceof Lifecycle.AddRemove addRemove) {
            addRemove.onAdd();
        }
        return this;
    }

    public ComponentManager removeComponent(Component component) {
        log.trace("Component<{}> removed", component.getClass().getSimpleName());
        components.remove(component);
        if (component instanceof Lifecycle.AddRemove addRemove) {
            addRemove.onRemove();
        }
        if (component instanceof BaseComponent baseComponent) {
            baseComponent.componentManager(null);
        }
        return this;
    }

    public <T extends Component> T getComponent(Class<? extends T> type) {
        for (Component component : components) {
            if (type.isAssignableFrom(component.getClass())) {
                return type.cast(component);
            }
        }
        return null;
    }

    public void update(Timer.Time time) {
        Wait.wait(Async.async(components, BaseComponent.class, BaseComponent::update, time));
    }

    public void shutdown(Timer.Time time) {
        Wait.wait(Async.async(components, BaseComponent.class, BaseComponent::shutdown, time));
    }
}
