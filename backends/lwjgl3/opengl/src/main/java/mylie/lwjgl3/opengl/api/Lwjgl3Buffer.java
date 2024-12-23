package mylie.lwjgl3.opengl.api;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import mylie.graphics.Datatypes;
import mylie.graphics.NativeData;
import mylie.graphics.opengl.api.GlBuffer;
import mylie.graphics.opengl.managers.GlBufferManager;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

@Slf4j
public class Lwjgl3Buffer implements GlBuffer {

	@Override
	public void createBuffer(NativeData.SharedData.Handle handle) {
		handle.handle(GL20.glGenBuffers());
		log.trace("createBuffer({})", handle.handle());
	}

	@Override
	public boolean bindBuffer(NativeData.SharedData.Handle handle, GlBufferManager.Target target) {
		boolean bind = GlBuffer.super.bindBuffer(handle, target);
		if (bind) {
			log.trace("bindBuffer({}, {})", handle.handle(), target.name());
			GL20.glBindBuffer(target.bindingPoint(), handle.handle());
		}
		return bind;
	}

	@Override
	public void bufferData(Datatypes.DataBuffer<?> buffer, GlBufferManager.Target target,
			GlBufferManager.AccessMode accessMode) {
		log.trace("bufferData({}, {}, {})", buffer, target.name(), accessMode.name());
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer nativeBuffer = stack.malloc(buffer.size());
			buffer.writeToBuffer(null, nativeBuffer);
			nativeBuffer.rewind();
			GL20.glBufferData(target.bindingPoint(), nativeBuffer, accessMode.mode());
		}

	}

	@Override
	public void updateBufferData(Datatypes.DataBuffer<?> buffer, GlBufferManager.Target target,
			GlBufferManager.AccessMode accessMode) {
		log.trace("updateBufferData({}, {}, {})", buffer, target.name(), accessMode.name());
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer nativeBuffer = stack.calloc(buffer.size());
			buffer.writeToBuffer(null, nativeBuffer);
			nativeBuffer.rewind();
			GL20.glBufferSubData(target.bindingPoint(), 0, nativeBuffer);
		}
	}

	@Override
	public void deleteBuffer(NativeData.SharedData.Handle handle) {
		log.trace("deleteBuffer({})", handle.handle());
		GL20.glDeleteBuffers(handle.handle());
	}
}
