package mylie.math.collision;

import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Getter
public class Sphere implements Collidable {
	private Vector3f center = new Vector3f();
	private float radius = 0;
	private float radiusSquared = 0;
	public Sphere() {

	}

	public Sphere(Vector3fc center, float radius) {
		this();
		this.center.set(center);
		radius(radius);
	}

	public void radius(float radius) {
		this.radius = radius;
		this.radiusSquared = radius * radius;
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
