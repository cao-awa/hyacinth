package com.github.cao.awa.hyacinth.math.axis;

import com.github.cao.awa.hyacinth.math.matrix.Matrix3f;
import com.github.zhuaidadaya.rikaishinikui.handler.entrust.EntrustParser;

import java.util.Arrays;

public enum AxisTransformation {
    P123(0, 1, 2),
    P213(1, 0, 2),
    P132(0, 2, 1),
    P231(1, 2, 0),
    P312(2, 0, 1),
    P321(2, 1, 0);

    private final int[] mappings;
    private final Matrix3f matrix;
    private static final int field_33113 = 3;
    private static final AxisTransformation[][] COMBINATIONS;

    AxisTransformation(int xMapping, int yMapping, int zMapping) {
        this.mappings = new int[]{xMapping, yMapping, zMapping};
        this.matrix = new Matrix3f();
        this.matrix.set(0, this.map(0), 1.0f);
        this.matrix.set(1, this.map(1), 1.0f);
        this.matrix.set(2, this.map(2), 1.0f);
    }

    public AxisTransformation prepend(AxisTransformation transformation) {
        return COMBINATIONS[this.ordinal()][transformation.ordinal()];
    }

    public int map(int oldAxis) {
        return this.mappings[oldAxis];
    }

    public Matrix3f getMatrix() {
        return this.matrix;
    }

    static {
        COMBINATIONS = EntrustParser.operation(new AxisTransformation[AxisTransformation.values().length][AxisTransformation.values().length], axisTransformations -> {
            for (AxisTransformation axisTransformation2 : AxisTransformation.values()) {
                for (AxisTransformation axisTransformation22 : AxisTransformation.values()) {
                    AxisTransformation i;
                    int[] is = new int[3];
                    for (int i2 = 0; i2 < 3; ++i2) {
                        is[i2] = axisTransformation2.mappings[axisTransformation22.mappings[i2]];
                    }
                    axisTransformations[axisTransformation2.ordinal()][axisTransformation22.ordinal()] = i = Arrays.stream(AxisTransformation.values()).filter(axisTransformation -> Arrays.equals(axisTransformation.mappings, is)).findFirst().get();
                }
            }
        });
    }
}

