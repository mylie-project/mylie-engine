package mylie.input;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.application.Application;
import mylie.async.Async;
import mylie.async.Caches;
import mylie.async.ExecutionMode;
import mylie.async.Functions;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Timer;
import mylie.input.devices.Gamepad;
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;

@Getter(AccessLevel.PACKAGE)
public class InputModule extends BaseCoreComponent implements Lifecycle.Update, Lifecycle.AddRemove {
	private final ExecutionMode eventExecutionMode = new ExecutionMode(ExecutionMode.Mode.Async, Application.Target,
			Caches.No);
	private final InputManager inputManager;
	private final Keyboard keyboard = new Keyboard();
	private final Mouse mouse = new Mouse();
	private final Set<Gamepad> gamepads = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private final List<InputProvider> inputProviders = new CopyOnWriteArrayList<>();
	private final List<RawInputListener> inputListeners = new CopyOnWriteArrayList<>();

	public InputModule() {
		inputManager = new InputManager(this);
	}

	@Override
	public void onUpdate(Timer.Time time) {
		List<InputEvent<?>> events = new LinkedList<>();
		for (InputProvider inputProvider : inputProviders) {
			events.addAll(inputProvider.inputEvents().result());
		}
		for (InputEvent<?> event : events) {
			if (event instanceof Gamepad.ConnectedEvent connectedEvent) {
				if (connectedEvent.value()) {
					gamepads().add(connectedEvent.device());
				}
			}

			for (RawInputListener inputListener : inputListeners()) {
				Async.async(eventExecutionMode, time.version(), NotifyInputListener, inputListener, event);
			}
		}
	}

	@Override
	public void onAdd() {
		component(inputManager);
	}

	@Override
	public void onRemove() {
	}

	private static final Functions.F1<Async.Void, RawInputListener, InputEvent<?>> NotifyInputListener = new Functions.F1<>(
			"NotifyInputListener") {

		@Override
		protected Async.Void run(RawInputListener object, InputEvent<?> inputEvent) {
			object.onEvent(inputEvent);
			return Async.VOID;
		}
	};
}
