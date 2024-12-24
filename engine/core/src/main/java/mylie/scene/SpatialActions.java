package mylie.scene;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class SpatialActions {
	interface Translatable {
		default void position(float x, float y, float z) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().position.set(x, y, z);
				spatial.onLocalTransformChanged();
			}
		}

		default void position(Vector3fc vector) {
			position(vector.x(), vector.y(), vector.z());
		}

		default void translate(float x, float y, float z) {
			if (this instanceof Spatial spatial) {
				Vector3f position = spatial.localTransform().position();
				position(position.x + x, position.y + y, position.z + z);
			}
		}

		default void translate(Vector3fc vector) {
			translate(vector.x(), vector.y(), vector.z());
		}
	}

	interface UniformScaleable {
		default void scale(float scale) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().scale().set(scale, scale, scale);
				spatial.onLocalTransformChanged();
			}
		}
	}

	interface Scaleable {
		default void scale(float x, float y, float z) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().scale().set(x, y, z);
				spatial.onLocalTransformChanged();
			}
		}

		default void scale(Vector3fc vector) {
			scale(vector.x(), vector.y(), vector.z());
		}
	}

	interface Rotatable {
		default void rotateDeg(float angle, Vector3fc axis) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().rotation().rotationAxis(Math.toRadians(angle), axis);
				spatial.onLocalTransformChanged();
			}
		}

		default void rotationAngles(float x, float y, float z) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().rotation().rotateXYZ(x, y, z);
				spatial.onLocalTransformChanged();
			}
		}
	}
}
