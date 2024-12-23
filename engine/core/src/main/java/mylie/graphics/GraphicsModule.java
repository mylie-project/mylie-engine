package mylie.graphics;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.application.ApplicationModule;
import mylie.async.Async;
import mylie.async.Functions;
import mylie.async.Result;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Engine;
import mylie.core.Timer;
import mylie.graphics.opengl.BindingState;

@Slf4j
@Getter(AccessLevel.PACKAGE)
public class GraphicsModule extends BaseCoreComponent
		implements
			Lifecycle.AddRemove,
			Lifecycle.Update,
			Lifecycle.InitDestroy {
	private Api api;
	private GraphicsContext primaryContext;
	private final List<GraphicsContext> activeContexts = new CopyOnWriteArrayList<>();
	private final List<GraphicsContext> syncedContexts = new CopyOnWriteArrayList<>();
	private final List<Result<Async.Void>> swapBufferQueue = new LinkedList<>();

	@Override
	public void onAdd() {
		component(new GraphicsManager(this));
		api = engineOption(Engine.Options.GraphicsApi).get();
		api.onInitialize(componentManager());
	}

	public void waitForSync() {
		while (!swapBufferQueue.isEmpty()) {
			swapBufferQueue.removeFirst().result();
		}
	}

	public GraphicsContext createContext(GraphicsContextConfiguration configuration, boolean synced) {
		GraphicsContext graphicsContext = api.contextProvider().createContext(configuration, primaryContext);
		Result<Async.Void> init = Async.async(graphicsContext.executionMode(), Integer.MAX_VALUE, initContext,
				graphicsContext, api, primaryContext);
		if (primaryContext == null) {
			primaryContext = graphicsContext;
		}
		activeContexts.add(graphicsContext);
		if (synced) {
			syncedContexts.add(graphicsContext);
		}
		graphicsContext.contextThread().start();
		init.result();
		return graphicsContext;
	}

	@Override
	public void onRemove() {
	}

	@Override
	public void onUpdate(Timer.Time time) {
		for (GraphicsContext activeContext : syncedContexts) {
			swapBufferQueue.add(activeContext.swapBuffers());
		}
	}

	@Override
	public String toString() {
		return "GraphicsModule";
	}

	@Override
	public void onInit() {
		runAfter(ApplicationModule.class);
	}

	@Override
	public void onDestroy() {
		for (GraphicsContext activeContext : activeContexts) {
			activeContext.destroy().result();
		}
	}

	private static final Functions.F2<Async.Void, GraphicsContext, Api, GraphicsContext> initContext = new Functions.F2<>(
			"InitContext") {

		@Override
		protected Async.Void run(GraphicsContext context, Api graphicsApi, GraphicsContext primaryContext) {
			BindingState.init();

			if (primaryContext == null) {
				GraphicsCapabilities.Capability.initAll(context);
				graphicsApi.initApiFeatures(context, primaryContext);
				graphicsApi.initApiManagers(context, primaryContext);
			} else {
				context.capabilities(primaryContext.capabilities());
				for (ApiFeature apiFeature : primaryContext.apiFeatures()) {
					context.apiFeatures().add(apiFeature);
				}
				for (ApiManager apiManager : primaryContext.apiManagers()) {
					context.apiManagers().add(apiManager);
				}
			}
			return Async.VOID;
		}
	};
}
