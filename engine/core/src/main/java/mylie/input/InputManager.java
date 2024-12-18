package mylie.input;

import mylie.component.AppComponent;

public class InputManager implements AppComponent {
	private final InputModule inputModule;

	public InputManager(InputModule inputModule) {
		this.inputModule = inputModule;
	}

	public void addInputProvider(InputProvider provider) {
		inputModule.inputProviders().add(provider);
	}

	public void removeInputProvider(InputProvider provider) {
		inputModule.inputProviders().remove(provider);
	}
}
