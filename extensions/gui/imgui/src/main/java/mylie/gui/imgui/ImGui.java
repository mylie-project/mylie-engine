package mylie.gui.imgui;

import imgui.ImDrawData;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.ImPlotContext;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.internal.ImGuiContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.application.ApplicationModule;
import mylie.async.Async;
import mylie.async.Functions;
import mylie.component.AppComponentParallel;
import mylie.component.Lifecycle;
import mylie.core.Timer;
import mylie.graphics.GraphicsContext;
import mylie.graphics.GraphicsModule;
import mylie.input.InputEvent;
import mylie.input.InputManager;
import mylie.input.RawInputListener;
import mylie.input.devices.Keyboard;
import mylie.input.devices.Mouse;
import mylie.util.versioned.Versioned;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
@Slf4j
public class ImGui extends AppComponentParallel implements RawInputListener, Lifecycle.AddRemove,Lifecycle.Update,Lifecycle.InitDestroy {
    private final Map<GraphicsContext,Context> contexts=new ConcurrentHashMap<>();
    @Override
    public void onAdd() {
        runAfter(ApplicationModule.class);
        runBefore(GraphicsModule.class);
        component(InputManager.class).addInputListener(this);
    }

    @Override
    public void onRemove() {
        component(InputManager.class).removeInputListener(this);
    }

    @Override
    public void onUpdate(Timer.Time time) {
        for (Context context : contexts.values()) {
            Async.async(context.context.executionMode(), time.version(), Render,time, context);
        }
    }

    @Override
    public void onEvent(InputEvent<?> event) {
        if(event.context()==null){
            for (Context value : contexts.values()) {
                value.inputEvents().add(event);
            }
            return;
        }
        Context context = contexts.get(event.context());
        if(context!=null){
            context.inputEvents().add(event);
        }
    }



    @Override
    public void onInit() {

    }

    @Override
    public void onDestroy() {
        for (Context value : contexts.values()) {
            value.shutdown();
        }
    }

    public static final Functions.F1<Async.Void, Timer.Time, Context> Render = new Functions.F1<>("Render") {
        @Override
        protected Async.Void run(Timer.Time time, Context gui) {
            gui.render(time);
            return Async.VOID;
        }
    };

    public void component(ImGuiComponent component, GraphicsContext context) {
        Context imguiContext = contexts.computeIfAbsent(context, Context::new);
        imguiContext.components.add(component);
    }


    @Getter
    public final static class Context{
        private final GraphicsContext context;
        private final Queue<InputEvent<?>> inputEvents=new LinkedList<>();
        private final Versioned.Reference<Vector2ic> size;
        private final List<ImGuiComponent> components=new CopyOnWriteArrayList<>();
        boolean initialized=false;
        ImGuiContext imGuiContext;
        ImPlotContext imPlotContext;
        ImGuiImplGl3 imGuiImplGl3;
        public Context(GraphicsContext context) {
            this.context = context;
            this.size=context.propertyReference(GraphicsContext.Properties.FrameBufferSize);
        }

        void init(){
            imGuiContext= imgui.ImGui.createContext();
            imgui.ImGui.setCurrentContext(imGuiContext);
            imPlotContext= ImPlot.createContext();
            ImPlot.setCurrentContext(imPlotContext);
            imGuiImplGl3=new ImGuiImplGl3();
            imGuiImplGl3().init("#version 460");
        }



        public void render(Timer.Time time) {
            if(!initialized){
                initialized=true;
                init();
            }
            Vector2ic vector2ic = this.size.value();
            imgui.ImGui.getIO().setDisplaySize(vector2ic.x(), vector2ic.y());
            while (!inputEvents.isEmpty()) {
                processInputEvent(inputEvents.poll());
            }
            imGuiImplGl3().newFrame();
            imgui.ImGui.newFrame();
            for (ImGuiComponent component : components) {
                component.renderImGui(time);
            }

            imgui.ImGui.endFrame();
            imgui.ImGui.render();
            ImDrawData drawData = imgui.ImGui.getDrawData();
            imGuiImplGl3().renderDrawData(drawData);
        }

        private void processInputEvent(InputEvent<?> event) {
            imgui.ImGui.setCurrentContext(imGuiContext);
            if (event instanceof Mouse.PositionEvent cursorMotionEvent) {
                Vector2ic position = cursorMotionEvent.value();
                imgui.ImGui.getIO().addMousePosEvent(position.x(), position.y());
            }
            if (event instanceof Mouse.ButtonEvent buttonEvent) {
                int buttonId = ImGuiUtil.convertMouseButton(buttonEvent.button());
                if (buttonId != -1) {
                    imgui.ImGui.getIO().addMouseButtonEvent(buttonId, buttonEvent.value());
                }
            }
            if (event instanceof Mouse.AxisEvent wheelEvent) {
                if (wheelEvent.axis() == Mouse.MouseAxis.WHEEL) {
                    imgui.ImGui.getIO().addMouseWheelEvent(0, wheelEvent.value());
                }
            }
            //if (event instanceof Keyboard.Text textEvent) {
            //    imgui.ImGui.getIO().addInputCharacter(textEvent.text());
            //}
            if (event instanceof Keyboard.KeyEvent keyEvent) {
                imgui.ImGui.getIO()
                        .addKeyEvent(
                                ImGuiUtil.toImgui(keyEvent.key()), keyEvent.value());
            }
        }

        private static final Functions.F0<Async.Void, Context> ShutdownContext = new Functions.F0<>("ShutdownContext") {
            @Override
            protected Async.Void run(Context context) {
                context.imGuiImplGl3.destroyDeviceObjects();
                context.cleanup();
                return Async.VOID;
            }
        };

        void cleanup(){
            imGuiImplGl3().shutdown();
            imgui.ImGui.destroyContext();
        }

        public void shutdown() {
            Async.async(context.executionMode(),Integer.MAX_VALUE,ShutdownContext,this).result();
        }
    }
}
