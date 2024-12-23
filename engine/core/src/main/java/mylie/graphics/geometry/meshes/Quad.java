package mylie.graphics.geometry.meshes;

import java.util.List;
import mylie.graphics.geometry.Mesh;
import mylie.graphics.geometry.VertexDataLayouts;
import mylie.graphics.geometry.VertexDataPoints;
import mylie.math.Constants;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Quad extends Mesh {

	static Vector3fc[] positions = { // Define vertices of a quad (two triangles making up a rectangle)
			new Vector3f(-0.5f, -0.5f, 0.0f), // Bottom-left
			new Vector3f(0.5f, -0.5f, 0.0f), // Bottom-right
			new Vector3f(0.5f, 0.5f, 0.0f), // Top-right
			new Vector3f(-0.5f, 0.5f, 0.0f) // Top-left
	};
	static Vector2fc[] texCoordinates = { // Define vertices of a quad (two triangles making up a rectangle)
			new Vector2f(0.0f, 0.0f), // Bottom-left
			new Vector2f(1.0f, 0.0f), // Bottom-right
			new Vector2f(1.0f, 1.0f), // Top-right
			new Vector2f(0.0f, 1.0f) // Top-left
	};
	static Vector3fc normal = Constants.UnitZ;

	public Quad(VertexDataLayouts.VertexDataLayout vertexLayout) {
		super(vertexLayout, 4);

		vertexData(VertexDataPoints.Position, positions); // Set vertex data
		if (vertexLayout.contains(VertexDataPoints.TextureCoordinates0)) {
			vertexData(VertexDataPoints.TextureCoordinates0, texCoordinates);
		}
		if (vertexLayout.contains(VertexDataPoints.Normal)) {
			vertexData(VertexDataPoints.Normal, normal, normal, normal, normal);
		}
		List<Integer> indices = List.of(0, 1, 2, 2, 3, 0); // Define indices (two triangles to form a quad)
		Lod lod = new Lod(Lod.RenderMode.Triangles, indices); // Create LOD with indices
		lod(lod); // Add LOD to this Mesh
	}
}
