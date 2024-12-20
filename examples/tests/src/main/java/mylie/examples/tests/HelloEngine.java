package mylie.examples.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.application.BaseApplication;
import mylie.async.SchedulerSettings;
import mylie.core.*;
import mylie.examples.utils.IconFactory;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.graphics.GraphicsManager;
import mylie.gui.imgui.ImGui;
import mylie.input.InputEvent;
import mylie.input.InputManager;
import mylie.input.RawInputListener;
import mylie.input.devices.Gamepad;
import mylie.input.devices.Keyboard;
import mylie.input.xinput.XinputProvider;
import mylie.lwjgl3.opengl.Lwjgl3OpenGlSettings;
import mylie.platform.desktop.Desktop;
import mylie.util.versioned.Versioned;
import org.joml.Vector2i;

@Slf4j
public class HelloEngine extends BaseApplication implements RawInputListener {
	GraphicsContext.VideoMode windowed = new GraphicsContext.VideoMode.Windowed(null, new Vector2i(800, 600),
			GraphicsContext.VideoMode.Windowed.Centered);
	GraphicsContext.VideoMode fullscreen = new GraphicsContext.VideoMode.Fullscreen(null, null);
	GraphicsContext context;
	Versioned.Reference<Boolean> escapeKey;
	public static void main(String[] args) {
		EngineConfiguration configuration = Platform.initialize(new Desktop());
		configuration.option(Engine.Options.Application, new HelloEngine());
		configuration.option(Engine.Options.GraphicsApi, new Lwjgl3OpenGlSettings());
		// configuration.option(Engine.Options.Scheduler, SchedulerSettings.SingleThreaded);
		Engine.ShutdownReason shutdownReason = Engine.start(configuration);
		//System.out.println(shutdownReason);
	}

	@Override
	public void onInitialize() {
		log.trace("HelloEngine initialized.");
		GraphicsContextConfiguration gcc = new GraphicsContextConfiguration();
		gcc.option(GraphicsContext.Option.AlwaysOnTop, true);
		gcc.option(GraphicsContext.Option.Title, "Hello Engine");
		gcc.option(GraphicsContext.Option.VideoMode, windowed);
		gcc.option(GraphicsContext.Option.VSync, false);
		gcc.option(GraphicsContext.Option.Icons, IconFactory.getDefaultIcons());
		gcc.option(GraphicsContext.Option.Cursor, GraphicsContext.Option.CursorMode.Normal);
		context = component(GraphicsManager.class).createContext(gcc, true);
		component(InputManager.class).addInputListener(this);
		escapeKey = component(InputManager.class).keyboard().keyReference(Keyboard.Key.ESCAPE);
		component(new XinputProvider());
		ImGui imGui = new ImGui();
		component(imGui);
		imGui.addContext(context);
	}

	@Override
	public void onUpdate(Timer.Time time) {
		log.trace("HelloEngine updated.");
		if (escapeKey.value()) {
			component(EngineManager.class).shutdown(new Engine.ShutdownReason.User("Escape pressed"));
		}
	}

	@Override
	public void onShutdown() {
		log.trace("HelloEngine shutdown.");
	}

	@Override
	public void onEvent(InputEvent<?> event) {
		if (event instanceof Gamepad.GamepadEvent<?> gamepadEvent) {
			log.info("Gamepad event: {}", gamepadEvent);
			/*
			 * if(gamepadEvent.device() instanceof XinputProvider.XInputGamepad
			 * xInputGamepad){ xInputGamepad.rumble(0,1); xInputGamepad.rumble(1,1); }
			 */
		}
		// log.info("Input event: {}", event);
	}
}
