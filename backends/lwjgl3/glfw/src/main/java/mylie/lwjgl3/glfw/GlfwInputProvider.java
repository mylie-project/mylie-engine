package mylie.lwjgl3.glfw;

import java.util.LinkedList;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.core.Engine;
import mylie.graphics.GraphicsContext;
import mylie.input.InputEvent;
import mylie.input.InputProvider;
import mylie.math.Vector2i;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class GlfwInputProvider implements InputProvider {
	private final ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Engine.Target,
			Caches.OneFrame);

	@Setter
	private List<InputEvent> eventList = null;

	@Override
	public Result<List<InputEvent>> inputEvents() {
		return Async.async(executionMode, -1, PollEvents, this);
	}

	private static final Functions.F0<List<InputEvent>, GlfwInputProvider> PollEvents = new Functions.F0<>(
			"PollEvents") {
		@Override
		protected List<InputEvent> run(GlfwInputProvider inputProvider) {
			List<InputEvent> inputEvents = new LinkedList<>();
			inputProvider.eventList(inputEvents);
			GLFW.glfwPollEvents();
			inputProvider.eventList(null);
			return inputEvents;
		}
	};

	public void keyCallback(GlfwContext window, int keycode, int scancode, int action, int mods) {
		log.trace("Key Callback: window={}, keycode={}, scancode={}, action={}, mods={}", window, keycode, scancode,
				action, mods);
		/*
		 * if (eventList == null) { log.warn("Event list is null"); return; } Input.Key
		 * key = DataTypes.convertKeyScanCode(keycode, scancode);
		 * InputEvent.Keyboard.Key.Type engineType = switch (action) { case
		 * GLFW.GLFW_PRESS -> InputEvent.Keyboard.Key.Type.PRESSED; case
		 * GLFW.GLFW_RELEASE -> InputEvent.Keyboard.Key.Type.RELEASED; case
		 * GLFW.GLFW_REPEAT -> InputEvent.Keyboard.Key.Type.LONG_PRESSED; default ->
		 * throw new IllegalStateException("Unexpected value: " + action); }; int
		 * modifiers = getModifiers(mods); this.mods = modifiers; eventList.add(new
		 * InputEvent.Keyboard.Key(getContext(window), defaultKeyboard, key, engineType,
		 * modifiers));
		 */
	}

	private static int getModifiers(int mods) {
		/*
		 * int modifiers = 0; if ((mods & GLFW.GLFW_MOD_SHIFT) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.SHIFT.ordinal(); } if ((mods &
		 * GLFW.GLFW_MOD_CONTROL) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.CONTROL.ordinal(); } if ((mods &
		 * GLFW.GLFW_MOD_ALT) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.ALT.ordinal(); } if ((mods &
		 * GLFW.GLFW_MOD_SUPER) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.SUPER.ordinal(); } if ((mods &
		 * GLFW.GLFW_MOD_CAPS_LOCK) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.CAPS_LOCK.ordinal(); } if ((mods &
		 * GLFW.GLFW_MOD_NUM_LOCK) != 0) { modifiers |= 1 <<
		 * InputEvent.Keyboard.Key.Modifier.NUM_LOCK.ordinal(); } return modifiers;
		 */
		return 0;
	}

	public void mouseButtonCallback(GlfwContext window, int button, int action, int mods) {
		log.trace("Mouse Button Callback: window={}, button={}, action={}, mods={}", window, button, action, mods);
		/*
		 * Input.MouseButton mouseButton = DataTypes.convertMouseButton(button);
		 * InputEvent.Mouse.Button.Type engineType = switch (action) { case
		 * GLFW.GLFW_PRESS -> InputEvent.Mouse.Button.Type.PRESSED; case
		 * GLFW.GLFW_RELEASE -> InputEvent.Mouse.Button.Type.RELEASED; default -> throw
		 * new IllegalStateException("Unexpected value: " + action); };
		 * eventList.add(new InputEvent.Mouse.Button(getContext(window), defaultMouse,
		 * mouseButton, engineType, mods));
		 */
	}

	public void charCallback(GlfwContext window, int codepoint) {
		log.trace("Char Callback: window={}, codepoint={}", window, codepoint);
		// eventList.add(new InputEvent.Keyboard.Text(getContext(window), (char)
		// codepoint));
	}

	public void cursorPosCallback(GlfwContext window, double xpos, double ypos) {
		log.trace("Cursor Pos Callback: window={}, xpos={}, ypos={}", window, xpos, ypos);
		// eventList.add(
		// new InputEvent.Mouse.Cursor(getContext(window), defaultMouse, new
		// Vector2i((int) xpos, (int) ypos)));
	}

	public void cursorEnterCallback(GlfwContext window, boolean entered) {
		log.trace("Cursor Enter Callback: window={}, entered={}", window, entered);
	}

	public void scrollCallback(GlfwContext window, double xoffset, double yoffset) {
		log.trace("Scroll Callback: window={}, xoffset={}, yoffset={}", window, xoffset, yoffset);
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
}
