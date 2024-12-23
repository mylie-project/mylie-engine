package mylie.graphics.opengl.managers;

import mylie.graphics.*;
import mylie.graphics.opengl.api.GlBuffer;

public class GlBufferManager implements ApiManager {
	GlBuffer glBuffer;
	private final NativeData.SharedData<Datatypes.DataBuffer<?>, BufferHandle> bufferHandles = new NativeData.SharedData<>(
			BufferHandle::new);
	@Override
	public boolean isSupported(GraphicsContext context) {
		glBuffer = api(GlBuffer.class, context);
		return glBuffer != null;
	}

	public boolean bindBuffer(RenderTask renderTask, Datatypes.DataBuffer<?> buffer, Target target,
			AccessMode accessMode) {
		boolean newBuffer = false;
		final BufferHandle handle = bufferHandles.get(buffer);
		if (!handle.initialized()) {
			handle.initialized(true);
			renderTask.subTask(() -> glBuffer.createBuffer(handle));
			newBuffer = true;
		}
		renderTask.subTask(() -> glBuffer.bindBuffer(handle, target));
		if (handle.version() < buffer.version()) {
			if (handle.size == -1) {
				renderTask.subTask(() -> glBuffer.bufferData(buffer, target, accessMode));
				handle.size = buffer.size();
				handle.version(buffer.version());
			} else {
				if (handle.size == buffer.size()) {
					renderTask.subTask(() -> glBuffer.updateBufferData(buffer, target, accessMode));
				} else {
					if (handle.size < buffer.size()) {
						renderTask.subTask(() -> glBuffer.bufferData(buffer, target, accessMode));
						handle.size = buffer.size();
						newBuffer = true;
					}
				}
			}
		}
		return newBuffer;
	}

	protected boolean checkBindBuffer(RenderTask renderTask, Datatypes.DataBuffer<?> buffer, Target target,
			AccessMode accessMode) {
		if (!checkBuffer(buffer)) {
			return bindBuffer(renderTask, buffer, target, accessMode);
		}
		return false;
	}

	protected boolean checkBuffer(Datatypes.DataBuffer<?> buffer) {
		BufferHandle handle = bufferHandles.get(buffer);
		return handle != null && handle.version() == buffer.version();
	}

	private static class BufferHandle extends NativeData.SharedData.Handle {
		private int size = -1;
	}

	public record Target(String name, int bindingPoint) {
		public static final Target Array = new Target("ArrayBuffer", 34962);
		public static final Target ElementArray = new Target("ElementArrayBuffer", 34963);
		public static final Target CopyRead = new Target("CopyReadBuffer", 36662);
		public static final Target CopyWrite = new Target("CopyWriteBuffer", 36663);
		public static final Target DispatchIndirect = new Target("DispatchIndirectBuffer", 37102);
		public static final Target DrawIndirect = new Target("DrawIndirectBuffer", 36671);
		public static final Target PixelPack = new Target("PixelPackBuffer", 35051);
		public static final Target PixelUnpack = new Target("PixelUnpackBuffer", 35052);
		public static final Target TransformFeedback = new Target("TransformFeedbackBuffer", 36386);
		public static final Target Uniform = new Target("UniformBuffer", 35345);
		public static final Target Texture = new Target("TextureBuffer", 35882);
		public static final Target ShaderStorage = new Target("ShaderStorageBuffer", 37074);
		public static final Target AtomicCounter = new Target("AtomicCounterBuffer", 37568);
	}

	public record AccessMode(String name, int mode) {
		public static final AccessMode StaticDraw = new AccessMode("StaticDraw", 35044);
		public static final AccessMode StaticRead = new AccessMode("StaticRead", 35045);
		public static final AccessMode StaticCopy = new AccessMode("StaticCopy", 35046);
		public static final AccessMode DynamicDraw = new AccessMode("DynamicDraw", 35048);
		public static final AccessMode DynamicRead = new AccessMode("DynamicRead", 35049);
		public static final AccessMode DynamicCopy = new AccessMode("DynamicCopy", 35050);
		public static final AccessMode StreamDraw = new AccessMode("StreamDraw", 35040);
		public static final AccessMode StreamRead = new AccessMode("StreamRead", 35041);
		public static final AccessMode StreamCopy = new AccessMode("StreamCopy", 35042);
	}
}
