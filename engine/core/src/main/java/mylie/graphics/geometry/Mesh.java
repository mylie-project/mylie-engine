package mylie.graphics.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.graphics.Datatypes;

@Slf4j
public class Mesh {
	final VertexDataLayouts.VertexDataLayout vertexDataLayout;
	Data data = new Data();
	final Datatypes.DataBuffer<Integer> indices;
	final List<Lod> lodLevels = new ArrayList<>();;

	private Mesh(VertexDataLayouts.VertexDataLayout vertexDataLayout) {
		this.vertexDataLayout = vertexDataLayout;
		this.indices = new Datatypes.ListDataBuffer<>(Datatypes.Integer);
	}

	public Mesh(VertexDataLayouts.VertexDataLayout vertexDataLayout, int size) {
		this(vertexDataLayout);
		for (VertexDataPoints.VertexDataPoint<?> dataPoint : vertexDataLayout.dataPoints()) {
			data.vertexDataBuffers.put(dataPoint, new Datatypes.ArrayDataBuffer<>(dataPoint.dataType(), size));
		}
	}

	@SuppressWarnings("unchecked")
	public final <T> void vertexData(VertexDataPoints.VertexDataPoint<T> point, int index, T data) {
		Datatypes.DataBuffer<T> dataBuffer = (Datatypes.DataBuffer<T>) this.data.vertexDataBuffers.get(point);
		dataBuffer.set(index, data);
	}

	@SafeVarargs
	public final <T> void vertexData(VertexDataPoints.VertexDataPoint<T> point, T... data) {
		for (int i = 0; i < data.length; i++) {
			vertexData(point, i, data[i]);
		}
	}

	@SuppressWarnings("unchecked")
	public final <T> T vertexData(VertexDataPoints.VertexDataPoint<T> point, int index) {
		Datatypes.DataBuffer<T> dataBuffer = (Datatypes.DataBuffer<T>) this.data.vertexDataBuffers.get(point);
		return dataBuffer.get(index);
	}

	public final void lod(Lod lod) {
		lodLevels.add(lod);
		lod.offset = indices.count();
		lod.count = lod.indices.size();
		int count = 0;
		for (Integer index : lod.indices) {
			indices.set(lod.offset + count, index);
			count++;
		}
	}

	public long version() {
		long version = -1;
		for (Datatypes.DataBuffer<?> value : data.vertexDataBuffers.values()) {
			version = Math.max(version, value.version());
		}
		return Math.max(version, indices.version());
	}

	@SuppressWarnings("unchecked")
	<T> Datatypes.DataBuffer<T> vertexDataBuffer(VertexDataPoints.VertexDataPoint<T> point) {
		return (Datatypes.DataBuffer<T>) data.vertexDataBuffers.get(point);
	}

	public Lod lod(int i) {
		return lodLevels.get(i);
	}

	private static class Data {
		final Map<VertexDataPoints.VertexDataPoint<?>, Datatypes.DataBuffer<?>> vertexDataBuffers = new HashMap<>();
		final Datatypes.DataBuffer<Integer> indices = new Datatypes.ListDataBuffer<>(Datatypes.Integer);
	}

	@SuppressWarnings("unused")
	@Getter
	public static class Lod {
		public enum RenderMode {
			Triangles, TriangleStrip, TriangleFan, TriangleAdjacency, TriangleStripAdjacency, Lines, LineStrip, LineLoop, LineStripAdjacency, LinesAdjacency, Points, Patches
		}

		int offset, count;
		RenderMode renderMode;
		int patchCount;
		List<Integer> indices;

		public Lod(RenderMode renderMode, int patchCount) {
			indices = new ArrayList<>();
			this.renderMode = renderMode;
			this.patchCount = patchCount;
		}

		public Lod(RenderMode renderMode, List<Integer> indices) {
			this(renderMode, 0);
			this.indices.addAll(indices);
		}

		public Lod(RenderMode renderMode, int[] indices) {
			this(renderMode, 0);
			for (int i : indices) {
				this.indices.add(i);
			}
		}

		public Lod(RenderMode renderMode) {
			this(renderMode, 0);
		}
	}
}
