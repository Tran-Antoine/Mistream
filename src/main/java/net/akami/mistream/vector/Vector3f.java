package net.akami.mistream.vector;

import com.google.flatbuffers.FlatBufferBuilder;

/**
 * A simple 3d vector class with the most essential operations.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vector3f extends rlbot.vector.Vector3 {

    public static final Vector3f ZERO = new Vector3f();

    public Vector3f(double x, double y, double z) {
        super((float) x, (float) y, (float) z);
    }

    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f(rlbot.flat.Vector3 vec) {
        // Invert the X value so that the axes make more sense.
        this(-vec.x(), vec.y(), vec.z());
    }

    public int toFlatbuffer(FlatBufferBuilder builder) {
        // Invert the X value again so that rlbot sees the format it expects.
        return rlbot.flat.Vector3.createVector3(builder, -x, y, z);
    }

    public Vector3f plus(Vector3f other) {
        return new Vector3f(x + other.x, y + other.y, z + other.z);
    }

    public Vector3f minus(Vector3f other) {
        return new Vector3f(x - other.x, y - other.y, z - other.z);
    }

    public Vector3f scaled(double scale) {
        return new Vector3f(x * scale, y * scale, z * scale);
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vector3f scaledToMagnitude(double magnitude) {
        if (isZero()) {
            throw new IllegalStateException("Cannot scale up a vector with length zero!");
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }

    public double distance(Vector3f other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        double zDiff = z - other.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3f normalized() {

        if (isZero()) {
            throw new IllegalStateException("Cannot normalize a vector with length zero!");
        }
        return this.scaled(1 / magnitude());
    }

    public double dot(Vector3f other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public Vector2f flatten() {
        return new Vector2f(x, y);
    }

    public double angle(Vector3f v) {
        double mag2 = magnitudeSquared();
        double vmag2 = v.magnitudeSquared();
        double dot = dot(v);
        return Math.acos(dot / Math.sqrt(mag2 * vmag2));
    }

    public Vector3f crossProduct(Vector3f v) {
        double tx = y * v.z - z * v.y;
        double ty = z * v.x - x * v.z;
        double tz = x * v.y - y * v.x;
        return new Vector3f(tx, ty, tz);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector3f) {
            Vector3f other = (Vector3f) obj;
            return x == other.x && y == other.y && z == other.z;
        }
        return false;
    }

    public boolean absoluteEquals(Vector3f o) {
        return Math.abs(x) == Math.abs(o.x) && Math.abs(y) == Math.abs(o.y) && Math.abs(z) == Math.abs(o.z);
    }

    public boolean approximatelyEquals(Vector3f end, int error) {
        return Math.abs(this.distance(end)) < error;
    }
}
