package mylie.util.configuration;

import java.util.HashMap;

public abstract class Configurations<TARGET, OPTION extends Option<TARGET, ?>> {
    protected abstract <T> void option(Option<TARGET, T> option, T value);

    protected abstract <T> T option(Option<TARGET, T> option);

    protected abstract Iterable<Option<TARGET, ?>> options();

    public static class Map<TARGET, OPTION extends Option<TARGET, ?>> extends Configurations<TARGET, OPTION> {
        private final java.util.Map<Option<TARGET, ?>, Object> dataStore = new HashMap<>();

        @Override
        protected <T> void option(Option<TARGET, T> option, T value) {
            dataStore.put(option, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <T> T option(Option<TARGET, T> option) {
            if (dataStore.containsKey(option)) {
                return (T) dataStore.get(option);
            } else {
                return option.defaultValue;
            }
        }

        @Override
        protected Iterable<Option<TARGET, ?>> options() {
            return dataStore.keySet();
        }
    }
}
