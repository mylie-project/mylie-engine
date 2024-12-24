package mylie.graphics.opengl.managers;

import mylie.graphics.GraphicsContext;
import mylie.graphics.NativeData;
import mylie.graphics.RenderTask;
import mylie.graphics.managers.RenderTarget;
import mylie.graphics.managers.RenderTargetManager;
import mylie.graphics.opengl.api.GlRenderTarget;

public class GlRenderTargetManager implements RenderTargetManager {
	GlRenderTarget glRenderTargetBase;
	final NativeData.NonSharedData<RenderTarget, RenderTargetHandle> renderTargetHandles;

	public GlRenderTargetManager() {
		renderTargetHandles = new NativeData.NonSharedData<>(RenderTargetHandle::new);
	}

	@Override
	public boolean isSupported(GraphicsContext context) {
		glRenderTargetBase = api(GlRenderTarget.class, context);
		return glRenderTargetBase != null;
	}

	@Override
	public void clearRenderTarget(RenderTask renderTask, RenderTarget renderTarget,
			RenderTarget.ClearOperation clearOperation) {
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
		final int finalMask = bitmask;
		bindRenderTarget(renderTask, renderTarget, RenderTarget.BindingMode.Write);
		renderTask.subTask(() -> {
			glRenderTargetBase.setClearColor(clearOperation.color());
			glRenderTargetBase.clearOperation(finalMask);
		});
	}

	@Override
	public void bindRenderTarget(RenderTask renderTask, RenderTarget renderTarget,
			RenderTarget.BindingMode bindingMode) {
		RenderTargetHandle renderTargetHandle = renderTargetHandles.get(renderTask.context(), renderTarget);
		if (renderTargetHandle.handle() == -1) {
			if (renderTarget != renderTask.context().renderTarget()) {
				renderTask.subTask(() -> glRenderTargetBase.createRenderTarget(renderTargetHandle));
			} else {
				renderTargetHandle.handle(0);
			}
		}
		renderTask.subTask(() -> glRenderTargetBase.bindRenderTarget(renderTargetHandle, bindingMode));
	}

	private static class RenderTargetHandle extends NativeData.NonSharedData.Handle {

	}
}
