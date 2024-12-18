package mylie.math.collidables;

import java.util.function.BiFunction;
import org.joml.Intersectionf;

public class CollisionSolver {
	private static final BiFunction<Collidable, Collidable, Boolean>[][] collisionSolvers = new BiFunction[2][2];

	static {
		collisionSolvers[0][0] = CollisionSolver::AabAab;
		collisionSolvers[0][1] = CollisionSolver::AabSphere;
		collisionSolvers[1][0] = CollisionSolver::SphereAab;
		collisionSolvers[1][1] = CollisionSolver::SphereSphere;
	}

	public static boolean colliding(Collidable collidable, Collidable other) {
		return false;
	}

	private static boolean AabAab(Collidable collidable1, Collidable collidable2) {
		AxisAlignedBox aab1 = (AxisAlignedBox) collidable1;
		AxisAlignedBox aab2 = (AxisAlignedBox) collidable2;
		return Intersectionf.testAabAab(aab1.min(), aab1.max(), aab2.min(), aab2.max());
	}

	private static boolean AabSphere(Collidable collidable1, Collidable collidable2) {
		AxisAlignedBox aab = (AxisAlignedBox) collidable1;
		Sphere sphere = (Sphere) collidable2;
		return Intersectionf.testAabSphere(aab.min(), aab.max(), sphere.center, sphere.radius);
	}

	private static boolean SphereAab(Collidable collidable1, Collidable collidable2) {
		return AabSphere(collidable2, collidable1);
	}

	private static boolean SphereSphere(Collidable collidable1, Collidable collidable2) {
		Sphere sphere1 = (Sphere) collidable1;
		Sphere sphere2 = (Sphere) collidable2;
		return Intersectionf.testSphereSphere(sphere1.center, sphere1.radius, sphere2.center, sphere2.radius);
	}
}
