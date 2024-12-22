package mylie.input.xinput;

import com.github.strikerx3.jxinput.*;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.component.AppComponent;
import mylie.component.AppComponentParallel;
import mylie.component.Lifecycle;
import mylie.input.InputEvent;
import mylie.input.InputManager;
import mylie.input.InputProvider;
import mylie.input.devices.Gamepad;
@Slf4j
public class XinputProvider extends AppComponentParallel implements AppComponent, InputProvider, Lifecycle.InitDestroy {
	private final List<XInputGamepad> gamepads = new LinkedList<>();
	public XinputProvider() {
		executionMode(new ExecutionMode(ExecutionMode.Mode.Direct, Target.Background, Caches.Unlimited));
	}

	@Override
	public Result<List<InputEvent<?>>> inputEvents() {
		List<InputEvent<?>> eventList = new LinkedList<>();

		for (XInputGamepad gamepad : gamepads) {
			gamepad.update(eventList);
		}
		return Results.fixed(-1, -1, eventList);
	}

	@Override
	public void onInit() {
		log.info("Loading XInput");
		try {
			XInputDevice14[] allDevices1 = XInputDevice14.getAllDevices();
			log.error("Xinput 14 {}", XInputDevice14.isAvailable());
			XInputDevice[] allDevices = XInputDevice.getAllDevices();
			for (int i = 0; i < allDevices.length; i++) {
				gamepads.add(new XInputGamepad(i, allDevices[i]));
			}
			log.info("Loaded {} gamepads", gamepads.size());
			component(InputManager.class).addInputProvider(this);
		} catch (XInputNotLoadedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onDestroy() {

	}

	public static class XInputGamepad extends Gamepad {
		final XInputDevice device;
		int left, right;
		public XInputGamepad(int id, XInputDevice device) {
			super(id, XinputProvider.class);
			this.device = device;
			rumbleMotors(2);
		}

		/*
		 * @Override public void rumble(int motor, float strength) { if (motor == 0) {
		 * left = (int) (strength * 65535); } else if (motor == 1) { right = (int)
		 * (strength * 65535); } device.setVibration(Math.min(left, 65535),
		 * Math.min(right, 65535)); }
		 */

		public void update(List<InputEvent<?>> eventsList) {
			boolean connected = device.isConnected();
			if (connected() != connected) {
				if (connected) {
					name("XBox Controller");
					eventsList.add(new Gamepad.ConnectedEvent(null, this, true));
				} else {
					eventsList.add(new Gamepad.ConnectedEvent(null, this, false));
				}
			}
			if (connected && device.poll()) {
				XInputComponents components = device.getComponents();
				XInputButtons buttons = components.getButtons();
				XInputAxes axes = components.getAxes();
				checkButton(buttons.a, Button.A, eventsList);
				checkButton(buttons.b, Button.B, eventsList);
				checkButton(buttons.x, Button.X, eventsList);
				checkButton(buttons.y, Button.Y, eventsList);
				checkButton(buttons.back, Button.SELECT, eventsList);
				checkButton(buttons.start, Button.START, eventsList);
				checkButton(buttons.lThumb, Button.LEFT_THUMP, eventsList);
				checkButton(buttons.rThumb, Button.RIGHT_THUMP, eventsList);
				checkButton(buttons.lShoulder, Button.LEFT_BUMPER, eventsList);
				checkButton(buttons.rShoulder, Button.RIGHT_BUMPER, eventsList);
				checkButton(buttons.left, Button.DPAD_LEFT, eventsList);
				checkButton(buttons.right, Button.DPAD_RIGHT, eventsList);
				checkButton(buttons.up, Button.DPAD_UP, eventsList);
				checkButton(buttons.down, Button.DPAD_DOWN, eventsList);
				checkAxis(axes.lt, Axis.LEFT_TRIGGER, eventsList);
				checkAxis(axes.rt, Axis.RIGHT_TRIGGER, eventsList);
				checkAxis(axes.lx, Axis.LEFT_STICK_X, eventsList);
				checkAxis(axes.ly, Axis.LEFT_STICK_Y, eventsList);
				checkAxis(axes.rx, Axis.RIGHT_STICK_X, eventsList);
				checkAxis(axes.ry, Axis.RIGHT_STICK_Y, eventsList);
			}
		}

		private void checkButton(boolean pressed, Gamepad.Button button, List<InputEvent<?>> eventsList) {
			if (button(button) != pressed) {
				eventsList.add(new Gamepad.ButtonEvent(null, this, button, pressed));
			}
		}

		private void checkAxis(float value, Gamepad.Axis axis, List<InputEvent<?>> eventsList) {
			if (axis(axis) != value) {
				eventsList.add(new Gamepad.AxisEvent(null, this, axis, value));
			}
		}
	}
}
