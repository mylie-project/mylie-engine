package mylie.math.collision;

import org.joml.Intersectionf;

public class CollisionMath {
	public static boolean intersects(Sphere sphere1, Sphere sphere2) {
		return Intersectionf.testSphereSphere(sphere1.center(), sphere1.radiusSquared(), sphere2.center(),
				sphere2.radiusSquared());
	}

	public static boolean intersects(AxisAlignedBox box1, AxisAlignedBox box2) {
		return Intersectionf.testAabAab(box1.min(), box1.max(), box2.min(), box2.max());
	}

	public static boolean intersects(Sphere sphere1, AxisAlignedBox box1) {
		return Intersectionf.testAabSphere(box1.min(), box1.max(), sphere1.center(), sphere1.radiusSquared());
	}

	public static boolean intersects(Ray ray1, Sphere sphere1) {
		return Intersectionf.testRaySphere(ray1.origin(), ray1.direction(), sphere1.center(), sphere1.radiusSquared());
	}

	public static boolean intersects(Ray ray1, AxisAlignedBox box1) {
		return Intersectionf.testRayAab(ray1.origin(), ray1.direction(), box1.min(), box1.max());
	}

	public static boolean intersects(Ray ray1, Ray ray2) {
		// Calculate if two rays intersect using the shortest distance approach
		float EPSILON = 1e-6f; // Small tolerance value for floating-point comparisons
		var p1 = ray1.origin();
		var d1 = ray1.direction();
		var p2 = ray2.origin();
		var d2 = ray2.direction();

		var p1p2 = p2.sub(p1, new org.joml.Vector3f());
		var d1xd2 = d1.cross(d2, new org.joml.Vector3f());
		float det = d1xd2.lengthSquared();

		// If the cross product is close to zero, the rays are parallel or collinear
		if (det < EPSILON) {
			return false;
		}

		// Calculate parametric distances along d1 and d2 to closest points
		float t1 = p1p2.cross(d2, new org.joml.Vector3f()).dot(d1xd2) / det;
		float t2 = p1p2.cross(d1, new org.joml.Vector3f()).dot(d1xd2) / det;

		// Verify if closest points lie within ray bounds (t >= 0)
		return t1 >= 0 && t2 >= 0;
	}
}
