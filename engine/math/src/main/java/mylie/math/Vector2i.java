package mylie.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.joml.Vector2dc;
import org.joml.Vector2fc;

public class Vector2i extends org.joml.Vector2i implements Vector2ic {
    public Vector2i() {}

    public Vector2i(int s) {
        super(s);
    }

    public Vector2i(int x, int y) {
        super(x, y);
    }

    public Vector2i(float x, float y, int mode) {
        super(x, y, mode);
    }

    public Vector2i(double x, double y, int mode) {
        super(x, y, mode);
    }

    public Vector2i(org.joml.Vector2ic v) {
        super(v);
    }

    public Vector2i(Vector2fc v, int mode) {
        super(v, mode);
    }

    public Vector2i(Vector2dc v, int mode) {
        super(v, mode);
    }

    public Vector2i(int[] xy) {
        super(xy);
    }

    public Vector2i(ByteBuffer buffer) {
        super(buffer);
    }

    public Vector2i(int index, ByteBuffer buffer) {
        super(index, buffer);
    }

    public Vector2i(IntBuffer buffer) {
        super(buffer);
    }

    public Vector2i(int index, IntBuffer buffer) {
        super(index, buffer);
    }
}
