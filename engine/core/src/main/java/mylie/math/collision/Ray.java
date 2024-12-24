package mylie.math.collision;

import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Getter
public class Ray implements Collidable {
	private Vector3f origin = new Vector3f();
	private Vector3f direction = new Vector3f();

	public Ray(Vector3fc origin, Vector3fc direction) {
		this.origin.set(origin);
		this.direction.set(direction);
	}

	@Override
	public boolean intersects(Collidable other) {
		return switch (other) {
			case Sphere sphere -> CollisionMath.intersects(this, sphere);
			case AxisAlignedBox box -> CollisionMath.intersects(this, box);
			case Ray ray -> CollisionMath.intersects(ray, this);
			case Custom custom -> custom.intersects(this);
			default -> throw new RuntimeException("Unsupported Collidable type");
		};
	}
}
