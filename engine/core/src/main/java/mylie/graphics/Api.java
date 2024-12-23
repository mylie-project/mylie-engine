package mylie.graphics;

import lombok.extern.slf4j.Slf4j;
import mylie.component.ComponentManager;

@Slf4j
public abstract class Api {
	protected abstract void onInitialize(ComponentManager componentManager);

	protected abstract ContextProvider contextProvider();

	public abstract void initApiFeatures(GraphicsContext context);

	public abstract void initApiManagers(GraphicsContext context);

	protected void manager(GraphicsContext context,ApiManager... manager) {
		for (ApiManager apiManager : manager) {
			if(apiManager.isSupported(context)){
				log.debug("Creating API manager: {}", apiManager.getClass().getSimpleName());
				context.apiManagers().add(apiManager);
				return;
			}
		}
	}

	protected void api(GraphicsContext context,ApiFeature... feature) {
		log.debug("Initializing API features: {}", java.util.Arrays.toString(feature));
		context.apiFeatures().addAll(java.util.Arrays.asList(feature));
	}
}
