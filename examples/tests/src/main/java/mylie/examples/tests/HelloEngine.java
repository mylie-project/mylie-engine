package mylie.examples.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.application.BaseApplication;
import mylie.core.*;
import mylie.examples.utils.IconFactory;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.graphics.GraphicsManager;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.VertexDataLayouts;
import mylie.graphics.geometry.meshes.Quad;
import mylie.graphics.managers.RenderManager;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.managers.RenderTargetManager;
import mylie.gui.imgui.ControlPanel;
import mylie.gui.imgui.ImGui;
import mylie.input.InputEvent;
import mylie.input.InputManager;
import mylie.input.RawInputListener;
import mylie.input.devices.Keyboard;
import mylie.input.xinput.XinputProvider;
import mylie.lwjgl3.opengl.Lwjgl3OpenGlSettings;
import mylie.platform.desktop.Desktop;
import mylie.util.versioned.Versioned;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.opengl.GL20;

@Slf4j
public class HelloEngine extends BaseApplication implements RawInputListener {
	GraphicsContext.VideoMode windowed = new GraphicsContext.VideoMode.Windowed(null, new Vector2i(800, 600),
			GraphicsContext.VideoMode.Windowed.Centered);
	GraphicsContext.VideoMode fullscreen = new GraphicsContext.VideoMode.Fullscreen(null, null);
	GraphicsContext.VideoMode[] videoModes = {windowed, fullscreen};
	int currentVideoMode = 0;
	GraphicsContext context;
	GraphicsContext context2;
	Versioned.Reference<Boolean> escapeKey;
	int programId, programId2;
	private Quad cube = new Quad(VertexDataLayouts.Unshaded);

	private String vertexShader = """
			         \
			#version 460

			layout (location=0) in vec3 vertexPosition;
			layout (location=3) in vec2 vertexTexCoord0;
			out vec2 fragTexCoord0;
			void main()
			{
				fragTexCoord0 = vertexTexCoord0;
			    gl_Position = vec4(vertexPosition, 1.0);
			}""";

	private String fragmentShader = """
			#version 460
			in vec2 fragTexCoord0;
			out vec4 fragColor;

			void main()
			{
			    fragColor = vec4(fragTexCoord0, 0.5, 1.0);
			}


			""";

	public static void main(String[] args) {
		EngineConfiguration configuration = Platform.initialize(new Desktop());
		configuration.option(Engine.Options.Application, new HelloEngine());
		configuration.option(Engine.Options.GraphicsApi, new Lwjgl3OpenGlSettings());
		// configuration.option(Engine.Options.Scheduler,
		// SchedulerSettings.SingleThreaded);
		Engine.ShutdownReason shutdownReason = Engine.start(configuration);
	}

	@Override
	public void onInitialize() {
		log.trace("HelloEngine initialized.");
		GraphicsContextConfiguration gcc = new GraphicsContextConfiguration();
		gcc.option(GraphicsContext.Option.AlwaysOnTop, true);
		gcc.option(GraphicsContext.Option.Title, "Hello Engine");
		gcc.option(GraphicsContext.Option.VideoMode, windowed);
		gcc.option(GraphicsContext.Option.VSync, true);
		gcc.option(GraphicsContext.Option.Icons, IconFactory.getDefaultIcons());
		gcc.option(GraphicsContext.Option.Cursor, GraphicsContext.Option.CursorMode.Normal);
		context = component(GraphicsManager.class).createContext(gcc, true);
		context2 = component(GraphicsManager.class).createContext(gcc, true);
		component(InputManager.class).addInputListener(this);
		escapeKey = component(InputManager.class).keyboard().keyReference(Keyboard.Key.ESCAPE);
		component(new XinputProvider());
		ImGui imGui = new ImGui();
		component(imGui);
		imGui.component(new ControlPanel(2), context);
		RenderTask renderTask = new RenderTask(context);
		renderTask.subTask(() -> {
			programId = GL20.glCreateProgram();
			int vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			int fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
			GL20.glShaderSource(vertexShaderId, vertexShader);
			GL20.glShaderSource(fragmentShaderId, fragmentShader);
			GL20.glCompileShader(vertexShaderId);
			GL20.glCompileShader(fragmentShaderId);
			GL20.glAttachShader(programId, vertexShaderId);
			GL20.glAttachShader(programId, fragmentShaderId);
			GL20.glLinkProgram(programId);
			GL20.glValidateProgram(programId);
			log.error("HelloEngine created shader program.");
		});
		renderTask.submit();

		RenderTask renderTask2 = new RenderTask(context2);
		renderTask2.subTask(() -> {
			programId2 = GL20.glCreateProgram();
			int vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			int fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
			GL20.glShaderSource(vertexShaderId, vertexShader);
			GL20.glShaderSource(fragmentShaderId, fragmentShader);
			GL20.glCompileShader(vertexShaderId);
			GL20.glCompileShader(fragmentShaderId);
			GL20.glAttachShader(programId2, vertexShaderId);
			GL20.glAttachShader(programId2, fragmentShaderId);
			GL20.glLinkProgram(programId2);
			GL20.glValidateProgram(programId2);
			log.error("HelloEngine created shader program.");
		});
		renderTask2.submit();
	}

	@Override
	public void onUpdate(Timer.Time time) {
		log.trace("HelloEngine updated.");
		if (escapeKey.value()) {
			component(EngineManager.class).shutdown(new Engine.ShutdownReason.User("Escape pressed"));
		}
		render(context, programId);
		// if(time.version()>4) {
		render(context2, programId2);
		// }
		// renderTask2.submit();
	}

	private void render(GraphicsContext theContext, int id) {
		RenderTask renderTask = new RenderTask(theContext);
		Vector2ic property = theContext.property(GraphicsContext.Properties.FrameBufferSize);

		theContext.manager(RenderTargetManager.class).clearRenderTarget(renderTask, theContext.renderTarget(),
				RenderTarget.ClearOperation.Default);
		renderTask.subTask(() -> {
			GL20.glViewport(0, 0, property.x(), property.y());
			GL20.glUseProgram(id);
		});
		theContext.manager(RenderManager.class).render(renderTask, cube);
		renderTask.submit();
	}

	@Override
	public void onShutdown() {
		log.trace("HelloEngine shutdown.");
	}

	@Override
	public void onEvent(InputEvent<?> event) {
		if (event instanceof Keyboard.KeyEvent keyEvent) {
			if (keyEvent.key() == Keyboard.Key.F11 && keyEvent.value()) {
				currentVideoMode = (currentVideoMode + 1) % videoModes.length;
				context.option(GraphicsContext.Option.VideoMode, videoModes[currentVideoMode]);
			}
			if (keyEvent.key() == Keyboard.Key.F12 && keyEvent.value()) {
				context.option(GraphicsContext.Option.VSync, !context.option(GraphicsContext.Option.VSync));
			}
		}
		// log.info("Input event: {}", event);
	}
}
