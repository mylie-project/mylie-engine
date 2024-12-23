package mylie.graphics.opengl.api;

import mylie.graphics.Datatypes;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.opengl.GlApiFeature;

public interface GlRendering extends GlApiFeature {
	void drawArrays(Mesh.Lod lod);

	void drawElements(Datatypes.DataBuffer<?> indexBuffer, Mesh.Lod lod);

	default int convertMeshMode(Mesh.Lod.RenderMode mode) {
		switch (mode) {
			case Triangles -> {
				return 4;
			}
			case TriangleStrip -> {
				return 5;
			}
			case TriangleFan -> {
				return 6;
			}
			case TriangleAdjacency -> {
				return 12;
			}
			case TriangleStripAdjacency -> {
				return 13;
			}
			case Lines -> {
				return 1;
			}
			case LineStrip -> {
				return 3;
			}
			case LineLoop -> {
				return 2;
			}
			case LineStripAdjacency -> {
				return 11;
			}
			case LinesAdjacency -> {
				return 10;
			}
			case Points -> {
				return 0;
			}
			case Patches -> {
				return 14;
			}
		}

		throw new UnsupportedOperationException("Invalid mesh render mode: " + mode);
	}
}
