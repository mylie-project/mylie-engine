package mylie.lwjgl3.glfw;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.system.MemoryUtil.memSlice;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOUtil {
	private IOUtil() {
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource
	 *            the resource to read
	 * @param bufferSize
	 *            the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		Path path = resource.startsWith("http") ? null : Paths.get(resource);
		if (path != null && Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = createByteBuffer((int) fc.size() + 1);
				// noinspection StatementWithEmptyBody
				while (fc.read(buffer) != -1) {
				}
			}
		} else {
			try (InputStream source = resource.startsWith("http")
					? new URI(resource).toURL().openStream()
					: IOUtil.class.getClassLoader().getResourceAsStream(resource)) {
				assert source != null;
				try (ReadableByteChannel rbc = Channels.newChannel(source)) {
					buffer = createByteBuffer(bufferSize);

					while (true) {
						int bytes = rbc.read(buffer);
						if (bytes == -1) {
							break;
						}
						if (buffer.remaining() == 0) {
							buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
						}
					}
				}
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		buffer.flip();
		return memSlice(buffer);
	}
}
