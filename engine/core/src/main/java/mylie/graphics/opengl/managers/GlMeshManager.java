package mylie.graphics.opengl.managers;

import mylie.graphics.Datatypes;
import mylie.graphics.GraphicsContext;
import mylie.graphics.NativeData;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.VertexDataLayouts;
import mylie.graphics.geometry.VertexDataPoints;
import mylie.graphics.managers.MeshManager;
import mylie.graphics.opengl.api.GlVao;

public class GlMeshManager implements MeshManager {
    GlVao glVao;
    GlBufferManager bufferManager;
    final NativeData.NonSharedData<Mesh, VaoHandle> meshHandles = new NativeData.NonSharedData<>();


    @Override
    public void bindMesh(RenderTask renderTask, Mesh mesh) {
        renderTask.subTask(() -> bindMeshInternal(renderTask, mesh));
    }


    void bindMeshInternal(RenderTask renderTask, Mesh mesh) {
        VaoHandle handle = meshHandles.get(renderTask.context(), mesh);
        if (handle == null) {
            handle = new VaoHandle();
            meshHandles.put(renderTask.context(), mesh, handle);
            glVao.createVao(handle);
        }
        glVao.bindVao(handle);
        if (handle.version() < mesh.version()) {
            VertexDataLayouts.VertexDataLayout vertexDataLayout = getVertexDataLayout(mesh);
            for (VertexDataPoints.VertexDataPoint<?> dataPoint : vertexDataLayout.dataPoints()) {
                Datatypes.DataBuffer<?> dataBuffer = getDataBuffer(mesh, dataPoint);
                if (bufferManager.checkBindBuffer(dataBuffer, GlBufferManager.Target.Array, GlBufferManager.AccessMode.StaticDraw)) {
                    glVao.attribPointer(dataPoint.bindingPoint(), dataPoint.dataType(), false, 0, 0);
                }
            }
            Datatypes.DataBuffer<?> indexBuffer = indexBuffer(mesh);
            bufferManager.checkBindBuffer(indexBuffer, GlBufferManager.Target.ElementArray, GlBufferManager.AccessMode.StaticDraw);
            handle.version(mesh.version());
        }
    }

    @Override
    public boolean isSupported(GraphicsContext context) {
        glVao = api(GlVao.class, context);
        bufferManager = manager(GlBufferManager.class, context);
        return glVao != null && bufferManager != null;
    }

    private static class VaoHandle extends NativeData.NonSharedData.Handle {

    }
}
