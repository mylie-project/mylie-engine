package mylie.graphics.geometry;

import lombok.Value;
import mylie.graphics.Datatypes;
import org.joml.Vector2fc;
import org.joml.Vector3fc;

public interface VertexDataPoints {
    VertexDataPoint<Vector3fc> Position = new VertexDataPoint<>("vertexPosition",0, Datatypes.Vector3f);
    VertexDataPoint<Vector3fc> Normal = new VertexDataPoint<>("vertexNormal",2, Datatypes.Vector3f);
    VertexDataPoint<Vector3fc> Color = new VertexDataPoint<>("vertexColor",1, Datatypes.Vector3f);
    VertexDataPoint<Vector2fc> TextureCoordinates0 =
            new VertexDataPoint<>("vertexTextureCoordinates0",3, Datatypes.Vector2f);

    @Value
    class VertexDataPoint<T> {
        String name;
        int bindingPoint;
        Datatypes.PrimitiveDataType<T> dataType;
    }
}
