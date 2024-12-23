package mylie.graphics;

public interface ApiManager {
    boolean isSupported(GraphicsContext context);

    default <T extends ApiFeature> T api(Class<T> apiClass,GraphicsContext context) {
        return context.api(apiClass);
    }

}
