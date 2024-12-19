package mylie.input.devices;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.graphics.GraphicsContext;
import mylie.util.versioned.AutoIncremented;
import mylie.util.versioned.Versioned;

public class Gamepad extends InputDevice {
	@Getter
	final int id;
	@Getter
	@Setter(AccessLevel.PROTECTED)
	String name;
	final Class<?> provider;
	public Gamepad(int id, Class<?> provider) {
		this.id = id;
		this.provider = provider;
	}
	private final Versioned<Boolean> connected = new AutoIncremented<>(false);
	private final Map<Gamepad.Button, Versioned<Boolean>> keyState = new java.util.HashMap<>();
	private final Map<Gamepad.Axis, Versioned<Float>> axisState = new java.util.HashMap<>();

	public boolean button(Gamepad.Button key) {
		return keyState.computeIfAbsent(key, _ -> new AutoIncremented<>(false)).value();
	}

	public Versioned.Reference<Boolean> buttonReference(Gamepad.Button key) {
		return keyState.computeIfAbsent(key, _ -> new AutoIncremented<>(false)).reference();
	}

	public float axis(Gamepad.Axis key) {
		return axisState.computeIfAbsent(key, _ -> new AutoIncremented<>(0.0f)).value();
	}

	public Versioned.Reference<Float> axisReference(Gamepad.Axis key) {
		return axisState.computeIfAbsent(key, _ -> new AutoIncremented<>(0.0f)).reference();
	}

	public boolean connected() {
		return connected.value();
	}

	public Versioned.Reference<Boolean> connectedReference() {
		return connected.reference();
	}

	public static class GamepadEvent<T> extends DeviceEvent<Gamepad, T> {

		public GamepadEvent(GraphicsContext context, Gamepad device, T value) {
			super(context, device, value);
		}
	}

	@Getter
	public static class ConnectedEvent extends GamepadEvent<Boolean> {

		public ConnectedEvent(GraphicsContext context, Gamepad device, boolean connected) {
			super(context, device, connected);
			device.connected.value(connected);
		}
		@Override
		public String toString() {
			return "Gamepad<%d>[Connected]: %s".formatted(device().id, value());
		}
	}

	@Getter
	public static class ButtonEvent extends GamepadEvent<Boolean> {
		final Button button;

		public ButtonEvent(GraphicsContext context, Gamepad device, Button button, boolean pressed) {
			super(context, device, pressed);
			this.button = button;
			device.keyState.computeIfAbsent(button, _ -> new AutoIncremented<>(pressed)).value(pressed);
		}
		@Override
		public String toString() {
			return "Gamepad<%s:%d>[%s]: %s".formatted(device.name(), device().id, button.name, value());
		}
	}

	@Getter
	public static class AxisEvent extends GamepadEvent<Float> {
		final Axis axis;

		public AxisEvent(GraphicsContext context, Gamepad device, Axis axis, float value) {
			super(context, device, value);
			this.axis = axis;
			device.axisState.computeIfAbsent(axis, _ -> new AutoIncremented<>(value)).value(value);
		}

		@Override
		public String toString() {
			return "Gamepad<%s:%d>[%s]: %s".formatted(device.name(), device().id, axis.name, value());
		}
	}

	@SuppressWarnings("unused")
	public record Button(String name) {
		public static final Button A = new Button("A");
		public static final Button B = new Button("B");
		public static final Button X = new Button("X");
		public static final Button Y = new Button("Y");
		public static final Button LEFT_BUMPER = new Button("LEFT_BUMPER");
		public static final Button RIGHT_BUMPER = new Button("RIGHT_BUMPER");
		public static final Button LEFT_THUMP = new Button("LEFT_THUMP");
		public static final Button RIGHT_THUMP = new Button("RIGHT_THUMP");
		public static final Button START = new Button("START");
		public static final Button SELECT = new Button("SELECT");
		public static final Button DPAD_LEFT = new Button("DPAD_LEFT");
		public static final Button DPAD_RIGHT = new Button("DPAD_RIGHT");
		public static final Button DPAD_UP = new Button("DPAD_UP");
		public static final Button DPAD_DOWN = new Button("DPAD_DOWN");
	}

	@SuppressWarnings("unused")
	public record Axis(String name) {
		public static final Axis LEFT_STICK_X = new Axis("LEFT_STICK_X");
		public static final Axis LEFT_STICK_Y = new Axis("LEFT_STICK_Y");
		public static final Axis RIGHT_STICK_X = new Axis("RIGHT_STICK_X");
		public static final Axis RIGHT_STICK_Y = new Axis("RIGHT_STICK_Y");
		public static final Axis LEFT_TRIGGER = new Axis("LEFT_TRIGGER");
		public static final Axis RIGHT_TRIGGER = new Axis("RIGHT_TRIGGER");
	}
}
