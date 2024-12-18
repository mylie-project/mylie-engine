package mylie.graphics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.Async;
import mylie.async.Result;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Engine;
import mylie.core.Timer;
import mylie.core.components.Scheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter(AccessLevel.PACKAGE)
public class GraphicsModule extends BaseCoreComponent implements Lifecycle.AddRemove, Lifecycle.Update,Lifecycle.InitDestroy {
    private Api api;
    private GraphicsContext primaryContext;
    private final List<GraphicsContext> activeContexts=new CopyOnWriteArrayList<>();
    private final List<GraphicsContext> syncedContexts=new CopyOnWriteArrayList<>();;
    private final List<Result<Async.Void>> swapBufferQueue=new LinkedList<>();

    @Override
    public void onAdd() {
        component(new GraphicsManager(this));
        api = engineOption(Engine.Options.GraphicsApi).get();
        api.onInitialize(componentManager());
    }

    public GraphicsContext createContext(GraphicsContextConfiguration configuration, boolean synced) {
        GraphicsContext graphicsContext=api.contextProvider().createContext(configuration,primaryContext);
        if(primaryContext==null){
            primaryContext=graphicsContext;
        }
        activeContexts.add(graphicsContext);
        if(synced){
            syncedContexts.add(graphicsContext);
        }
        graphicsContext.contextThread().start();

        return graphicsContext;
    }

    @Override
    public void onRemove() {}

    @Override
    public void onUpdate(Timer.Time time) {
        while (!swapBufferQueue.isEmpty()) {
            swapBufferQueue.removeFirst().result();
        }
        for (GraphicsContext activeContext : activeContexts) {
            swapBufferQueue.add(activeContext.swapBuffers());
        }
    }

    @Override
    public String toString() {
        return "GraphicsModule";
    }


    @Override
    public void onInit() {

    }

    @Override
    public void onDestroy() {
        for (GraphicsContext activeContext : activeContexts) {
            activeContext.destroy().result();
        }
    }
}
