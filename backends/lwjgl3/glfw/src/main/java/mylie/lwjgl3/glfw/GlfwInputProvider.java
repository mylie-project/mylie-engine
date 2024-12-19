package mylie.lwjgl3.glfw;

import java.util.LinkedList;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.core.Engine;
import mylie.graphics.GraphicsContext;
import mylie.input.InputEvent;
import mylie.input.InputManager;
import mylie.input.InputProvider;
import mylie.input.devices.Gamepad;
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MemoryStack;

@Slf4j
public class GlfwInputProvider implements InputProvider {
	private final ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Engine.Target,
			Caches.OneFrame);
	private static final List<GlfwGamepad> gamepads = new LinkedList<>();
	private final Keyboard defaultKeyboard;
	private final Mouse defaultMouse;

	@Setter
	private List<InputEvent<?>> eventList = null;

	public GlfwInputProvider(InputManager inputManager) {
		defaultKeyboard = inputManager.keyboard();
		defaultMouse = inputManager.mouse();
		for (int i = 0; i < 4; i++) {
			gamepads.add(new GlfwGamepad(i));
		}
	}

	@Override
	public Result<List<InputEvent<?>>> inputEvents() {
		return Async.async(executionMode, -1, PollEvents, this);
	}

	private static final Functions.F0<List<InputEvent<?>>, GlfwInputProvider> PollEvents = new Functions.F0<>(
			"PollEvents") {
		@Override
		protected List<InputEvent<?>> run(GlfwInputProvider inputProvider) {
			List<InputEvent<?>> inputEvents = new LinkedList<>();
			inputProvider.eventList(inputEvents);
			GLFW.glfwPollEvents();
			inputProvider.eventList(null);
			processGamepadEvents(inputEvents);
			return inputEvents;
		}
	};

	private static int getModifiers(int mods) {
		int modifiers = 0;
		if ((mods & GLFW.GLFW_MOD_SHIFT) != 0) {
			modifiers |= 1 << Keyboard.Modifier.SHIFT.ordinal();
		}
		if ((mods & GLFW.GLFW_MOD_CONTROL) != 0) {
			modifiers |= 1 << Keyboard.Modifier.CONTROL.ordinal();
		}
		if ((mods & GLFW.GLFW_MOD_ALT) != 0) {
			modifiers |= 1 << Keyboard.Modifier.ALT.ordinal();
		}
		if ((mods & GLFW.GLFW_MOD_SUPER) != 0) {
			modifiers |= 1 << Keyboard.Modifier.SUPER.ordinal();
		}
		if ((mods & GLFW.GLFW_MOD_CAPS_LOCK) != 0) {
			modifiers |= 1 << Keyboard.Modifier.CAPS_LOCK.ordinal();
		}
		if ((mods & GLFW.GLFW_MOD_NUM_LOCK) != 0) {
			modifiers |= 1 << Keyboard.Modifier.NUM_LOCK.ordinal();
		}
		return modifiers;
	}

	public void keyCallback(GlfwContext window, int keycode, int scancode, int action, int mods) {
		log.trace("Key Callback: window={}, keycode={}, scancode={}, action={}, mods={}", window, keycode, scancode,
				action, mods);
		if (eventList != null) {
			eventList.add(new Keyboard.KeyEvent(window, defaultKeyboard, GlfwConvert.convertKey(scancode, keycode),
					action == GLFW.GLFW_PRESS, getModifiers(mods)));
		}
	}

	public void mouseButtonCallback(GlfwContext window, int button, int action, int mods) {
		log.trace("Mouse Button Callback: window={}, button={}, action={}, mods={}", window, button, action, mods);
		if (eventList != null) {
			eventList.add(new Mouse.ButtonEvent(window, defaultMouse, GlfwConvert.convertMouseButton(button),
					action == GLFW.GLFW_PRESS, getModifiers(mods)));
		}
	}

	public void charCallback(GlfwContext window, int codepoint) {
		log.trace("Char Callback: window={}, codepoint={}", window, codepoint);
		// eventList.add(new InputEvent.Keyboard.Text(getContext(window), (char)
		// codepoint));
	}
	private org.joml.Vector2ic lastPosition;
	public void cursorPosCallback(GlfwContext window, double xpos, double ypos) {
		log.trace("Cursor Pos Callback: window={}, xpos={}, ypos={}", window, xpos, ypos);
		Vector2ic position = new Vector2i((int) xpos, (int) ypos);
		if (lastPosition == null) {
			lastPosition = position;
		}
		if (eventList != null) {
			switch (window.option(GraphicsContext.Option.Cursor)) {
				case Normal, Hidden -> {
					eventList.add(new Mouse.PositionEvent(window, defaultMouse, new Vector2i((int) xpos, (int) ypos)));
				}
			}
			eventList.add(new Mouse.MotionEvent(window, defaultMouse, lastPosition.sub(position, new Vector2i())));
			eventList.add(new Mouse.AxisEvent(window, defaultMouse, Mouse.MouseAxis.X, (int) xpos - lastPosition.x()));
			eventList.add(new Mouse.AxisEvent(window, defaultMouse, Mouse.MouseAxis.Y, (int) ypos - lastPosition.y()));
		}
		lastPosition = position;
		// eventList.add(
		// new InputEvent.Mouse.Cursor(getContext(window), defaultMouse, new
		// Vector2i((int) xpos, (int) ypos)));
	}

	public void cursorEnterCallback(GlfwContext window, boolean entered) {
		log.trace("Cursor Enter Callback: window={}, entered={}", window, entered);
		// todo
	}

	public void scrollCallback(GlfwContext window, double xoffset, double yoffset) {
		log.trace("Scroll Callback: window={}, xoffset={}, yoffset={}", window, xoffset, yoffset);
		eventList.add(new Mouse.AxisEvent(window, defaultMouse, Mouse.MouseAxis.WHEEL, (int) yoffset));
		/*
		 * InputEvent.Mouse.Wheel.WheelAxis axis = yoffset != 0 ?
		 * InputEvent.Mouse.Wheel.WheelAxis.Y : InputEvent.Mouse.Wheel.WheelAxis.X;
		 * eventList.add(new InputEvent.Mouse.Wheel(getContext(window), defaultMouse,
		 * axis, (int) yoffset));
		 */
	}

	public void frameBufferSizeCallback(GlfwContext window, int width, int height) {
		log.trace("Frame Buffer Size Callback: window={}, width={}, height={}", window, width, height);
		window.properties().property(GraphicsContext.Properties.FrameBufferSize, new Vector2i(width, height));
		/*
		 * GlfwContext context = getContext(window); Vector2i frameBufferSize = new
		 * Vector2i(width, height); ContextProperties.FrameBufferSize.set( context,
		 * frameBufferSize, timer.time().frameId()); eventList.add(new
		 * InputEvent.Window.FramebufferSize(context, frameBufferSize));
		 */
	}

	public void windowSizeCallback(GlfwContext window, int width, int height) {
		log.trace("Size Callback: window={}, width={}, height={}", window, width, height);
		window.properties().property(GraphicsContext.Properties.Size, new Vector2i(width, height));
		/*
		 * GlfwContext context = getContext(window); Vector2i size = new Vector2i(width,
		 * height); ContextProperties.Size.set(context, size, timer.time().frameId());
		 * eventList.add(new InputEvent.Window.Size(context, size));
		 */
	}

	public void windowCloseCallback(GlfwContext glfwContext) {
		log.trace("Window Close Callback: window={}", glfwContext);
		glfwContext.properties().property(GraphicsContext.Properties.Close, true);
		/*
		 * GlfwContext context = getContext(l); eventList.add(new
		 * InputEvent.Window.Close(context));
		 */
	}

	public void windowFocusCallback(GlfwContext glfwContext, boolean b) {
		log.trace("Window Focus Callback: window={}, focused={}", glfwContext, b);
		glfwContext.properties().property(GraphicsContext.Properties.Focus, b);
		/*
		 * GlfwContext context = getContext(l); ContextProperties.Focus.set(context, b,
		 * timer.time().frameId()); eventList.add(new InputEvent.Window.Focus(context,
		 * b));
		 */
	}

	public void windowMaximizeCallback(GlfwContext window, boolean b) {
		log.trace("Window Maximize Callback: window={}, maximized={}", window, b);
		window.properties().property(GraphicsContext.Properties.Maximized, b);
		/*
		 * GlfwContext context = getContext(l); ContextProperties.Maximized.set(context,
		 * b, timer.time().frameId()); eventList.add(new
		 * InputEvent.Window.Maximized(context, b));
		 */
	}

	public void windowPosCallback(GlfwContext window, int width, int height) {
		log.trace("Window Pos Callback: window={}, x={}, y={}", window, width, height);
		window.properties().property(GraphicsContext.Properties.Position, new Vector2i(width, height));
		/*
		 * GlfwContext context = getContext(l); Vector2i position = new Vector2i(i, i1);
		 * ContextProperties.Position.set(context, position, timer.time().frameId());
		 * eventList.add(new InputEvent.Window.Position(context, position));
		 */
	}

	private static void processGamepadEvents(List<InputEvent<?>> gamepadEvents) {
		for (GlfwGamepad gamepad : gamepads) {
			gamepad.update(gamepadEvents);
		}
	}

	private static class GlfwGamepad extends Gamepad {
		public GlfwGamepad(int index) {
			super(index, GlfwInputProvider.class);
		}

		void update(List<InputEvent<?>> gamepadEvents) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				GLFWGamepadState state = GLFWGamepadState.malloc(stack);
				if (GLFW.glfwGetGamepadState(id(), state)) {

					if (!connected()) {
						String gamepadName = GLFW.glfwGetGamepadName(id());
						name(gamepadName);
						log.trace("Gamepad {} connected: {}", id(), gamepadName);
						gamepadEvents.add(new Gamepad.ConnectedEvent(null, this, true));
					}
					for (int i = 0; i < GLFW.GLFW_GAMEPAD_BUTTON_LAST; i++) {
						Button button = GlfwConvert.convertGampadButton(i);
						boolean buttonState = state.buttons(i) == GLFW.GLFW_PRESS;
						if (button(button) != buttonState) {
							log.trace("Gamepad {} button {} state changed: {}", id(), button, buttonState);
							gamepadEvents.add(new Gamepad.ButtonEvent(null, this, button, buttonState));
						}
					}
					for (int i = 0; i < GLFW.GLFW_GAMEPAD_AXIS_LAST; i++) {
						Axis axis = GlfwConvert.convertGampadAxis(i);
						float axisValue = state.axes(i);
						if (axis(axis) != axisValue) {
							log.trace("Gamepad {} axis {} value changed: {}", id(), axis, axisValue);
							gamepadEvents.add(new Gamepad.AxisEvent(null, this, axis, axisValue));
						}
					}
				} else {

					if (connected()) {
						log.trace("Gamepad {} disconnected", id());
						gamepadEvents.add(new ConnectedEvent(null, this, false));
					}
				}
			}
		}
	}
}
