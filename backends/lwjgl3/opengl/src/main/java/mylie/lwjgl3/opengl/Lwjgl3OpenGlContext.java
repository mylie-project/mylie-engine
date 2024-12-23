package mylie.lwjgl3.opengl;

import static org.lwjgl.opengl.GL43C.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43C.GL_DEBUG_SEVERITY_HIGH;

import lombok.extern.slf4j.Slf4j;
import mylie.async.Async;
import mylie.async.Functions;
import mylie.core.components.Scheduler;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.lwjgl3.glfw.GlfwContext;
import mylie.lwjgl3.opengl.api.Lwjgl3GlGet;
import org.lwjgl.opengl.*;

@Slf4j
public class Lwjgl3OpenGlContext extends GlfwContext implements GLDebugMessageCallbackI {
	GLCapabilities glCapabilities;

	public Lwjgl3OpenGlContext(GraphicsContextConfiguration configuration, GraphicsContext primaryContext,
			Scheduler scheduler) {
		super(configuration, primaryContext, scheduler);
		apiFeatures().add(new Lwjgl3GlGet());
	}

	public void createGlCapabilities() {
		Async.async(executionMode(), -1, CreateGlCapabilities, this);
	}

	private static final Functions.F0<Boolean, Lwjgl3OpenGlContext> CreateGlCapabilities = new Functions.F0<>(
			"CreateGlCapabilities") {
		@Override
		protected Boolean run(Lwjgl3OpenGlContext o) {
			o.glCapabilities = GL.createCapabilities();
			setupErrorCallback(o);
			return true;
		}
	};

	private static void setupErrorCallback(Lwjgl3OpenGlContext context) {
		GLCapabilities caps = context.glCapabilities;
		GL43.glDebugMessageCallback(context, 0);
		GL11.glEnable(GL_DEBUG_OUTPUT);
	}

	@Override
	public void invoke(int source, int type, int id, int severity, int lenght, long message, long userparam) {
		if (severity == GL_DEBUG_SEVERITY_HIGH) {
			log.error("GL Error: {}", GLDebugMessageCallback.getMessage(lenght, message));
		}
	}
}
