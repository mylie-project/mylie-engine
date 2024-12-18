package mylie.graphics;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.component.BaseCoreComponent;
import mylie.component.Lifecycle;
import mylie.core.Engine;
import mylie.core.Timer;

@Getter(AccessLevel.PACKAGE)
public class GraphicsModule extends BaseCoreComponent implements Lifecycle.AddRemove, Lifecycle.Update {
    private Api api;



    @Override
    public void onAdd() {
        component(new GraphicsManager(this));
        api = engineOption(Engine.Options.GraphicsApi).get();
        api.onInitialize(componentManager());
    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onUpdate(Timer.Time time) {}

    @Override
    public String toString() {
        return "GraphicsModule";
    }
}
