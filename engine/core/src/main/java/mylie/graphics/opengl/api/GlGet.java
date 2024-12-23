package mylie.graphics.opengl.api;

public interface GlGet extends mylie.graphics.opengl.GlApiFeature {
	int getInteger(int parameter);

	double getDouble(int parameter);

	String getString(int parameter);
}
