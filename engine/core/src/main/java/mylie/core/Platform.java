package mylie.core;

public abstract class Platform {
	public static EngineConfiguration initialize(Platform platform) {
		EngineConfiguration engineConfiguration = new EngineConfiguration();
		platform.initializePlatform(engineConfiguration);
		return engineConfiguration;
	}

	protected abstract void initializePlatform(EngineConfiguration configuration);
}
