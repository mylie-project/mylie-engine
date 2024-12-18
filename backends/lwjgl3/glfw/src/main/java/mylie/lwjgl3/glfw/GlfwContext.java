package mylie.lwjgl3.glfw;

import java.util.function.BiConsumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.core.Engine;
import mylie.core.components.Scheduler;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsContextConfiguration;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

@Slf4j
@Getter(AccessLevel.PACKAGE)
public class GlfwContext extends GraphicsContext {
    private static final ExecutionMode engine = new ExecutionMode(ExecutionMode.Mode.Async, Engine.Target, Caches.No);

    @Setter(AccessLevel.PACKAGE)
    private GlfwCallbacks callbacks;

    @Setter(AccessLevel.PACKAGE)
    private long handle;

    public GlfwContext(
            GraphicsContextConfiguration configuration, GraphicsContext primaryContext, Scheduler scheduler) {
        super(configuration, primaryContext, scheduler);
    }

    @Override
    protected GlfwContext primaryContext() {
        return (GlfwContext) super.primaryContext();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> void onOptionChanged(mylie.util.configuration.Option<GraphicsContext, T> option, T value) {
        ExecutionMode executionMode = option == Option.VSync ? executionMode() : engine;
        if (option instanceof GlfwOption<T>) {
            GlfwOption<Object> glfwOption = (GlfwOption<Object>) option;
            Async.async(executionMode, -1, ApplySettings, handle, glfwOption, value);
        }
    }

    @Override
    protected Result<Async.Void> destroy() {
        return Async.async(executionMode(), -1, ShutdownContext, this);
    }

    @Override
    public Result<Async.Void> swapBuffers() {
        return Async.async(executionMode(), -1, SwapBuffers, this);
    }

    public Result<Async.Void> makeCurrent() {
        return Async.async(executionMode(), -1, MakeCurrent, this);
    }

    public Result<Async.Void> release() {
        return Async.async(executionMode(), -1, Release, this);
    }

    @Getter(AccessLevel.PACKAGE)
    public static class GlfwOption<T> extends GraphicsContext.Option<T> {
        final int windowHint;
        final BiConsumer<Long, T> consumer;

        public GlfwOption(String name, int windowHint, BiConsumer<Long, T> consumer, T defaultValue) {
            super(name, defaultValue);
            this.windowHint = windowHint;
            this.consumer = consumer;
        }
    }

    public static final Functions.F2<Async.Void, Long, GlfwContext.GlfwOption<Object>, Object> ApplySettings =
            new Functions.F2<>("ApplySettings") {

                @Override
                protected Async.Void run(Long handle, GlfwContext.GlfwOption<Object> parameter, Object value) {
                    if (parameter.windowHint() != -1) {
                        int v = 0;
                        if (value instanceof Boolean) {
                            v = ((Boolean) value) ? 1 : 0;
                        } else if (value instanceof Integer) {
                            v = (Integer) value;
                        }
                        GLFW.glfwWindowHint(parameter.windowHint(), v);
                    } else if (parameter.consumer() != null) {
                        parameter.consumer().accept(handle, value);
                    }
                    return Async.VOID;
                }
            };

    public static final Functions.F0<Async.Void, GlfwContext> MakeCurrent = new Functions.F0<>("MakeCurrent") {

        @Override
        protected Async.Void run(GlfwContext context) {
            GLFW.glfwMakeContextCurrent(context.handle);
            Boolean vsync = context.option(Option.VSync);
            GLFW.glfwSwapInterval(vsync ? -1 : 0);
            return Async.VOID;
        }
    };

    public static final Functions.F0<Async.Void, GlfwContext> Release = new Functions.F0<>("Release") {

        @Override
        protected Async.Void run(GlfwContext context) {
            GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
            return Async.VOID;
        }
    };

    public static final Functions.F0<Async.Void, GlfwContext> SwapBuffers = new Functions.F0<>("SwapBuffers") {
        @Override
        protected Async.Void run(GlfwContext context) {
            GLFW.glfwSwapBuffers(context.handle);
            return Async.VOID;
        }
    };

    private static final Functions.F0<Async.Void, GlfwContext> ShutdownContext = new Functions.F0<>("ShutdownContext") {
        @Override
        protected Async.Void run(GlfwContext context) {
            context.callbacks().free();
            GLFW.glfwDestroyWindow(context.handle());
            return Async.VOID;
        }
    };
}
