package mylie.scene;

import java.util.LinkedList;
import java.util.Queue;

public class Traverser {

	public static void traverse(Direction direction, Spatial start, Visitor visitor) {
		Queue<Spatial> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Spatial spatial = queue.poll();
			visitor.visit(spatial);
			direction.apply(queue, spatial);
		}
	}

	public interface Direction {
		Direction ToLeaf = (queue, spatial) -> {
			if (spatial instanceof Node node) {
				queue.addAll(node.children());
			}
		};
		Direction ToRoot = ((queue, spatial) -> {
			if (spatial.parent() != null) {
				queue.add(spatial.parent());
			}
		});

		void apply(Queue<Spatial> queue, Spatial spatial);
	}

	public interface Visitor {
		void visit(Spatial spatial);
	}
}
