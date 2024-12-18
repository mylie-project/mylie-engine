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
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class GlfwInputProvider implements InputProvider {
	private final ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Engine.Target,
			Caches.OneFrame);
	private final Keyboard defaultKeyboard;
	private final Mouse defaultMouse;

	@Setter
	private List<InputEvent> eventList = null;

	public GlfwInputProvider(InputManager inputManager) {
		defaultKeyboard = inputManager.keyboard();
		defaultMouse = inputManager.mouse();
	}

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
		if (eventList != null) {
			eventList.add(new Keyboard.KeyEvent(window, defaultKeyboard, GlfwConvert.convertKey(scancode, keycode),
					action == GLFW.GLFW_PRESS));
		}
	}

	public void mouseButtonCallback(GlfwContext window, int button, int action, int mods) {
		log.trace("Mouse Button Callback: window={}, button={}, action={}, mods={}", window, button, action, mods);
		if (eventList != null) {
			eventList.add(new Mouse.ButtonEvent(window, defaultMouse, GlfwConvert.convertMouseButton(button),
					action == GLFW.GLFW_PRESS));
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
		if(lastPosition == null) {
			lastPosition = position;
		}
		if (eventList != null) {
			switch (window.option(GraphicsContext.Option.Cursor)) {
				case Normal, Hidden -> {
					eventList.add(new Mouse.PositionEvent(window, defaultMouse, new Vector2i((int) xpos, (int) ypos)));
				}
			}
			eventList.add(new Mouse.MotionEvent(window, defaultMouse, lastPosition.sub(position, new Vector2i())));
			eventList.add(new Mouse.AxisEvent(window,defaultMouse, Mouse.MouseAxis.X, (int) xpos - lastPosition.x()));
			eventList.add(new Mouse.AxisEvent(window,defaultMouse, Mouse.MouseAxis.Y, (int) ypos - lastPosition.y()));
		}
		lastPosition = position;
		// eventList.add(
		// new InputEvent.Mouse.Cursor(getContext(window), defaultMouse, new
		// Vector2i((int) xpos, (int) ypos)));
	}

	public void cursorEnterCallback(GlfwContext window, boolean entered) {
		log.trace("Cursor Enter Callback: window={}, entered={}", window, entered);
		//todo
	}

	public void scrollCallback(GlfwContext window, double xoffset, double yoffset) {
		log.trace("Scroll Callback: window={}, xoffset={}, yoffset={}", window, xoffset, yoffset);
		eventList.add(new Mouse.AxisEvent(window,defaultMouse, Mouse.MouseAxis.WHEEL, (int) yoffset));
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
