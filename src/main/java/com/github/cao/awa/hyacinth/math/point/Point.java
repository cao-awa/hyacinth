package com.github.cao.awa.hyacinth.math.point;

public record Point(double x, double y, double angle) {
    public String toString() {
        return "[x=" + x + ", y=" + y + ", angle=" + angle + "]";
    }
}
