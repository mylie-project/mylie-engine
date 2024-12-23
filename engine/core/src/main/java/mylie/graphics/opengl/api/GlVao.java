package mylie.graphics.opengl.api;

import mylie.graphics.Datatypes;
import mylie.graphics.NativeData;
import mylie.graphics.opengl.BindingState;
import mylie.graphics.opengl.GlApiFeature;

public interface GlVao extends GlApiFeature {
    void createVao(NativeData.NonSharedData.Handle handle);

    default boolean bindVao(NativeData.NonSharedData.Handle handle){
        BindingState bindingState = BindingState.get();
        if(bindingState.currentVao() == handle.handle()){
            return false;
        }
        bindingState.currentVao(handle.handle());
        return true;
    }

    void attribPointer(int bindingPoint, Datatypes.PrimitiveDataType<?> dataType, boolean normalized, int stride, int offset);
}
