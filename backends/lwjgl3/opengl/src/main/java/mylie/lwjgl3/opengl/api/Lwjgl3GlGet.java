package mylie.lwjgl3.opengl.api;

import mylie.graphics.opengl.api.GlGet;
import org.lwjgl.opengl.GL11;

public class Lwjgl3GlGet implements GlGet {
	@Override
	public int getInteger(int parameter) {
		return GL11.glGetInteger(parameter);
	}

	@Override
	public double getDouble(int parameter) {
		return GL11.glGetDouble(parameter);
	}

	@Override
	public String getString(int parameter) {
		return GL11.glGetString(parameter);
	}
}
