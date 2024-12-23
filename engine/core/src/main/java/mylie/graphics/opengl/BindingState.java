package mylie.graphics.opengl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.graphics.NativeData;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.opengl.managers.GlBufferManager;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class BindingState {
    private static final ThreadLocal<BindingState> bindingState = new ThreadLocal<>();

    private Vector4fc currentClearColor = new Vector4f(0, 0, 0, 0);
    private EnumMap<RenderTarget.BindingMode, Integer> currentRenderTarget =
            new EnumMap<>(RenderTarget.BindingMode.class);
    private int currentVao;
    private Map<GlBufferManager.Target, NativeData.Handle> bufferBindings=new HashMap<>();

    public static BindingState get() {
        return bindingState.get();
    }

    public static void init() {
        log.trace("Binding state initialized");
        bindingState.set(new BindingState());
    }
}
