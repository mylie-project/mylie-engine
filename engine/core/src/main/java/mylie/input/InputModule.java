package mylie.input;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
import mylie.util.versioned.AutoIncremented;

@Getter(AccessLevel.PACKAGE)
public class InputModule extends BaseCoreComponent implements Lifecycle.Update, Lifecycle.AddRemove {
	private final ExecutionMode eventExecutionMode = new ExecutionMode(ExecutionMode.Mode.Async, Application.Target,
			Caches.No);
	private final InputManager inputManager;
	private final Keyboard keyboard = new Keyboard();
	private final Mouse mouse = new Mouse();
	private final VirtualGamepad[] virtualGamepads = new VirtualGamepad[4];
	private final Set<Gamepad> gamepads = new HashSet<>();
	private final Map<Gamepad, Integer> gamepadMapping = new ConcurrentHashMap<>();

	@Getter(AccessLevel.PACKAGE)
	private final List<InputProvider> inputProviders = new CopyOnWriteArrayList<>();
	private final List<RawInputListener> inputListeners = new CopyOnWriteArrayList<>();

	public InputModule() {
		inputManager = new InputManager(this);
		for (int i = 0; i < virtualGamepads().length; i++) {
			virtualGamepads[i] = new VirtualGamepad(i, getClass());
		}
	}

	@Override
	public void onUpdate(Timer.Time time) {
		List<InputEvent<?>> events = new LinkedList<>();
		for (InputProvider inputProvider : inputProviders) {
			events.addAll(inputProvider.inputEvents().result());
		}
		InputEvent<?> eventToForward = null;
		for (InputEvent<?> event : events) {
			eventToForward = event;
			if (event instanceof Gamepad.ConnectedEvent connectedEvent) {
				if (connectedEvent.value()) {
					gamepads().add(connectedEvent.device());
				} else {
					gamepads().remove(connectedEvent.device());
				}
			}
			if (event instanceof Gamepad.GamepadEvent<?> gamepadEvent) {
				Integer virtualGamePadId = gamepadMapping.get(gamepadEvent.device());
				if (virtualGamePadId != null) {
					eventToForward = virtualGamepads[virtualGamePadId].processEvent(gamepadEvent);
				}
			}
			if (eventToForward != null) {
				for (RawInputListener inputListener : inputListeners()) {
					Async.async(eventExecutionMode, time.version(), NotifyInputListener, inputListener, event);
				}
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

	private final static class VirtualGamepad extends Gamepad {
		private Gamepad gamepad;
		public VirtualGamepad(int id, Class<?> provider) {
			super(id, provider);
			name("Virtual Gamepad " + id);
		}

		public GamepadEvent<?> processEvent(GamepadEvent<?> event) {
			GamepadEvent<?> newEvent = null;
			if (event instanceof Gamepad.ButtonEvent buttonEvent) {
				newEvent = new ButtonEvent(buttonEvent.context(), this, buttonEvent.button(), buttonEvent.value());
				buttonState().computeIfAbsent(buttonEvent.button(), _ -> new AutoIncremented<>())
						.value(buttonEvent.value());
			}
			if (event instanceof Gamepad.AxisEvent axisEvent) {
				newEvent = new AxisEvent(axisEvent.context(), this, axisEvent.axis(), axisEvent.value());
				axisState().computeIfAbsent(axisEvent.axis(), _ -> new AutoIncremented<>()).value(axisEvent.value());
			}
			if (event instanceof Gamepad.ConnectedEvent connectedEvent) {
				newEvent = new ConnectedEvent(connectedEvent.context(), this, connectedEvent.value());
				connected(connectedEvent.value());
			}
			return newEvent;
		}
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
