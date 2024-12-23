package mylie.lwjgl3.opengl.api;

import lombok.extern.slf4j.Slf4j;
import mylie.graphics.Datatypes;
import mylie.graphics.NativeData;
import mylie.graphics.opengl.api.GlVao;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

@Slf4j
public class Lwjgl3GlVao implements GlVao {
    @Override
    public void createVao(NativeData.NonSharedData.Handle handle) {
        log.trace("createVao()");
        int i = GL30.glGenVertexArrays();
        handle.handle(i);
    }

    @Override
    public boolean bindVao(NativeData.NonSharedData.Handle handle) {
        boolean bind= GlVao.super.bindVao(handle);
        if(bind){
            log.trace("bindVao({})", handle.handle());
            GL30.glBindVertexArray(handle.handle());
        }
        return bind;
    }

    @Override
    public void attribPointer(int bindingPoint, Datatypes.PrimitiveDataType<?> dataType, boolean normalized, int stride, int offset) {
        log.trace("attribPointer({}, {}, {}, {}, {})", bindingPoint, dataType, normalized, stride, offset);
        GL30.glVertexAttribPointer(bindingPoint,dataType.components(),convertDataType(dataType), normalized,stride,offset);
    }

    private int convertDataType(Datatypes.PrimitiveDataType<?> dataType) {
        if(dataType==Datatypes.Vector3f){
            return GL21.GL_FLOAT;
        }
        if(dataType==Datatypes.Vector2f){
            return GL21.GL_FLOAT;
        }
        if(dataType==Datatypes.Float){
            return GL21.GL_FLOAT;
        }
        if(dataType==Datatypes.Integer){
            return GL21.GL_INT;
        }
        throw new UnsupportedOperationException("Unsupported data type: " + dataType);
    }
}
