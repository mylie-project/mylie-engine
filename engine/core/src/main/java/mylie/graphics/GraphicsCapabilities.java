package mylie.graphics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class GraphicsCapabilities {
    private static final List<Capability<?>> allCapabilities = new ArrayList<>();
    public static Capability<Integer> MaxTextureSize;
    public static Capability<Integer> Max3dTextureSize;

    @Getter(AccessLevel.PRIVATE)
    private final Map<Capability<?>,Object> capabilities=new HashMap<>();

    public static abstract class Capability<T> {
        private final String name;
        public Capability(String name) {
            this.name = name;
            allCapabilities.add(this);
        }
        static void initAll(GraphicsContext context) {
            for (Capability<?> capability : allCapabilities) {
                Object init = capability.init(context);
                context.capabilities().capabilities().put(capability, init);
                log.debug("Capability<{}> = {}", capability.name, init);
            }
        }

        protected abstract T init(GraphicsContext context);

        protected <T extends ApiFeature> T api(Class<T> apiClass, GraphicsContext context) {
            return context.api(apiClass);
        }
    }


}
