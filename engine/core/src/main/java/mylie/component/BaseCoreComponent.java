package mylie.component;

import mylie.core.Core;
import mylie.core.EngineConfiguration;
import mylie.util.configuration.Option;
import mylie.util.properties.Property;
import mylie.util.versioned.Versioned;

public class BaseCoreComponent extends BaseComponent implements CoreComponent {

    protected <T extends Component> T component(Class<? extends T> type) {
        return componentManager().getComponent(type);
    }

    protected <T extends Component> void addComponent(T component) {
        componentManager().addComponent(component);
    }

    protected <T extends Component> void removeComponent(T component) {
        componentManager().removeComponent(component);
    }

    protected <T> T engineOption(Option<T, EngineConfiguration> option) {
        return option.get(componentManager().core().configuration());
    }

    protected <T> Versioned<T> engineProperty(Property<T, Core> property) {
        return property.get(componentManager().core());
    }

    protected ComponentManager componentManager() {
        return super.componentManager();
    }
}
