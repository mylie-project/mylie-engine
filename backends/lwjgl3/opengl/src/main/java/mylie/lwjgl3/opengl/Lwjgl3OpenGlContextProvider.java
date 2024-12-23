package mylie.lwjgl3.opengl;

import lombok.extern.slf4j.Slf4j;
import mylie.async.Async;
import mylie.async.Caches;
import mylie.async.ExecutionMode;
import mylie.async.Functions;
import mylie.component.ComponentManager;
import mylie.core.Engine;
import mylie.core.components.Scheduler;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import mylie.lwjgl3.glfw.GlfwContext;
import mylie.lwjgl3.glfw.GlfwContextProvider;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class Lwjgl3OpenGlContextProvider extends GlfwContextProvider {
	private final ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Engine.Target, Caches.No);
	private mylie.core.components.Scheduler scheduler;



	@Override
	public void onInitialize(ComponentManager componentManager) {
		super.onInitialize(componentManager);
		scheduler = componentManager.getComponent(Scheduler.class);
	}

	@Override
	public GraphicsContext createContext(GraphicsContextConfiguration configuration, GraphicsContext primaryContext) {
		Lwjgl3OpenGlContext lwjgl3OpenGlContext = new Lwjgl3OpenGlContext(configuration, primaryContext, scheduler);
		Async.async(executionMode, -1, CreateContext, this, lwjgl3OpenGlContext).result();
		lwjgl3OpenGlContext.makeCurrent();
		lwjgl3OpenGlContext.createGlCapabilities();
		return lwjgl3OpenGlContext;
	}

	private void setupApi(GlfwContext glfwContext) {
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		// todo: opengl profile selection.
	}

	private static final Functions.F1<Boolean, Lwjgl3OpenGlContextProvider, GlfwContext> CreateContext = new Functions.F1<>(
			"CreateContext") {
		@Override
		protected Boolean run(Lwjgl3OpenGlContextProvider o, GlfwContext glfwContext) {
			o.setupContext(glfwContext);
			o.setupApi(glfwContext);
			return o.createWindow(glfwContext);
		}
	};
}
