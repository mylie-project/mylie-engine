package mylie.lwjgl3.opengl.api;

import lombok.extern.slf4j.Slf4j;
import mylie.graphics.Datatypes;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.opengl.api.GlRendering;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
@Slf4j
public class Lwjgl3Rendering implements GlRendering {
	@Override
	public void drawArrays(Mesh.Lod lod) {
		log.trace("drawArrays({},{},{})", lod.renderMode(), lod.offset(), lod.count());
		GL20.glDrawArrays(convertMeshMode(lod.renderMode()), lod.offset(), lod.count());
	}

	@Override
	public void drawElements(Datatypes.DataBuffer<?> indexBuffer, Mesh.Lod lod) {
		log.trace("drawElements({},{},{})", lod.renderMode(), lod.offset(), lod.count());
		int type = 0;
		if (indexBuffer.dataType() == Datatypes.Integer) {
			type = GL11.GL_UNSIGNED_INT;
		} else {
			throw new UnsupportedOperationException("Only unsigned integers are supported for index buffers");
		}
		GL20.glDrawElements(convertMeshMode(lod.renderMode()), lod.count(), type, lod.offset());
	}
}
