package mylie.lwjgl3.opengl.api;

import lombok.extern.slf4j.Slf4j;
import mylie.graphics.NativeData;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.opengl.api.GlRenderTarget;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@Slf4j
public class Lwjgl3GlRenderTarget implements GlRenderTarget {

    @Override
    public boolean bindRenderTarget(NativeData.Handle target, RenderTarget.BindingMode bindingMode) {
        boolean rebind = GlRenderTarget.super.bindRenderTarget(target, bindingMode);
        if (rebind) {
            log.trace("RenderTarget<{}>.bind({})", target.handle(), bindingMode);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, target.handle());
        }
        return rebind;
    }

    @Override
    public boolean createRenderTarget(NativeData.Handle target) {
        target.handle(GL30.glGenFramebuffers());
        log.trace("RenderTarget<{}>.create()", target.handle());
        return true;
    }

    @Override
    public boolean deleteRenderTarget(NativeData.Handle target) {
        log.trace("RenderTarget<{}>.delete()", target.handle());
        GL30.glDeleteFramebuffers(target.handle());
        target.handle(-1);
        return true;
    }

    @Override
    public void clearOperation(int bitmask) {
        log.trace("ClearOperation({})", bitmask);
        GL30.glClear(bitmask);
    }

    @Override
    public boolean setClearColor(Vector4fc color) {
        boolean setColor = GlRenderTarget.super.setClearColor(color);
        if (setColor) {
            log.trace("ClearColor({})", color);
            GL11.glClearColor(color.x(), color.y(), color.z(), color.w());
        }
        return setColor;
    }
}
