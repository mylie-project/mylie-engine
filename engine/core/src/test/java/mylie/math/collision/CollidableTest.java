package mylie.math.collision;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

public class CollidableTest {

	// Description:
	// CollidableTest tests the `collides` method of the Collidable class.
	// This method determines if two Collidables overlap, using different algorithms
	// based on the types of collidables (spheres or axis-aligned boxes).

	@Test
	void testCollidesSphereSphere_Colliding() {
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 4);
		Sphere sphere2 = new Sphere(new Vector3f(3, 0, 0), 4);
		assertTrue(sphere1.intersects(sphere2));
	}

	@Test
	void testCollidesSphereSphere_NotColliding() {
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 4);
		Sphere sphere2 = new Sphere(new Vector3f(10, 0, 0), 4);
		assertFalse(sphere1.intersects(sphere2));
	}

	@Test
	void testCollidesAABB_AABB_Colliding() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(3, 3, 3), new Vector3f(7, 7, 7));
		assertTrue(box1.intersects(box2));
	}

	@Test
	void testCollidesAABB_AABB_NotColliding() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(5, 5, 5), new Vector3f(9, 9, 9));
		assertFalse(box1.intersects(box2));
	}

	@Test
	void testCollidesSphereAABB_Colliding() {
		Sphere sphere = new Sphere(new Vector3f(3, 3, 3), 4);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testCollidesSphereAABB_NotColliding() {
		Sphere sphere = new Sphere(new Vector3f(10, 10, 10), 4);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		assertFalse(sphere.intersects(box));
	}

	@Test
	void testCollidesAABBSphere_Colliding() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		Sphere sphere = new Sphere(new Vector3f(3, 3, 3), 4);
		assertTrue(box.intersects(sphere));
	}

	@Test
	void testCollidesAABBSphere_NotColliding() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		Sphere sphere = new Sphere(new Vector3f(10, 10, 10), 4);
		assertFalse(box.intersects(sphere));
	}
	@Test
	void testCollidesSphereWithZeroRadius() {
		Sphere sphere1 = new Sphere(new Vector3f(1, 1, 1), 0f);
		Sphere sphere2 = new Sphere(new Vector3f(1, 1, 1), 4);
		assertTrue(sphere1.intersects(sphere2));
	}

	@Test
	void testCollidesSphereWithExtremePositiveCoords() {
		Sphere sphere1 = new Sphere(new Vector3f(Float.MAX_VALUE / 2, 0, 0), 4);
		Sphere sphere2 = new Sphere(new Vector3f(Float.MAX_VALUE / 2, 5, 0), 4);
		assertTrue(sphere1.intersects(sphere2));
	}

	@Test
	void testNonCollidingSpheresWithExtremeNegativeCoords() {
		Sphere sphere1 = new Sphere(new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE), 4);
		Sphere sphere2 = new Sphere(new Vector3f(0, 0, 0), 4);
		assertFalse(sphere1.intersects(sphere2));
	}

	@Test
	void testRayInsideSphere() {
		Ray ray = new Ray(new Vector3f(1, 1, 1), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 5.0f);
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testRayOnSphereSurface() {
		Ray ray = new Ray(new Vector3f(6, 0, 0), new Vector3f(-1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 6);
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testTiltedAABBNonIntersectingSphere() {
		AxisAlignedBox tiltedBox = new AxisAlignedBox(new Vector3f(3, 3, 3), new Vector3f(6, 7, 8));
		Sphere sphere = new Sphere(new Vector3f(10, 10, 10), 4);
		assertFalse(tiltedBox.intersects(sphere));
	}

	@Test
	void testZeroSizeAABBagainstSphere() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(4, 4, 4), new Vector3f(4, 4, 4));
		Sphere sphere = new Sphere(new Vector3f(4, 4, 4), 1.0f);
		assertTrue(box.intersects(sphere));
	}

	@Test
	void testRayWithZeroDirectionDoesNotIntersectSphere() {
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 5.0f);
		assertFalse(ray.intersects(sphere));
	}

	@Test
	void testRayWithTinyDirectionIntersectsAABB() {
		Ray ray = new Ray(new Vector3f(1, 1, 1), new Vector3f(0.000001f, 0.000001f, 0.000001f));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(5, 5, 5));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testSphereInsideAnotherSphereTouching() {
		Sphere sphere1 = new Sphere(new Vector3f(1, 1, 1), 5);
		Sphere sphere2 = new Sphere(new Vector3f(1, 1, 1), 5);
		assertTrue(sphere1.intersects(sphere2));
	}

	@Test
	void testAABBInsideAnotherTouchingEdges() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(4, 4, 4));
		assertTrue(box1.intersects(box2));
	}

	@Test
	void testAABBWithNegativeSize() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(5, 5, 5), new Vector3f(3, 3, 3));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(2, 2, 2), new Vector3f(6, 6, 6));
		assertTrue(box1.intersects(box2));
	}

	@Test
	void testSphereWithNegativeRadius() {
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), -2);
		Sphere sphere2 = new Sphere(new Vector3f(1, 1, 1), 5);
		assertFalse(sphere1.intersects(sphere2));
	}

	@Test
	void testRayOriginInsideAABB() {
		Ray ray = new Ray(new Vector3f(2, 2, 2), new Vector3f(1, 1, 1));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testSphereBarelyTouchingAABBCorner() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Sphere sphere = new Sphere(new Vector3f(5, 5, 5), (float) Math.sqrt(75));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testRayStartingOutsideIntersectsAABB() {
		Ray ray = new Ray(new Vector3f(-10, -10, -10), new Vector3f(1, 1, 1));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testSphereOutsideExtremelyFarFromAABB() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Sphere sphere = new Sphere(new Vector3f(1e6f, 1e6f, 1e6f), 4);
		assertFalse(sphere.intersects(box));
	}

	@Test
	void testMinimalAABBIntersectingLargeSphere() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(1.0001f, 1.0001f, 1.0001f));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 10);
		assertTrue(box.intersects(sphere));
	}

	@Test
	void testRayOnTheEdgeOfSphere() {
		Ray ray = new Ray(new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(2, 0, 0), 1);
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testDegenerateRayInsideSphere() {
		Ray ray = new Ray(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 10);
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testCollidesAABBWithZeroSizeInsideAnother() {
		AxisAlignedBox smallBox = new AxisAlignedBox(new Vector3f(3, 3, 3), new Vector3f(3, 3, 3));
		AxisAlignedBox largeBox = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(6, 6, 6));
		assertTrue(smallBox.intersects(largeBox));
	}

	@Test
	void testRaySphereIntersection() {
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(5, 0, 0), 2);
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testRayAABBIntersection() {
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(3, 3, 3), new Vector3f(5, 5, 5));
		assertTrue(ray.intersects(box));
	}

}
