package mylie.component;

import mylie.core.EngineConfiguration;

public class BaseCoreComponent extends BaseComponent implements CoreComponent {

	protected <T extends Component> T component(Class<? extends T> type) {
		return componentManager().getComponent(type);
	}

	protected <T extends Component> void component(T component) {
		componentManager().addComponent(component);
	}

	protected <T extends Component> void removeComponent(T component) {
		componentManager().removeComponent(component);
	}

	protected <T> T engineOption(EngineConfiguration.EngineOption<T> option) {
		return componentManager().core().configuration().option(option);
	}

	protected ComponentManager componentManager() {
		return super.componentManager();
	}

	protected void runAfter(Class<? extends Component> component) {
		Component theComponent = component(component);
		if (theComponent instanceof BaseComponent baseComponent) {
			dependencies().add(baseComponent);
		}
	}
}
