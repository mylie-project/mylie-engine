package mylie.component;

import mylie.core.EngineConfiguration;

public sealed abstract class BaseAppComponent extends BaseComponent
		permits AppComponentSequential, AppComponentParallel {
	protected <T extends AppComponent> T component(Class<? extends T> type) {
		return componentManager().getComponent(type);
	}

	protected <T extends AppComponent> void component(T component) {
		componentManager().addComponent(component);
	}

	protected <T extends AppComponent> void removeComponent(T component) {
		componentManager().removeComponent(component);
	}

	protected <T> T engineOption(EngineConfiguration.EngineOption<T> option) {
		return componentManager().core().configuration().option(option);
	}
}
