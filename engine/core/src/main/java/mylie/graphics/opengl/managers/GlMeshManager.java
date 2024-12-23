package mylie.graphics.opengl.managers;

import lombok.extern.slf4j.Slf4j;
import mylie.graphics.Datatypes;
import mylie.graphics.GraphicsContext;
import mylie.graphics.NativeData;
import mylie.graphics.RenderTask;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.VertexDataLayouts;
import mylie.graphics.geometry.VertexDataPoints;
import mylie.graphics.managers.MeshManager;
import mylie.graphics.opengl.api.GlVao;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class GlMeshManager implements MeshManager {
	GlVao glVao;
	GlBufferManager bufferManager;
	final NativeData.NonSharedData<Mesh, VaoHandle> meshHandles = new NativeData.NonSharedData<>(VaoHandle::new);

	@Override
	public void bindMesh(RenderTask renderTask, Mesh mesh) {
		final VaoHandle handle = meshHandles.get(renderTask.context(), mesh);
		if (handle.handle() == -1) {
			meshHandles.put(renderTask.context(), mesh, handle);
			renderTask.subTask(() -> glVao.createVao(handle));
		}
		renderTask.subTask(() -> glVao.bindVao(handle));
		if (handle.version() < mesh.version()) {
			VertexDataLayouts.VertexDataLayout vertexDataLayout = getVertexDataLayout(mesh);
			for (VertexDataPoints.VertexDataPoint<?> dataPoint : vertexDataLayout.dataPoints()) {
				Datatypes.DataBuffer<?> dataBuffer = getDataBuffer(mesh, dataPoint);
				boolean enableAttribute=false;
				if(!handle.activeDataPoints.contains(dataPoint)){
					bufferManager.bindBuffer(renderTask,dataBuffer,GlBufferManager.Target.Array,GlBufferManager.AccessMode.StaticDraw);
					enableAttribute=true;
				}else{
					if (bufferManager.checkBindBuffer(renderTask, dataBuffer, GlBufferManager.Target.Array,
							GlBufferManager.AccessMode.StaticDraw)) {
						enableAttribute=true;
					}
				}
				if(enableAttribute) {
					handle.activeDataPoints.add(dataPoint);
					renderTask.subTask(() -> {
						glVao.attribPointer(dataPoint.bindingPoint(), dataPoint.dataType(), false, 0, 0);
						glVao.enableAttributes(dataPoint);
					});
				}
			}
			Datatypes.DataBuffer<?> indexBuffer = indexBuffer(mesh);
			bufferManager.bindBuffer(renderTask, indexBuffer, GlBufferManager.Target.ElementArray,
					GlBufferManager.AccessMode.StaticDraw);
			handle.version(mesh.version());
		}
	}

	@Override
	public boolean isSupported(GraphicsContext context) {
		glVao = api(GlVao.class, context);
		bufferManager = manager(GlBufferManager.class, context);
		return glVao != null && bufferManager != null;
	}

	public static class VaoHandle extends NativeData.NonSharedData.Handle {
		Set<VertexDataPoints.VertexDataPoint<?>> activeDataPoints=new HashSet<>();
	}
}
