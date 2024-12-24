package mylie.scene;

import lombok.Getter;
import mylie.core.Timer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Getter
public class Transform {
	Vector3f position = new Vector3f();
	Quaternionf rotation = new Quaternionf();
	Vector3f scale = new Vector3f(1);
	long version;

	void update(Transform localTransform, Transform parentWorldTransform) {
		parentWorldTransform.rotation.mul(localTransform.rotation, this.rotation);
		parentWorldTransform.scale.mul(localTransform.scale, this.scale);
		parentWorldTransform.scale.mul(localTransform.position, this.position);
		parentWorldTransform.rotation.transform(this.position, this.position);
		this.position.add(parentWorldTransform.position);
		onChange();
	}

	void set(Transform localTransform) {
		position.set(localTransform.position);
		rotation.set(localTransform.rotation);
		scale.set(localTransform.scale);
		onChange();
	}

	void onChange() {
		version = Timer.time.version();
	}
}
