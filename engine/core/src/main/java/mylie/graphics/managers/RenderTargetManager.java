package mylie.graphics.managers;

import mylie.graphics.ApiManager;
import mylie.graphics.RenderTask;

public interface RenderTargetManager extends ApiManager {
	void clearRenderTarget(RenderTask renderTask, RenderTarget target, RenderTarget.ClearOperation clearOperation);

	void bindRenderTarget(RenderTask renderTask, RenderTarget renderTarget, RenderTarget.BindingMode bindingMode);
}
