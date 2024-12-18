package mylie.math.collidables;

import mylie.math.Vector3fc;

public class Sphere extends Collidable {
	final Vector3fc center;
	final float radius;

	public Sphere(Vector3fc center, float radius) {
		super(1);
		this.center = center;
		this.radius = radius;
	}
}
