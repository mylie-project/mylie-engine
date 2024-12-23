package mylie.graphics.opengl.api;


import mylie.graphics.NativeData;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.opengl.BindingState;
import mylie.graphics.opengl.GlApiFeature;
import org.joml.Vector4fc;

public interface GlRenderTarget extends GlApiFeature {
    default boolean bindRenderTarget(NativeData.Handle target, RenderTarget.BindingMode bindingMode) {
        BindingState bindingState = BindingState.get();
        Integer i = bindingState.currentRenderTarget().get(bindingMode);
        if (i == null || i != target.handle()) {
            bindingState.currentRenderTarget().put(bindingMode, target.handle());
            if (bindingMode == RenderTarget.BindingMode.ReadWrite) {
                bindingState.currentRenderTarget().put(RenderTarget.BindingMode.Read, target.handle());
                bindingState.currentRenderTarget().put(RenderTarget.BindingMode.Write, target.handle());
            } else {
                bindingState.currentRenderTarget().put(RenderTarget.BindingMode.ReadWrite, -1);
            }
            return true;
        }
        return false;
    }

    default boolean setClearColor(Vector4fc color) {
        BindingState bindingState = BindingState.get();
        if (bindingState.currentClearColor().equals(color)) {
            bindingState.currentClearColor(color);
            return true;
        }
        return false;
    }

    boolean createRenderTarget(NativeData.Handle target);

    boolean deleteRenderTarget(NativeData.Handle target);

    void clearOperation(int bitmask);
}
