package mylie.component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.async.*;
import mylie.core.Timer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseComponent implements Component {
    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private ComponentManager componentManager;

    @Setter(AccessLevel.PACKAGE)
    private boolean initialized = false, enabled = false, requestEnabled = true;

    @Setter(AccessLevel.PACKAGE)
    private ExecutionMode executionMode =
            new ExecutionMode(ExecutionMode.Mode.Async, Target.Background, Caches.OneFrame);

    @Getter(AccessLevel.PACKAGE)
    private List<BaseComponent> dependencies = new CopyOnWriteArrayList<>();

    Result<Async.Void> update(Timer.Time time) {
        return Async.async(executionMode, time.version(), UpdateComponent, this, time);
    }

    Result<Async.Void> shutdown(Timer.Time time) {
        return Async.async(executionMode, time.version(), DestroyComponent, this, time);
    }



    private final Functions.F1<Async.Void, BaseComponent, Timer.Time> UpdateComponent =
            new Functions.F1<>("UpdateComponent") {
                @Override
                protected Async.Void run(BaseComponent component, Timer.Time time) {
                    Wait.wait(Async.async(dependencies, BaseComponent.class, BaseComponent::update, time));
                    if (!component.initialized) {
                        component.initialized = true;
                        if (component instanceof Lifecycle.InitDestroy initDestroy) {
                            initDestroy.onInit();
                        }
                    }
                    if (component.enabled != component.requestEnabled) {
                        component.enabled = component.requestEnabled;
                        if (component instanceof Lifecycle.EnableDisable enableDisable) {
                            if (component.enabled) {
                                enableDisable.onEnable();
                            } else {
                                enableDisable.onDisable();
                            }
                        }
                    }
                    if (component.enabled) {
                        if (component instanceof Lifecycle.Update update) {
                            update.onUpdate(time);
                        }
                    }
                    return Async.VOID;
                }
            };
    private final Functions.F1<Async.Void, BaseComponent, Timer.Time> DestroyComponent =
            new Functions.F1<>("DestroyComponent") {
                @Override
                protected Async.Void run(BaseComponent component, Timer.Time time) {
                    if (component instanceof Lifecycle.InitDestroy initDestroy) {
                        initDestroy.onDestroy();
                    }
                    return Async.VOID;
                }
            };
}
