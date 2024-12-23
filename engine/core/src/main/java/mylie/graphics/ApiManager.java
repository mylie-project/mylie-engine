package mylie.graphics;

public interface ApiManager {
	boolean isSupported(GraphicsContext context);

	default <T extends ApiFeature> T api(Class<T> apiClass, GraphicsContext context) {
		return context.api(apiClass);
	}

	default <T extends ApiManager> T manager(Class<T> managerClass, GraphicsContext context) {
		return context.manager(managerClass);
	}

}
