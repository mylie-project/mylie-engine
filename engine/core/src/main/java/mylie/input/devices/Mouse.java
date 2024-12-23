package mylie.input.devices;

import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import mylie.graphics.GraphicsContext;
import mylie.util.versioned.AutoIncremented;
import mylie.util.versioned.Versioned;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class Mouse extends InputDevice {
	private final Map<Button, Versioned<Boolean>> keyState = new java.util.HashMap<>();
	private final Versioned<Vector2ic> cursorPosition = new AutoIncremented<>(new Vector2i());
	private final Versioned<Vector2ic> cursorMotion = new AutoIncremented<>(new Vector2i());
	void update(Button key, boolean pressed) {
		keyState.computeIfAbsent(key, _ -> new AutoIncremented<>(false)).value(pressed);
	}

	public boolean pressed(Button key) {
		return keyState.computeIfAbsent(key, _ -> new AutoIncremented<>(false)).value();
	}

	public Versioned.Reference<Boolean> referenced(Button key) {
		return keyState.computeIfAbsent(key, _ -> new AutoIncremented<>(false)).reference();
	}

	public Vector2ic position() {
		return cursorPosition.value();
	}

	public Versioned.Reference<Vector2ic> referencedPosition() {
		return cursorPosition.reference();
	}

	public Vector2ic motion() {
		return cursorMotion.value();
	}

	public Versioned.Reference<Vector2ic> referencedMotion() {
		return cursorMotion.reference();
	}

	@Getter
	public static class MouseEvent<T> extends DeviceEvent<Mouse, T> {
		public MouseEvent(GraphicsContext context, Mouse mouse, T value) {
			super(context, mouse, value);
		}
	}
	@ToString
	@Getter
	public static class ButtonEvent extends MouseEvent<Boolean> {
		private final Button button;
		private final int modifiers;
		public ButtonEvent(GraphicsContext context, Mouse mouse, Button button, boolean pressed, int modifiers) {
			super(context, mouse, pressed);
			this.button = button;
			this.modifiers = modifiers;
			mouse.update(button, pressed);
		}
	}
	@ToString
	@Getter
	public static class AxisEvent extends MouseEvent<Float> {
		private final MouseAxis axis;

		public AxisEvent(GraphicsContext context, Mouse mouse, MouseAxis axis, float value) {
			super(context, mouse, value);
			this.axis = axis;
		}
	}
	@ToString
	@Getter
	public static class PositionEvent extends MouseEvent<Vector2ic> {
		public PositionEvent(GraphicsContext context, Mouse mouse, Vector2ic value) {
			super(context, mouse, value);
			mouse.cursorPosition.value(value);
		}
	}
	@ToString
	@Getter
	public static class MotionEvent extends MouseEvent<Vector2ic> {
		public MotionEvent(GraphicsContext context, Mouse mouse, Vector2ic value) {
			super(context, mouse, value);
			mouse.cursorMotion.value(value);
		}
	}

	public record Button(String name) {
		public static final Button LEFT = new Button("LEFT");
		public static final Button RIGHT = new Button("RIGHT");
		public static final Button MIDDLE = new Button("MIDDLE");
		public static final Button BUTTON_4 = new Button("BUTTON_4");
		public static final Button BUTTON_5 = new Button("BUTTON_5");
		public static final Button BUTTON_6 = new Button("BUTTON_6");
		public static final Button BUTTON_7 = new Button("BUTTON_7");
		public static final Button BUTTON_8 = new Button("BUTTON_8");
		public static final Button UNKNOWN = new Button("UNKNOWN");
	}

	public record MouseAxis(String name) {
		public static final MouseAxis X = new MouseAxis("X");
		public static final MouseAxis Y = new MouseAxis("Y");
		public static final MouseAxis WHEEL = new MouseAxis("WHEEL");
	}
}
