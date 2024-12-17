package mylie.component;

import mylie.core.Timer;

public interface Lifecycle {
    interface AddRemove {
        void onAdd();

        void onRemove();
    }

    interface InitDestroy {
        void onInit();

        void onDestroy();
    }

    interface EnableDisable {
        void onEnable();

        void onDisable();

        default void enabled(boolean enabled) {
            if (this instanceof BaseComponent component) {
                component.requestEnabled(enabled);
            }
        }
    }

    interface Update {
        void onUpdate(Timer.Time time);
    }
}
