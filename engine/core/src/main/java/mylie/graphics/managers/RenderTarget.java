package mylie.graphics.managers;

import lombok.Getter;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class RenderTarget {
	public static final RenderTarget Output = new RenderTarget();

	public enum BindingMode {
		Read, Write, ReadWrite
	}

	@Getter
	public static class ClearOperation {
		public static ClearOperation Default = new ClearOperation(Type.Color, Type.Depth, Type.Stencil);

		public enum Type {
			Color, Depth, Stencil
		}

		Vector4fc color = new Vector4f(0, 0, 0, 0);
		int stencilValue = 0;
		Type[] type;

		public ClearOperation(Type... type) {
			this.type = type;
		}

		public ClearOperation(Vector4fc color, Type... type) {
			this(type);
			this.color = color;
		}

		public ClearOperation(int stencilValue, Type... type) {
			this(type);
			this.stencilValue = stencilValue;
		}

		public ClearOperation(Vector4fc color, int stencilValue, Type... type) {
			this(type);
			this.color = color;
			this.stencilValue = stencilValue;
		}
	}
}
