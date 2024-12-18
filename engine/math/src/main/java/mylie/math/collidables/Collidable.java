package mylie.math.collidables;

public abstract class Collidable {
	final int id;

	public Collidable(int id) {
		this.id = id;
	}

	public boolean collidesWith(Collidable other) {
		return CollisionSolver.colliding(this, other);
	}
}
