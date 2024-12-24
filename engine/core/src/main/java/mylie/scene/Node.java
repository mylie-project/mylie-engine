package mylie.scene;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public class Node extends Spatial
		implements
			SpatialActions.Translatable,
			SpatialActions.UniformScaleable,
			SpatialActions.Rotatable {
	private List<Spatial> children = new ArrayList<>();

	public void child(Spatial spatial) {
		children.add(spatial);
		spatial.parent(this);
		spatial.onLocalTransformChanged();
	}

	public void removeChild(Spatial spatial) {
		children.remove(spatial);
		spatial.parent(null);
		onWorldBoundsChanged();
	}

}
