package mylie.graphics;

import mylie.component.ComponentManager;

public abstract class Api {
	protected abstract void onInitialize(ComponentManager componentManager);

	protected abstract ContextProvider contextProvider();
}
