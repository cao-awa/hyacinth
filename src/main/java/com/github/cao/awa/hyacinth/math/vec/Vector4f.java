package com.github.cao.awa.hyacinth.math.vec;

import com.github.cao.awa.hyacinth.math.Mathematics;
import com.github.cao.awa.hyacinth.math.matrix.Matrix4f;
import com.github.cao.awa.hyacinth.math.quaternion.Quaternion;

/**
 * @implNote Even though this is used only on the client, it accesses protected
 * fields from {@link Matrix4f}, which requires them to be in the same package.
 */
public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f() {
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vec3f vector) {
        this(vector.getX(), vector.getY(), vector.getZ(), 1.0f);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Vector4f vector4f = (Vector4f)o;
        if (Float.compare(vector4f.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(vector4f.y, this.y) != 0) {
            return false;
        }
        if (Float.compare(vector4f.z, this.z) != 0) {
            return false;
        }
        return Float.compare(vector4f.w, this.w) == 0;
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
        i = 31 * i + Float.floatToIntBits(this.w);
        return i;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getW() {
        return this.w;
    }

    public void multiply(float value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        this.w *= value;
    }

    public void multiplyComponentwise(Vec3f vector) {
        this.x *= vector.getX();
        this.y *= vector.getY();
        this.z *= vector.getZ();
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }

    public float dotProduct(Vector4f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public boolean normalize() {
        float f = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if ((double)f < 1.0E-5) {
            return false;
        }
        float g = Mathematics.fastInverseSqrt(f);
        this.x *= g;
        this.y *= g;
        this.z *= g;
        this.w *= g;
        return true;
    }

    public void transform(Matrix4f matrix) {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = this.w;
        this.x = matrix.a00 * f + matrix.a01 * g + matrix.a02 * h + matrix.a03 * i;
        this.y = matrix.a10 * f + matrix.a11 * g + matrix.a12 * h + matrix.a13 * i;
        this.z = matrix.a20 * f + matrix.a21 * g + matrix.a22 * h + matrix.a23 * i;
        this.w = matrix.a30 * f + matrix.a31 * g + matrix.a32 * h + matrix.a33 * i;
    }

    public void rotate(Quaternion rotation) {
        Quaternion quaternion = new Quaternion(rotation);
        quaternion.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0f));
        Quaternion quaternion2 = new Quaternion(rotation);
        quaternion2.conjugate();
        quaternion.hamiltonProduct(quaternion2);
        this.set(quaternion.getX(), quaternion.getY(), quaternion.getZ(), this.getW());
    }

    public void normalizeProjectiveCoordinates() {
        this.x /= this.w;
        this.y /= this.w;
        this.z /= this.w;
        this.w = 1.0f;
    }

    public void lerp(Vector4f to, float delta) {
        float f = 1.0f - delta;
        this.x = this.x * f + to.x * delta;
        this.y = this.y * f + to.y * delta;
        this.z = this.z * f + to.z * delta;
        this.w = this.w * f + to.w * delta;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}

