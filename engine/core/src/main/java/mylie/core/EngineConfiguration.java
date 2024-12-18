package mylie.core;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.util.configuration.Configurable;
import mylie.util.configuration.Configurations;
import mylie.util.configuration.Option;

public class EngineConfiguration implements Configurable<EngineConfiguration, EngineConfiguration.EngineOption<?>> {
	public static class EngineOption<T> extends Option<EngineConfiguration, T> {
		public EngineOption(String name, T defaultValue) {
			super(name, defaultValue);
		}
	}

	@Getter(AccessLevel.PUBLIC)
	private final Configurations<EngineConfiguration, EngineOption<?>> configuration = new Configurations.Map<>();

	EngineConfiguration() {
	}
}
