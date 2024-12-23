package mylie.graphics.opengl.managers;

import mylie.graphics.Datatypes;
import mylie.graphics.GraphicsContext;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.MeshDataAccess;
import mylie.graphics.managers.MeshManager;
import mylie.graphics.managers.RenderManager;
import mylie.graphics.opengl.api.GlRendering;

public class GlRenderManager implements RenderManager, MeshDataAccess {
    private MeshManager meshManager;
    private GlRendering glRendering;
    @Override
    public boolean isSupported(GraphicsContext context) {
        meshManager=manager(MeshManager.class,context);
        glRendering=api(GlRendering.class,context);
        return meshManager != null && glRendering != null;
    }

    @Override
    public void render(RenderTask renderTask, Mesh mesh) {
        meshManager.bindMesh(renderTask, mesh);
        Datatypes.DataBuffer<?> indexBuffer = indexBuffer(mesh);
        renderTask.subTask(() -> {
            if(indexBuffer != null){
                glRendering.drawElements(indexBuffer,mesh.lod(0));
            }else{
                glRendering.drawArrays(mesh.lod(0));
            }
        });
    }


}
