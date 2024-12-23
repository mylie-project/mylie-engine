package mylie.graphics.geometry;

import mylie.graphics.Datatypes;

public interface MeshDataAccess {

	default VertexDataLayouts.VertexDataLayout getVertexDataLayout(Mesh mesh) {
		return mesh.vertexDataLayout;
	}

	default Datatypes.DataBuffer<?> getDataBuffer(Mesh mesh, VertexDataPoints.VertexDataPoint<?> vertexDataPoint) {
		return mesh.vertexDataBuffer(vertexDataPoint);
	}

	default Datatypes.DataBuffer<?> indexBuffer(Mesh mesh) {
		return mesh.indices;
	}
}
