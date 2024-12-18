package mylie.lwjgl3.glfw;

import java.util.List;
import java.util.Objects;
import mylie.graphics.Display;
import mylie.math.Vector2i;
import mylie.math.Vector4i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class GlfwUtil {

	public static List<Display> getDisplays() {
		List<Display> displays = new java.util.ArrayList<>();
		PointerBuffer pointerBuffer = GLFW.glfwGetMonitors();
		for (int i = 0; i < Objects.requireNonNull(pointerBuffer).capacity(); i++) {
			long handle = pointerBuffer.get(i);
			displays.add(getDisplay(handle));
		}
		return displays;
	}

	public static Display getDisplay(long handle) {
		Display.VideoMode defaultVideoMode = convert(Objects.requireNonNull(GLFW.glfwGetVideoMode(handle)));
		Display display = new GlfwDisplay(handle, GLFW.glfwGetPrimaryMonitor() == handle, defaultVideoMode);
		setVideoModes(handle, display.videoModes());
		return display;
	}

	private static void setVideoModes(long handle, List<Display.VideoMode> videoModes) {
		GLFWVidMode.Buffer glfwVidModes = GLFW.glfwGetVideoModes(handle);
		assert glfwVidModes != null;
		for (GLFWVidMode glfwVidMode : glfwVidModes) {
			videoModes.add(convert(glfwVidMode));
		}
	}

	private static Display.VideoMode convert(org.lwjgl.glfw.GLFWVidMode glfwVidMode) {
		Vector2i resolution = new Vector2i(glfwVidMode.width(), glfwVidMode.height());
		int refreshRate = glfwVidMode.refreshRate();
		Vector4i bits = new Vector4i(glfwVidMode.redBits(), glfwVidMode.greenBits(), glfwVidMode.blueBits(), 0);
		return new Display.VideoMode(resolution, refreshRate, bits);
	}
}
