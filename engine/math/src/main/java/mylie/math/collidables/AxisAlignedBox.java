package mylie.math.collidables;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.math.Vector3fc;

@Getter(AccessLevel.PACKAGE)
public final class AxisAlignedBox extends Collidable {
	private final Vector3fc min, max;

	public AxisAlignedBox(Vector3fc min, Vector3fc max) {
		super(0);
		this.min = min;
		this.max = max;
	}
}
