package mylie.component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.async.*;
import mylie.core.Timer;
@Slf4j
public abstract sealed class BaseComponent implements Component permits BaseCoreComponent, BaseAppComponent {
	@Setter(AccessLevel.PACKAGE)
	@Getter(AccessLevel.PACKAGE)
	private ComponentManager componentManager;

	@Setter(AccessLevel.PACKAGE)
	private boolean initialized = false, enabled = false, requestEnabled = true;

	@Setter(AccessLevel.PROTECTED)
	private ExecutionMode executionMode = new ExecutionMode(ExecutionMode.Mode.Async, Target.Background,
			Caches.OneFrame);

	@Getter(AccessLevel.PACKAGE)
	private final List<BaseComponent> dependencies = new CopyOnWriteArrayList<>();

	void runAfter(Class<? extends Component> component1) {
		Component theComponent = componentManager().getComponent(component1);
		if (theComponent instanceof BaseComponent baseComponent) {
			dependencies().add(baseComponent);
		}
	}

	void runBefore(Class<? extends Component> component1) {
		Component theComponent = componentManager().getComponent(component1);
		if (theComponent instanceof BaseComponent baseComponent) {
			baseComponent.dependencies().add(this);
		}
	}

	Result<Async.Void> update(Timer.Time time) {
		return Async.async(executionMode, time.version(), UpdateComponent, this, time);
	}

	Result<Async.Void> shutdown(Timer.Time time) {
		return Async.async(executionMode, time.version(), DestroyComponent, this, time);
	}

	private final Functions.F1<Async.Void, BaseComponent, Timer.Time> UpdateComponent = new Functions.F1<>(
			"UpdateComponent") {
		@Override
		protected Async.Void run(BaseComponent component, Timer.Time time) {
			Wait.wait(Async.async(dependencies, BaseComponent.class, BaseComponent::update, time));
			if (!component.initialized) {
				component.initialized = true;
				if (component instanceof Lifecycle.InitDestroy initDestroy) {
					log.trace("Component<{}>.onInit()", component.getClass().getSimpleName());
					initDestroy.onInit();
				}
			}
			if (component.enabled != component.requestEnabled) {
				component.enabled = component.requestEnabled;
				if (component instanceof Lifecycle.EnableDisable enableDisable) {
					if (component.enabled) {
						log.trace("Component<{}>.onEnable()", component.getClass().getSimpleName());
						enableDisable.onEnable();
					} else {
						log.trace("Component<{}>.onDisable()", component.getClass().getSimpleName());
						enableDisable.onDisable();
					}
				}
			}
			if (component.enabled) {
				if (component instanceof Lifecycle.Update update) {
					log.trace("Component<{}>.onUpdate()", component.getClass().getSimpleName());
					update.onUpdate(time);
				}
			}
			return Async.VOID;
		}
	};
	private final Functions.F1<Async.Void, BaseComponent, Timer.Time> DestroyComponent = new Functions.F1<>(
			"DestroyComponent") {
		@Override
		protected Async.Void run(BaseComponent component, Timer.Time time) {
			Wait.wait(Async.async(dependencies, BaseComponent.class, BaseComponent::shutdown, time));
			if (component instanceof Lifecycle.InitDestroy initDestroy) {
				log.trace("Component<{}>.onDestroy()", component.getClass().getSimpleName());
				initDestroy.onDestroy();
			}
			return Async.VOID;
		}
	};

}
