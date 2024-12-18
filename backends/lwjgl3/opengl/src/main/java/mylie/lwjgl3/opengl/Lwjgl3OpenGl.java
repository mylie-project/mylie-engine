package mylie.lwjgl3.opengl;

import lombok.Getter;
import mylie.component.ComponentManager;
import mylie.graphics.opengl.OpenGl;
import mylie.lwjgl3.glfw.GlfwContextProvider;

@Getter
public class Lwjgl3OpenGl extends OpenGl {
	private final Lwjgl3OpenGlSettings lwjgl3OpenGlSettings;
	private GlfwContextProvider contextProvider;

	public Lwjgl3OpenGl(Lwjgl3OpenGlSettings lwjgl3OpenGlSettings) {
		this.lwjgl3OpenGlSettings = lwjgl3OpenGlSettings;
	}

	@Override
	protected void onInitialize(ComponentManager componentManager) {
		contextProvider = new Lwjgl3OpenGlContextProvider();
		contextProvider.onInitialize(componentManager);
	}
}
