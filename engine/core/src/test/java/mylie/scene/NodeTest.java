package mylie.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mylie.math.Constants;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

class ScenegraphTests {

	/**
	 * Test translation implementation for `Node` and its children. This verifies if
	 * setting local transformations properly impacts the objects in the scene
	 * hierarchy.
	 */

	@Test
	void testTranslateUpdatesNodePosition() {
		Node node = new Node();
		node.translate(Constants.UnitX);
		assertEquals(Constants.UnitX, node.worldPosition());
	}

	@Test
	void testCompositeTransformationsOnNode() {
		Node node = new Node();
		node.rotateDeg(-90, Constants.UnitY);
		node.scale(2);
		Node child = new Node();
		node.child(child);
		child.translate(Constants.UnitX);
		assertEquals(Constants.UnitZ.mul(2, new Vector3f()), child.worldPosition());
	}

	@Test
	void testScalingEffectOnTranslatedChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.child(child);
		child.translate(Constants.UnitX);
		parent.scale(3);
		assertEquals(Constants.UnitX.mul(3, new Vector3f()), child.worldPosition());
	}

	@Test
	void testNestedChildHierarchyTransformations() {
		Node root = new Node();
		Node child = new Node();
		Node grandchild = new Node();
		root.child(child);
		child.child(grandchild);
		root.translate(Constants.UnitX);
		child.translate(Constants.UnitY);
		grandchild.translate(Constants.UnitZ);
		assertEquals(new Vector3f(1, 1, 1), grandchild.worldPosition());
	}

	@Test
	void testTranslationDoesNotAffectNodeScale() {
		Node parent = new Node();
		Node child = new Node();
		parent.child(child);
		parent.translate(Constants.UnitX);
		assertEquals(Constants.One, parent.worldScale());
		assertEquals(Constants.One, child.worldScale());
	}

	@Test
	void testTranslateUpdatesChildrenPosition() {
		Node node = new Node();
		Node child = new Node();
		node.child(child);
		node.translate(Constants.UnitX);
		assertEquals(Constants.UnitX, child.worldPosition());
	}

	@Test
	void testTranslateUpdatesChildrenPosition2() {
		Node node = new Node();
		Node child = new Node();
		node.child(child);
		node.translate(Constants.One);
		assertEquals(Constants.One, child.worldPosition());
	}

	@Test
	void testScaleUpdatesNodeScale() {
		Node node = new Node();
		node.scale(1);
		assertEquals(node.worldScale(), Constants.One);
		node.scale(2);
		assertEquals(Constants.One.mul(2, new Vector3f()), node.worldScale());
	}

	@Test
	void testScaleUpdatesChildrenScale() {
		Node node = new Node();
		Node child = new Node();
		node.child(child);
		node.scale(1);
		assertEquals(child.worldScale(), Constants.One);
		node.scale(2);
		assertEquals(Constants.One.mul(2, new Vector3f()), child.worldScale());
	}

	@Test
	void testScaleUpdatesChildrenScale2() {
		Node node = new Node();
		Node child = new Node();
		node.child(child);
		node.scale(2);
		child.translate(Constants.UnitX);
		assertEquals(Constants.UnitX.mul(2, new Vector3f()), child.worldPosition());
	}

	@Test
	void testRotateUpdatesNodeRotation() {
		Node node = new Node();
		Node child = new Node();
		node.child(child);
		child.translate(Constants.UnitX);
		node.rotateDeg(-90, Constants.UnitY);
		assertEquals(Constants.UnitZ, child.worldPosition());
	}

}
