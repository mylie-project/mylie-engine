package mylie.graphics.opengl;

import mylie.graphics.Api;
import mylie.graphics.ApiFeature;
import mylie.graphics.GraphicsCapabilities;
import mylie.graphics.GraphicsContext;
import mylie.graphics.opengl.api.GlGet;
import mylie.graphics.opengl.managers.GlBufferManager;
import mylie.graphics.opengl.managers.GlMeshManager;
import mylie.graphics.opengl.managers.GlRenderTargetManager;

import java.util.function.BiFunction;

public abstract class OpenGl extends Api {
    public OpenGl() {
        OpenGlCapabilities.GlApiVersion =
                new OpenGl.GlCapability<>("ApiVersion", GlGet.class, GlGet::getString, 7938);
        OpenGlCapabilities.GlApiMajorVersion =
                new OpenGl.GlCapability<>("ApiVersionMajor", GlGet.class, GlGet::getInteger, 33307);
        OpenGlCapabilities.GlApiMinorVersion =
                new OpenGl.GlCapability<>("ApiVersionMinor", GlGet.class, GlGet::getInteger, 33308);
        GraphicsCapabilities.MaxTextureSize =
                new OpenGl.GlCapability<>("MaxTextureSize", GlGet.class, GlGet::getInteger, 3379);
        GraphicsCapabilities.Max3dTextureSize =
                new OpenGl.GlCapability<>("Max3DTextureSize", GlGet.class, GlGet::getInteger, 32883);
    }



    @Override
    public void initApiManagers(GraphicsContext context) {
        manager(context,new GlRenderTargetManager());
        manager(context,new GlBufferManager());
        manager(context,new GlMeshManager());
    }


    public static class GlCapability<T,C extends ApiFeature> extends GraphicsCapabilities.Capability<T> {
        final Class<C> apiFeature;
        final BiFunction<C, Integer, T> getter;
        final int parameter;
        public GlCapability(String name, Class<C> apiFeature, BiFunction<C, Integer, T> getter, int parameter) {
            super(name);
            this.apiFeature = apiFeature;
            this.getter = getter;
            this.parameter = parameter;
        }

        @Override
        protected T init(GraphicsContext context) {
            C api=api(apiFeature,context);
            return getter.apply(api, parameter);
        }
    }
}
