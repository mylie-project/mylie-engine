package mylie.application;

import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Setter;
import mylie.component.AppComponent;
import mylie.component.ComponentManager;

public abstract class BaseApplication implements Application {
	@Setter(AccessLevel.PACKAGE)
	private ComponentManager componentManager;

	protected <T extends AppComponent> T component(Class<T> type) {
		return componentManager.getComponent(type);
	}

	protected <T extends AppComponent> void component(T component) {
		componentManager.addComponent(component);
	}

	@Override
	public void onInitialize(Consumer<? extends AppComponent> initializer) {
		onInitialize();
	}

	protected abstract void onInitialize();
}
