package mylie.lwjgl3.opengl;

import mylie.graphics.Api;
import mylie.graphics.opengl.OpenGlSettings;

public class Lwjgl3OpenGlSettings extends OpenGlSettings {
	@Override
	protected Api get() {
		return new Lwjgl3OpenGl(this);
	}
}
