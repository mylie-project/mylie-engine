package mylie.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

@Getter(AccessLevel.PACKAGE)
public class Spatial {
	private final static int WorldTransformChanged = 1 << 0;
	private final static int WorldBoundsChanged = 1 << 1;
	@Setter(AccessLevel.PACKAGE)
	private Node parent;
	private final Transform worldTransform = new Transform();
	private final Transform localTransform = new Transform();
	private int flags;

	void onLocalTransformChanged() {
		localTransform().onChange();
		Traverser.traverse(Traverser.Direction.ToLeaf, this,
				s -> s.setFlag(WorldBoundsChanged | WorldTransformChanged));
		onWorldBoundsChanged();
	}

	void onWorldBoundsChanged() {
		Traverser.traverse(Traverser.Direction.ToRoot, this, s -> s.setFlag(WorldBoundsChanged));
	}

	Transform worldTransform() {
		if (flag(WorldTransformChanged)) {
			if (parent() != null) {
				worldTransform.update(localTransform, parent().worldTransform());
			} else {
				worldTransform.set(localTransform);
			}
			clearFlag(WorldTransformChanged);
		}
		return worldTransform;
	}

	Vector3fc worldPosition() {
		return worldTransform().position();
	}

	Vector3fc worldScale() {
		return worldTransform().scale();
	}

	Quaternionfc worldRotation() {
		return worldTransform().rotation();
	}

	private void setFlag(int flag) {
		flags |= flag;
	}

	private void clearFlag(int flag) {
		flags &= ~flag;
	}

	private boolean flag(int flag) {
		return (flags & flag) == flag;
	}
}
