package mylie.graphics.opengl.managers;


import mylie.graphics.GraphicsContext;
import mylie.graphics.NativeData;
import mylie.graphics.RenderTask;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.managers.RenderTargetManager;
import mylie.graphics.opengl.api.GlRenderTarget;

public class GlRenderTargetManager implements RenderTargetManager {
    GlRenderTarget glRenderTargetBase;
    final NativeData.NonSharedData<RenderTarget> renderTargetHandles;

    public GlRenderTargetManager() {
        renderTargetHandles = new NativeData.NonSharedData<>();
    }

    @Override
    public boolean isSupported(GraphicsContext context) {
        glRenderTargetBase = api(GlRenderTarget.class,context);
        return glRenderTargetBase != null;
    }


    @Override
    public void clearRenderTarget(
            RenderTask renderTask, RenderTarget renderTarget, RenderTarget.ClearOperation clearOperation) {
        renderTask.subTask(() -> {
            clearRenderTargetInternal(renderTask, renderTarget, clearOperation);
        });
    }

    @Override
    public void bindRenderTarget(
            RenderTask renderTask, RenderTarget renderTarget, RenderTarget.BindingMode bindingMode) {
        renderTask.subTask(() -> {
            bindRenderTargetInternal(renderTask, renderTarget, bindingMode);
        });
    }

    void clearRenderTargetInternal(
            RenderTask renderTask, RenderTarget renderTarget, RenderTarget.ClearOperation clearOperation) {
        int bitmask = 0;
        for (RenderTarget.ClearOperation.Type type : clearOperation.type()) {
            if (type == RenderTarget.ClearOperation.Type.Color) {
                bitmask |= 16384;
            }
            if (type == RenderTarget.ClearOperation.Type.Depth) {
                bitmask |= 256;
            }
            if (type == RenderTarget.ClearOperation.Type.Stencil) {
                bitmask |= 1024;
            }
        }
        bindRenderTargetInternal(renderTask, renderTarget, RenderTarget.BindingMode.Write);
        glRenderTargetBase.setClearColor(clearOperation.color());
        glRenderTargetBase.clearOperation(bitmask);
    }

    void bindRenderTargetInternal(
            RenderTask renderTask, RenderTarget renderTarget, RenderTarget.BindingMode bindingMode) {
        NativeData.NonSharedData.Handle renderTargetHandle = renderTargetHandles.get(renderTask.context(), renderTarget);
        if (renderTargetHandle == null) {
            renderTargetHandle = new NativeData.NonSharedData.Handle();
            renderTargetHandles.put(renderTask.context(), renderTarget, renderTargetHandle);
            if (renderTarget != renderTask.context().renderTarget()) {
                glRenderTargetBase.createRenderTarget(renderTargetHandle);
            } else {
                renderTargetHandle.handle(0);
            }
        }
        glRenderTargetBase.bindRenderTarget(renderTargetHandle, bindingMode);
    }
}
