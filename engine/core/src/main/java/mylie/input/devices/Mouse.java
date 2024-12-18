package mylie.input.devices;

import lombok.Getter;
import lombok.ToString;
import mylie.graphics.GraphicsContext;
import mylie.input.InputEvent;
import mylie.util.versioned.AutoIncremented;
import mylie.util.versioned.Versioned;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.Map;

public class Mouse extends InputDevice {
	private final Map<Button, Versioned<Boolean>> keyState=new java.util.HashMap<>();
	private final Versioned<Vector2ic> cursorPosition=new AutoIncremented<>(new Vector2i());
	private final Versioned<Vector2ic> cursorMotion=new AutoIncremented<>(new Vector2i());
	void update(Button key, boolean pressed) {
		keyState.computeIfAbsent(key,_->new AutoIncremented<>(false)).value(pressed);
	}

	public boolean pressed(Button key) {
		return keyState.computeIfAbsent(key,_->new AutoIncremented<>(false)).value();
	}

	public Versioned.Reference<Boolean> referenced(Button key) {
		return keyState.computeIfAbsent(key,_->new AutoIncremented<>(false)).reference();
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
	public static class MouseEvent extends InputEvent {
		private final Mouse mouse;

		public MouseEvent(GraphicsContext context, Mouse mouse) {
			super(context);
			this.mouse = mouse;
		}
	}
	@ToString
	@Getter
	public static class ButtonEvent extends MouseEvent implements EventType.DigitalEvent {
		private final Button button;
		private final boolean pressed;

		public ButtonEvent(GraphicsContext context, Mouse mouse, Button button, boolean pressed) {
			super(context, mouse);
			this.button = button;
			this.pressed = pressed;
			mouse.update(button, pressed);
		}
	}
	@ToString
	@Getter
	public static class AxisEvent extends MouseEvent implements EventType.AnalogEvent {
		private final MouseAxis axis;
		private final float value;

		public AxisEvent(GraphicsContext context, Mouse mouse, MouseAxis axis, float value) {
			super(context, mouse);
			this.axis = axis;
			this.value = value;
		}
	}
	@ToString
	@Getter
	public static class PositionEvent extends MouseEvent implements EventType.ValueEvent<Vector2ic> {
		private final Vector2ic value;

		public PositionEvent(GraphicsContext context, Mouse mouse, Vector2ic value) {
			super(context, mouse);
			this.value = value;
			mouse.cursorPosition.value(value);
		}
	}
	@ToString
	@Getter
	public static class MotionEvent extends MouseEvent implements EventType.ValueEvent<Vector2ic> {
		private final Vector2ic value;
		public MotionEvent(GraphicsContext context, Mouse mouse, Vector2ic value) {
			super(context, mouse);
			this.value = value;
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

	public record MousePosition(String name) {
		public static final MousePosition POSITION = new MousePosition("POSITION");
	}
}
