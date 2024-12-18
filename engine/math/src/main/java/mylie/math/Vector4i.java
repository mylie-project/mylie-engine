package mylie.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.joml.*;
import org.joml.Vector2ic;
import org.joml.Vector3fc;

public class Vector4i extends org.joml.Vector4i implements Vector4ic {
    public Vector4i() {}

    public Vector4i(org.joml.Vector4ic v) {
        super(v);
    }

    public Vector4i(Vector3ic v, int w) {
        super(v, w);
    }

    public Vector4i(Vector2ic v, int z, int w) {
        super(v, z, w);
    }

    public Vector4i(Vector3fc v, float w, int mode) {
        super(v, w, mode);
    }

    public Vector4i(Vector4fc v, int mode) {
        super(v, mode);
    }

    public Vector4i(Vector4dc v, int mode) {
        super(v, mode);
    }

    public Vector4i(int s) {
        super(s);
    }

    public Vector4i(int x, int y, int z, int w) {
        super(x, y, z, w);
    }

    public Vector4i(int[] xyzw) {
        super(xyzw);
    }

    public Vector4i(ByteBuffer buffer) {
        super(buffer);
    }

    public Vector4i(int index, ByteBuffer buffer) {
        super(index, buffer);
    }

    public Vector4i(IntBuffer buffer) {
        super(buffer);
    }

    public Vector4i(int index, IntBuffer buffer) {
        super(index, buffer);
    }
}
