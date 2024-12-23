package mylie.graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mylie.core.Timer;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unused")
public class Datatypes {
    public static final PrimitiveDataType<Integer> Integer = new PrimitiveDataType<>("Integer",
            java.lang.Integer.BYTES, 1, ByteBuffer::getInt, (value, buffer) -> buffer.putInt(value));
    public static final PrimitiveDataType<Float> Float = new PrimitiveDataType<>("Float",
            java.lang.Float.BYTES, 1, ByteBuffer::getFloat, (value, buffer) -> buffer.putFloat(value));
    public static final PrimitiveDataType<Matrix4fc> Matrix4f =
            new PrimitiveDataType<>("Matrix4f",Float.size, 16, null, Matrix4fc::get);
    public static final PrimitiveDataType<Matrix3fc> Matrix3f =
            new PrimitiveDataType<>("Matrix3f",Float.size, 9, null, Matrix3fc::get);
    public static final PrimitiveDataType<Matrix2fc> Matrix2f =
            new PrimitiveDataType<>("Matrix2f",Float.size, 4, null, Matrix2fc::get);
    public static final PrimitiveDataType<Vector4fc> Vector4f =
            new PrimitiveDataType<>("Vector4f",Float.size, 4, org.joml.Vector4f::new, Vector4fc::get);
    public static final PrimitiveDataType<Vector3fc> Vector3f =
            new PrimitiveDataType<>("Vector3f",Float.size, 3, org.joml.Vector3f::new, Vector3fc::get);
    public static final PrimitiveDataType<Vector2fc> Vector2f =
            new PrimitiveDataType<>("Vector2f",Float.size, 2, org.joml.Vector2f::new, Vector2fc::get);
    public static final PrimitiveDataType<Vector3ic> Vector3i =
            new PrimitiveDataType<>("Vector3f",Integer.size, 3, org.joml.Vector3i::new, Vector3ic::get);
    public static final PrimitiveDataType<Vector2ic> Vector2i =
            new PrimitiveDataType<>("Vector2i",Integer.size, 2, org.joml.Vector2i::new, Vector2ic::get);

    @AllArgsConstructor
    @Getter
    public static class GpuDataType<T> {
        final String name;
        final int size;
        final BufferReader<T> reader;
        final BufferWriter<T> writer;
    }

    @Getter
    public static class PrimitiveDataType<T> extends GpuDataType<T> {
        final int componentByteSize;
        final int components;

        public PrimitiveDataType(
                String name,int componentByteSize, int components, BufferReader<T> reader, BufferWriter<T> writer) {
            super(name,componentByteSize * components, reader, writer);
            this.componentByteSize = componentByteSize;
            this.components = components;
        }

        @Override
        public String toString() {
            return "Datatype<" + name + ">";
        }
    }

    public static class StructDataType<T extends Struct> extends GpuDataType<T> {

        public StructDataType(Class<T> structClass) {
            super(null,0,null,null);

        }
    }

    public interface Struct {}

    public interface DataBuffer<T> extends Iterable<T>, BufferReader<DataBuffer<T>>, BufferWriter<DataBuffer<T>> {
        void set(int index, T value);

        T get(int index);

        int count();

        int size();

        long version();
    }

    public static class ListDataBuffer<T> implements DataBuffer<T> {
        final GpuDataType<T> dataType;
        private final ArrayList<T> data;
        @Getter
        private long version = -1;
        public ListDataBuffer(GpuDataType<T> dataType) {
            this.dataType = dataType;
            data = new ArrayList<>();
        }

        @Override
        public void set(int index, T value) {
            if(index >= data.size()){
                for(int i = data.size(); i <= index; i++){
                    data.add(value);
                }
            }
            data.set(index, value);
            version= Timer.time.version();
        }

        @Override
        public T get(int index) {
            return data.get(index);
        }

        @Override
        public int count() {
            return data.size();
        }

        @Override
        public int size() {
            return dataType.size * count();
        }

        @NotNull @Override
        public Iterator<T> iterator() {
            return data.iterator();
        }

        @Override
        public DataBuffer<T> readFromBuffer(ByteBuffer buffer) {
            int bufferIndex = buffer.position();
            int index = 0;
            while (buffer.limit() >= bufferIndex + dataType.size) {
                data.set(index, dataType.reader.readFromBuffer(buffer));
                buffer.position(bufferIndex + dataType.size);
                index++;
            }
            return this;
        }

        @Override
        public void writeToBuffer(DataBuffer<T> value, ByteBuffer buffer) {
            int bufferIndex = buffer.position();
            for (int i = 0; i < data.size(); i++) {
                dataType.writer.writeToBuffer(data.get(i), buffer);
                buffer.position(bufferIndex + i * dataType.size);
            }
        }

        @Override
        public String toString() {
            return "ListData[type=%s,elements=%d,size=%d]".formatted(dataType.name, data.size(), size());
        }
    }

    public static class ArrayDataBuffer<T> implements DataBuffer<T> {
        final GpuDataType<T> dataType;
        T[] data;
        @Getter
        private long version = -1;
        @SuppressWarnings("unchecked")
        public ArrayDataBuffer(GpuDataType<T> dataType, int size) {
            this.dataType = dataType;
            data = (T[]) new Object[size];
        }

        @Override
        public void set(int index, T value) {
            data[index] = value;
            version= Timer.time.version();
        }

        @Override
        public T get(int index) {
            return data[index];
        }

        @Override
        public int count() {
            return data.length;
        }

        @Override
        public int size() {
            return dataType.size * count();
        }

        @NotNull @Override
        public Iterator<T> iterator() {
            return new Iterator<>() {
                int index = 0;

                @Override
                public boolean hasNext() {
                    return index != data.length;
                }

                @Override
                public T next() {
                    return data[index++];
                }
            };
        }

        @Override
        public DataBuffer<T> readFromBuffer(ByteBuffer buffer) {
            int bufferIndex = buffer.position();
            int index = 0;
            while (buffer.limit() >= bufferIndex + dataType.size) {
                data[index] = dataType.reader.readFromBuffer(buffer);
                buffer.position(bufferIndex + dataType.size);
                index++;
            }
            return this;
        }

        @Override
        public void writeToBuffer(DataBuffer<T> dataBuffer, ByteBuffer buffer) {
            int bufferIndex = buffer.position();
            for (int i = 0; i < data.length; i++) {
                dataType.writer.writeToBuffer(data[i], buffer);
                buffer.position(bufferIndex + i * dataType.size);
            }
        }

        @Override
        public String toString() {
            return "ArrayData[type=%s,elements=%d,size=%d]".formatted(dataType.name, data.length, size());
        }
    }

    public interface BufferWriter<T> {
        void writeToBuffer(T value, ByteBuffer buffer);
    }

    public interface BufferReader<T> {
        T readFromBuffer(ByteBuffer buffer);
    }
}
