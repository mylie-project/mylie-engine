package mylie.graphics.opengl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.graphics.managers.RenderTarget;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.EnumMap;

@Slf4j
@Getter
@Setter
public class BindingState {
    private static final ThreadLocal<BindingState> bindingState = new ThreadLocal<>();

    private Vector4fc currentClearColor = new Vector4f(0, 0, 0, 0);
    private EnumMap<RenderTarget.BindingMode, Integer> currentRenderTarget =
            new EnumMap<>(RenderTarget.BindingMode.class);

    public static BindingState get() {
        return bindingState.get();
    }

    public static void init() {
        log.trace("Binding state initialized");
        bindingState.set(new BindingState());
    }
}