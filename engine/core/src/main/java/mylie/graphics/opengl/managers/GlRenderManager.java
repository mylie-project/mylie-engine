package mylie.graphics.opengl.managers;

import mylie.graphics.Datatypes;
import mylie.graphics.GraphicsContext;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.MeshDataAccess;
import mylie.graphics.managers.MeshManager;
import mylie.graphics.managers.RenderManager;
import mylie.graphics.opengl.api.GlRendering;
import mylie.graphics.opengl.api.GlVao;

public class GlRenderManager implements RenderManager, MeshDataAccess {
	private MeshManager meshManager;
	private GlRendering glRendering;
	private GlVao glVao;
	@Override
	public boolean isSupported(GraphicsContext context) {
		glVao = api(GlVao.class, context);
		meshManager = manager(MeshManager.class, context);
		glRendering = api(GlRendering.class, context);
		return meshManager != null && glRendering != null && glVao != null;
	}

	@Override
	public void render(RenderTask renderTask, Mesh mesh) {
		meshManager.bindMesh(renderTask, mesh);
		Datatypes.DataBuffer<?> indexBuffer = indexBuffer(mesh);
		renderTask.subTask(() -> {
			if (indexBuffer != null) {
				glRendering.drawElements(indexBuffer, mesh.lod(0));
			} else {
				glRendering.drawArrays(mesh.lod(0));
			}
		});
	}

}
