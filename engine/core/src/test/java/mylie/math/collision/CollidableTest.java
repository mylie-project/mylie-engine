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
		assertTrue(sphere1.intersects(sphere2));
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

	
	
	@Test
	void testSphereTouchingMinimalEdgeAABB() {
		Sphere sphere = new Sphere(new Vector3f(5, 5, 5), (float) Math.sqrt(3));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(6, 6, 6), new Vector3f(6.0001f, 6.0001f, 6.0001f));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testRayParallelToAABBEdgeNotIntersecting() {
		Ray ray = new Ray(new Vector3f(-2, -2, 5), new Vector3f(1, 0, 0));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(3, 3, 3));
		assertFalse(ray.intersects(box));
	}

	@Test
	void testRayGrazingSphereSurface() {
		Ray ray = new Ray(new Vector3f(0, 0, 1), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(2, 0, 0), 1);
		assertFalse(ray.intersects(sphere));
	}

	@Test
	void testSphereTouchingCornerOfAABB() {
		Sphere sphere = new Sphere(new Vector3f(5, 5, 5), 5);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(10, 10, 10), new Vector3f(15, 15, 15));
		assertFalse(sphere.intersects(box));
	}

	@Test
	void testRayDiagonalNonIntersectingAABB() {
		Ray ray = new Ray(new Vector3f(-3, -3, -3), new Vector3f(1, 1, 2));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		assertFalse(ray.intersects(box));
	}

	@Test
	void testExtremeLargeSphereIntersectingSmallAABB() {
		Sphere sphere = new Sphere(new Vector3f(1e30f, 1e30f, 1e30f), 1e30f);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(2, 2, 2));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testRayNearlyParallelToAABBSurface() {
		Ray ray = new Ray(new Vector3f(-5, 0, 0), new Vector3f(1, 0.0001f, 0));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(3, 3, 3));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testSphereWithNegativeExtremeCoordinates() {
		Sphere sphere = new Sphere(new Vector3f(-1e30f, -1e30f, -1e30f), 5);
		Sphere otherSphere = new Sphere(new Vector3f(0, 0, 0), 1e30f);
		assertTrue(sphere.intersects(otherSphere));
	}

	@Test
	void testRaySkimmingAcrossSphereEdgeIntersecting() {
		Ray ray = new Ray(new Vector3f(-3, -3, 0), new Vector3f(1, 1, 0));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), (float) Math.sqrt(18));
		assertTrue(ray.intersects(sphere));
	}

	@Test
	void testExtremeNegativeAABBOverlap() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(-1e30f, -1e30f, -1e30f), new Vector3f(-1e20f, -1e20f, -1e20f));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(-1e25f, -1e25f, -1e25f), new Vector3f(0, 0, 0));
		assertTrue(box1.intersects(box2));
	}

	@Test
	void testRayOriginOnAABBPlane() {
		Ray ray = new Ray(new Vector3f(0, 3, 3), new Vector3f(1, 0, 0));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 6, 6));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testVeryLargeSphereWithSmallSphereInside() {
		Sphere largeSphere = new Sphere(new Vector3f(0, 0, 0), 1e6f);
		Sphere smallSphere = new Sphere(new Vector3f(1, 1, 1), 1);
		assertTrue(largeSphere.intersects(smallSphere));
	}

	@Test
	void testRayBarelyTouchingAABBSurface() {
		Ray ray = new Ray(new Vector3f(2, 6, 6), new Vector3f(-1, 0, 0));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
		assertFalse(ray.intersects(box));
	}

	@Test
	void testAABBsJustTouchingEdges() {
		AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
		AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(4, 0, 0), new Vector3f(6, 6, 6));
		assertTrue(box1.intersects(box2));
	}

	@Test
	void testSphereWithTinyRadiusInsideAABB() {
		Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 0.001f);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testAABBEncapsulatingSphere() {
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(-10, -10, -10), new Vector3f(10, 10, 10));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 5);
		assertTrue(box.intersects(sphere));
	}

	@Test
	void testRayAndSphereNoIntersection() {
		Ray ray = new Ray(new Vector3f(10, 10, 10), new Vector3f(1, 1, 0));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);
		assertFalse(ray.intersects(sphere));
	}

	@Test
	void testRayOriginAtAABBCorner() {
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		assertTrue(ray.intersects(box));
	}

	@Test
	void testDegenerateSphereInMinimalAABB() {
		Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 0);
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		assertTrue(sphere.intersects(box));
	}

	@Test
	void testRayAndNegativeSizedAABB() {
		Ray ray = new Ray(new Vector3f(-10, -10, -10), new Vector3f(1, 1, 1));
		AxisAlignedBox box = new AxisAlignedBox(new Vector3f(5, 5, 5), new Vector3f(4, 4, 4));
		assertFalse(ray.intersects(box));
	}
	
	
    @Test
    void testExtremeLargeAABBIntersectingSmallAABB() {
        AxisAlignedBox largeBox = new AxisAlignedBox(new Vector3f(-1e30f, -1e30f, -1e30f), new Vector3f(1e30f, 1e30f, 1e30f));
        AxisAlignedBox smallBox = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        assertTrue(largeBox.intersects(smallBox));
    }

    @Test
    void testZeroRadiusSphereTouchingEdgeAABB() {
        Sphere sphere = new Sphere(new Vector3f(2, 2, 2), 0.0f);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
        assertTrue(sphere.intersects(box));
    }

    @Test
    void testRayOriginEncapsulatedByAABBIntersecting() {
        Ray ray = new Ray(new Vector3f(1, 1, 1), new Vector3f(0, 0, -1));
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(3, 3, 3));
        assertTrue(ray.intersects(box));
    }

    @Test
    void testTiltedAABBInsideAnother() {
        AxisAlignedBox innerBox = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(3, 3, 5));
        AxisAlignedBox outerBox = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 10));
        assertTrue(outerBox.intersects(innerBox));
    }

    @Test
    void testFarRayTinyAABB() {
        Ray ray = new Ray(new Vector3f(1000, 1000, 1000), new Vector3f(-1, -1, -1));
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(1.01f, 1.01f, 1.01f));
        assertTrue(ray.intersects(box));
    }

    @Test
    void testSphereIntersectingLongThinAABB() {
        Sphere sphere = new Sphere(new Vector3f(5, 5, 5), 5);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(4, 4, 0), new Vector3f(6, 6, 100));
        assertTrue(sphere.intersects(box));
    }

    @Test
    void testPartiallyOverlappingTiltedAABBs() {
        AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(3, 7, 3));
        AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(2, 2, 2), new Vector3f(4, 6, 4));
        assertTrue(box1.intersects(box2));
    }

    @Test
    void testRayExitingSphereFromOppositeSide() {
        Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        Sphere sphere = new Sphere(new Vector3f(5, 5, 5), 8.5f);
        assertTrue(ray.intersects(sphere));
    }

    @Test
    void testDegenerateRayTouchingAABBCorner() {
        Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
        assertTrue(ray.intersects(box));
    }

    @Test
    void testMinimalSphereIntersectingLargeAABB() {
        Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 0.0001f);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertTrue(sphere.intersects(box));
    }

    @Test
    void testMinimalAABBInsideLargeSphere() {
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(1, 1, 1), new Vector3f(1.0001f, 1.0001f, 1.0001f));
        Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 10);
        assertTrue(box.intersects(sphere));
    }

    @Test
    void testTiltedAABBTangentialToSphere() {
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(9, 6, 6), new Vector3f(12, 7, 7));
        Sphere sphere = new Sphere(new Vector3f(10, 6.5f, 6.5f), 1);
        assertTrue(box.intersects(sphere));
    }

    @Test
    void testRaySkimmingOppositeAABBEdges() {
        Ray ray = new Ray(new Vector3f(-1, 2, 2), new Vector3f(1, 0, 0));
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(4, 4, 4));
        assertTrue(ray.intersects(box));
    }

    @Test
    void testRayPassingThroughLargeTiltedAABB() {
        Ray ray = new Ray(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5));
        assertTrue(ray.intersects(box));
    }

    @Test
    void testLargeSphereFullyEnvelopingSmallAABB() {
        Sphere sphere = new Sphere(new Vector3f(1, 1, 1), 10);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(2, 2, 2), new Vector3f(3, 3, 3));
        assertTrue(sphere.intersects(box));
    }

    @Test
    void testTiltedAABBsTouchingAtCorner() {
        AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 4, 6));
        AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(2, 4, 6), new Vector3f(3, 5, 7));
        assertTrue(box1.intersects(box2));
    }

    @Test
    void testRayIntersectingTwoAABBs() {
        Ray ray = new Ray(new Vector3f(-1, 1, 1), new Vector3f(1, 0, 0));
        AxisAlignedBox box1 = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(3, 3, 3));
        AxisAlignedBox box2 = new AxisAlignedBox(new Vector3f(4, 0, 0), new Vector3f(6, 3, 3));
        assertTrue(ray.intersects(box1) && ray.intersects(box2));
    }

    @Test
    void testInfiniteSphereIntersectingAABBs() {
        Sphere sphere = new Sphere(new Vector3f(0, 0, 0), Float.POSITIVE_INFINITY);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(10, 10, 10), new Vector3f(20, 20, 20));
        assertTrue(sphere.intersects(box));
    }
	
	
    @Test
    void testSphereEntirelyInsideAABB() {
        Sphere sphere = new Sphere(new Vector3f(3, 3, 3), 1);
        AxisAlignedBox box = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertTrue(sphere.intersects(box));
    }

    @Test
    void testAABBCompletelyInsideAnotherAABB() {
        AxisAlignedBox innerBox = new AxisAlignedBox(new Vector3f(2, 2, 2), new Vector3f(3, 3, 3));
        AxisAlignedBox outerBox = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertTrue(outerBox.intersects(innerBox));
    }

    @Test
    void testSphereCompletelyInsideAnotherSphere() {
        Sphere innerSphere = new Sphere(new Vector3f(0, 0, 0), 3);
        Sphere outerSphere = new Sphere(new Vector3f(0, 0, 0), 5);
        assertTrue(outerSphere.intersects(innerSphere));
    }

    @Test
    void testTiltedAABBCompletelyInsideAnother() {
        AxisAlignedBox innerBox = new AxisAlignedBox(new Vector3f(2, 3, 1), new Vector3f(4, 5, 3));
        AxisAlignedBox outerBox = new AxisAlignedBox(new Vector3f(0, 0, 0), new Vector3f(6, 6, 6));
        assertTrue(outerBox.intersects(innerBox));
    }
}
