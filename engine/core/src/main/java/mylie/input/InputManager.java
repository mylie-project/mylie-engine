package mylie.input;

import mylie.component.AppComponent;
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;

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

	public void addInputListener(RawInputListener listener) {
		inputModule.inputListeners().add(listener);
	}

	public void removeInputListener(RawInputListener listener) {
		inputModule.inputListeners().remove(listener);
	}

	public Keyboard keyboard() {
		return inputModule.keyboard();
	}

	public Mouse mouse() {
		return inputModule.mouse();
	}
}
