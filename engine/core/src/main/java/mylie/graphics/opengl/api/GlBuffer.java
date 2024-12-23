package mylie.graphics.opengl.api;

import mylie.graphics.Datatypes;
import mylie.graphics.NativeData;
import mylie.graphics.opengl.BindingState;
import mylie.graphics.opengl.GlApiFeature;
import mylie.graphics.opengl.managers.GlBufferManager;

public interface GlBuffer extends GlApiFeature {

    void createBuffer(NativeData.SharedData.Handle handle);

    default boolean bindBuffer(NativeData.SharedData.Handle handle, GlBufferManager.Target target){
        BindingState bindingState = BindingState.get();
        if(bindingState.bufferBindings().get(target) == handle){
            return false;
        }
        bindingState.bufferBindings().put(target, handle);
        return true;
    }

    void bufferData(Datatypes.DataBuffer<?> buffer, GlBufferManager.Target target, GlBufferManager.AccessMode accessMode);

    void updateBufferData(Datatypes.DataBuffer<?> buffer, GlBufferManager.Target target, GlBufferManager.AccessMode accessMode);

    void deleteBuffer(NativeData.SharedData.Handle handle);
}
