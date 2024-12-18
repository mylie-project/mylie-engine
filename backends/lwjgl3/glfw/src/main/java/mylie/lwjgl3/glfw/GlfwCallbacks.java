package mylie.lwjgl3.glfw;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.*;

@Getter
@Setter
public class GlfwCallbacks {
    final GLFWKeyCallback keyCallback;
    final GLFWCharCallback charCallback;

    final GLFWMouseButtonCallback mouseButtonCallback;
    final GLFWCursorEnterCallback cursorEnterCallback;
    final GLFWCursorPosCallback cursorPosCallback;
    final GLFWScrollCallback scrollCallback;

    final GLFWFramebufferSizeCallback framebufferSizeCallback;
    final GLFWWindowSizeCallback windowSizeCallback;
    final GLFWWindowCloseCallback windowCloseCallback;
    final GLFWWindowFocusCallback windowFocusCallback;
    final GLFWWindowMaximizeCallback windowMaximizeCallback;
    final GLFWWindowPosCallback windowPosCallback;
    private GlfwContext contexts;
    private GlfwInputProvider glfwInputProvider;

    public GlfwCallbacks(GlfwContext contexts, GlfwInputProvider inputProvider) {
        this.contexts = contexts;
        this.glfwInputProvider = inputProvider;
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                glfwInputProvider.keyCallback(contexts, key, scancode, action, mods);
            }
        };
        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                glfwInputProvider.mouseButtonCallback(contexts, button, action, mods);
            }
        };
        charCallback = new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                glfwInputProvider.charCallback(contexts, codepoint);
            }
        };
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                glfwInputProvider.cursorPosCallback(contexts, xpos, ypos);
            }
        };
        cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                glfwInputProvider.cursorEnterCallback(contexts, entered);
            }
        };
        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                glfwInputProvider.scrollCallback(contexts, xoffset, yoffset);
            }
        };
        framebufferSizeCallback = new GLFWFramebufferSizeCallback() {

            @Override
            public void invoke(long window, int width, int height) {
                glfwInputProvider.frameBufferSizeCallback(contexts, width, height);
            }
        };
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glfwInputProvider.windowSizeCallback(contexts, width, height);
            }
        };
        windowCloseCallback = new GLFWWindowCloseCallback() {

            @Override
            public void invoke(long l) {
                glfwInputProvider.windowCloseCallback(contexts);
            }
        };
        windowFocusCallback = new GLFWWindowFocusCallback() {

            @Override
            public void invoke(long l, boolean b) {
                glfwInputProvider.windowFocusCallback(contexts, b);
            }
        };
        windowMaximizeCallback = new GLFWWindowMaximizeCallback() {

            @Override
            public void invoke(long l, boolean b) {
                glfwInputProvider.windowMaximizeCallback(contexts, b);
            }
        };
        windowPosCallback = new GLFWWindowPosCallback() {

            @Override
            public void invoke(long l, int i, int i1) {
                glfwInputProvider.windowPosCallback(contexts, i, i1);
            }
        };
        GLFW.glfwSetKeyCallback(contexts.handle(), keyCallback);
        GLFW.glfwSetMouseButtonCallback(contexts.handle(), mouseButtonCallback);
        GLFW.glfwSetCharCallback(contexts.handle(), charCallback);
        GLFW.glfwSetCursorPosCallback(contexts.handle(), cursorPosCallback);
        GLFW.glfwSetCursorEnterCallback(contexts.handle(), cursorEnterCallback);
        GLFW.glfwSetScrollCallback(contexts.handle(), scrollCallback);
        GLFW.glfwSetFramebufferSizeCallback(contexts.handle(), framebufferSizeCallback);
        GLFW.glfwSetWindowSizeCallback(contexts.handle(), windowSizeCallback);
        GLFW.glfwSetWindowCloseCallback(contexts.handle(), windowCloseCallback);
        GLFW.glfwSetWindowFocusCallback(contexts.handle(), windowFocusCallback);
        GLFW.glfwSetWindowMaximizeCallback(contexts.handle(), windowMaximizeCallback);
        GLFW.glfwSetWindowPosCallback(contexts.handle(), windowPosCallback);
    }

    public void free() {
        keyCallback.free();
        mouseButtonCallback.free();
        charCallback.free();
        cursorPosCallback.free();
        cursorEnterCallback.free();
        scrollCallback.free();
        framebufferSizeCallback.free();
        windowSizeCallback.free();
        windowCloseCallback.free();
        windowFocusCallback.free();
        windowMaximizeCallback.free();
        windowPosCallback.free();
    }
}
