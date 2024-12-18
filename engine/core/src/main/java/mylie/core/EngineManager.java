package mylie.core;

import mylie.component.AppComponent;

public class EngineManager implements AppComponent {
	private final Core core;

	public EngineManager(Core core) {
		this.core = core;
	}

	public void shutdown(Engine.ShutdownReason reason) {
		core.onShutdown(reason);
	}
}
