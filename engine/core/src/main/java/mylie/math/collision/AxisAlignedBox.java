package mylie.math.collision;

import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Getter
public class AxisAlignedBox implements Collidable {
	private Vector3f min = new Vector3f();
	private Vector3f max = new Vector3f();
	public AxisAlignedBox() {

	}

	public AxisAlignedBox(Vector3fc min, Vector3fc max) {
		this();
		this.min.set(min);
		this.max.set(max);
	}

	@Override
	public boolean intersects(Collidable other) {
		return switch (other) {
			case Sphere sphere -> CollisionMath.intersects(sphere, this);
			case AxisAlignedBox box -> CollisionMath.intersects(box, this);
			case Ray ray -> CollisionMath.intersects(ray, this);
			case Custom custom -> custom.intersects(this);
			default -> throw new RuntimeException("Unsupported Collidable type");
		};
	}
}
