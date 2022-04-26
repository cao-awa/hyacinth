package com.github.cao.awa.hyacinth.math.direction;

import com.github.cao.awa.hyacinth.math.Mathematics;
import com.github.cao.awa.hyacinth.math.matrix.Matrix4f;
import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.quaternion.Quaternion;
import com.github.cao.awa.hyacinth.math.vec.Vec3f;
import com.github.cao.awa.hyacinth.math.vec.Vec3i;
import com.github.cao.awa.hyacinth.math.vec.Vector4f;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustParser;
import com.google.common.collect.Iterators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.cao.awa.hyacinth.math.direction.Direction.Axis.*;

public enum Direction implements StringIdentifiable {
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Y, new Vec3i(0, -1, 0)),
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Y, new Vec3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Z, new Vec3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Z, new Vec3i(0, 0, 1)),
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, X, new Vec3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, X, new Vec3i(1, 0, 0));

    public static final Codec<Direction> CODEC;
    public static final Codec<Direction> VERTICAL_CODEC;
    private final int id;
    private final int idOpposite;
    private final int idHorizontal;
    private final String name;
    private final Axis axis;
    private final AxisDirection direction;
    private final Vec3i vector;
    private static final Direction[] ALL;
    private static final Map<String, Direction> NAME_MAP;
    private static final Direction[] VALUES;
    private static final Direction[] HORIZONTAL;
    private static final Long2ObjectMap<Direction> VECTOR_TO_DIRECTION;

    Direction(int id, int idOpposite, int idHorizontal, String name, AxisDirection direction, Axis axis, Vec3i vector) {
        this.id = id;
        this.idHorizontal = idHorizontal;
        this.idOpposite = idOpposite;
        this.name = name;
        this.axis = axis;
        this.direction = direction;
        this.vector = vector;
    }

//    public static Direction[] getEntityFacingOrder(Entity entity) {
//        Direction direction3;
//        float f = entity.getPitch(1.0f) * ((float)Math.PI / 180);
//        float g = -entity.getYaw(1.0f) * ((float)Math.PI / 180);
//        float h = Mathematics.sin(f);
//        float i = Mathematics.cos(f);
//        float j = Mathematics.sin(g);
//        float k = Mathematics.cos(g);
//        boolean bl = j > 0.0f;
//        boolean bl2 = h < 0.0f;
//        boolean bl3 = k > 0.0f;
//        float l = bl ? j : -j;
//        float m = bl2 ? -h : h;
//        float n = bl3 ? k : -k;
//        float o = l * i;
//        float p = n * i;
//        Direction direction = bl ? EAST : WEST;
//        Direction direction2 = bl2 ? UP : DOWN;
//        Direction direction4 = direction3 = bl3 ? SOUTH : NORTH;
//        if (l > n) {
//            if (m > o) {
//                return Direction.listClosest(direction2, direction, direction3);
//            }
//            if (p > m) {
//                return Direction.listClosest(direction, direction3, direction2);
//            }
//            return Direction.listClosest(direction, direction2, direction3);
//        }
//        if (m > p) {
//            return Direction.listClosest(direction2, direction3, direction);
//        }
//        if (o > m) {
//            return Direction.listClosest(direction3, direction, direction2);
//        }
//        return Direction.listClosest(direction3, direction2, direction);
//    }

    /**
     * Helper function that returns the 3 directions given, followed by the 3 opposite given in opposite order.
     */
    private static Direction[] listClosest(Direction first, Direction second, Direction third) {
        return new Direction[]{first, second, third, third.getOpposite(), second.getOpposite(), first.getOpposite()};
    }

    public static Direction transform(Matrix4f matrix, Direction direction) {
        Vec3i vec3i = direction.getVector();
        Vector4f vector4f = new Vector4f(vec3i.getX(), vec3i.getY(), vec3i.getZ(), 0.0f);
        vector4f.transform(matrix);
        return Direction.getFacing(vector4f.getX(), vector4f.getY(), vector4f.getZ());
    }

    public Quaternion getRotationQuaternion() {
        Quaternion quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f);
        return switch (this) {
            default -> throw new IncompatibleClassChangeError();
            case DOWN -> Vec3f.POSITIVE_X.getDegreesQuaternion(180.0f);
            case UP -> Quaternion.IDENTITY.copy();
            case NORTH -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
                yield quaternion;
            }
            case SOUTH -> quaternion;
            case WEST -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0f));
                yield quaternion;
            }
            case EAST -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0f));
                yield quaternion;
            }
        };
    }

    public int getId() {
        return this.id;
    }

    public int getHorizontal() {
        return this.idHorizontal;
    }

    public AxisDirection getDirection() {
        return this.direction;
    }

//    public static Direction getLookDirectionForAxis(Entity entity, Axis axis) {
//        return switch (axis) {
//            default -> throw new IncompatibleClassChangeError();
//            case X -> {
//                if (EAST.pointsTo(entity.getYaw(1.0f))) {
//                    yield EAST;
//                }
//                yield WEST;
//            }
//            case Z -> {
//                if (SOUTH.pointsTo(entity.getYaw(1.0f))) {
//                    yield SOUTH;
//                }
//                yield NORTH;
//            }
//            case Y -> entity.getPitch(1.0f) < 0.0f ? UP : DOWN;
//        };
//    }

    public Direction getOpposite() {
        return Direction.byId(this.idOpposite);
    }

    public Direction rotateClockwise(Axis axis) {
        return switch (axis) {
            default -> throw new IncompatibleClassChangeError();
            case X -> {
                if (this == WEST || this == EAST) {
                    yield this;
                }
                yield this.rotateXClockwise();
            }
            case Y -> {
                if (this == UP || this == DOWN) {
                    yield this;
                }
                yield this.rotateYClockwise();
            }
            case Z -> this == NORTH || this == SOUTH ? this : this.rotateZClockwise();
        };
    }

    public Direction rotateCounterclockwise(Axis axis) {
        return switch (axis) {
            default -> throw new IncompatibleClassChangeError();
            case X -> {
                if (this == WEST || this == EAST) {
                    yield this;
                }
                yield this.rotateXCounterclockwise();
            }
            case Y -> {
                if (this == UP || this == DOWN) {
                    yield this;
                }
                yield this.rotateYCounterclockwise();
            }
            case Z -> this == NORTH || this == SOUTH ? this : this.rotateZCounterclockwise();
        };
    }

    public Direction rotateYClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    private Direction rotateXClockwise() {
        return switch (this) {
            case UP -> NORTH;
            case NORTH -> DOWN;
            case DOWN -> SOUTH;
            case SOUTH -> UP;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateXCounterclockwise() {
        return switch (this) {
            case UP -> SOUTH;
            case SOUTH -> DOWN;
            case DOWN -> NORTH;
            case NORTH -> UP;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateZClockwise() {
        return switch (this) {
            case UP -> EAST;
            case EAST -> DOWN;
            case DOWN -> WEST;
            case WEST -> UP;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    private Direction rotateZCounterclockwise() {
        return switch (this) {
            case UP -> WEST;
            case WEST -> DOWN;
            case DOWN -> EAST;
            case EAST -> UP;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    public Direction rotateYCounterclockwise() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
        };
    }

    public int getOffsetX() {
        return this.vector.getX();
    }

    public int getOffsetY() {
        return this.vector.getY();
    }

    public int getOffsetZ() {
        return this.vector.getZ();
    }

    public Vec3f getUnitVector() {
        return new Vec3f(this.getOffsetX(), this.getOffsetY(), this.getOffsetZ());
    }

    public String getName() {
        return this.name;
    }

    public Axis getAxis() {
        return this.axis;
    }

    @Nullable
    public static Direction byName(@Nullable String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase(Locale.ROOT));
    }

    public static Direction byId(int id) {
        return VALUES[Mathematics.abs(id % VALUES.length)];
    }

    public static Direction fromHorizontal(int value) {
        return HORIZONTAL[Mathematics.abs(value % HORIZONTAL.length)];
    }

    @Nullable
    public static Direction fromVector(BlockPos pos) {
        return VECTOR_TO_DIRECTION.get(pos.asLong());
    }

    @Nullable
    public static Direction fromVector(int x, int y, int z) {
        return VECTOR_TO_DIRECTION.get(BlockPos.asLong(x, y, z));
    }

    public static Direction fromRotation(double rotation) {
        return Direction.fromHorizontal(Mathematics.floor(rotation / 90.0 + 0.5) & 3);
    }

    public static Direction from(Axis axis, AxisDirection direction) {
        return switch (axis) {
            default -> throw new IncompatibleClassChangeError();
            case X -> {
                if (direction == AxisDirection.POSITIVE) {
                    yield EAST;
                }
                yield WEST;
            }
            case Y -> {
                if (direction == AxisDirection.POSITIVE) {
                    yield UP;
                }
                yield DOWN;
            }
            case Z -> direction == AxisDirection.POSITIVE ? SOUTH : NORTH;
        };
    }

    public float asRotation() {
        return (this.idHorizontal & 3) * 90;
    }

    public static Direction random(Random random) {
        return EntrustParser.select(ALL, random);
    }

    public static Direction getFacing(double x, double y, double z) {
        return Direction.getFacing((float)x, (float)y, (float)z);
    }

    public static Direction getFacing(float x, float y, float z) {
        Direction direction = NORTH;
        float f = Float.MIN_VALUE;
        for (Direction direction2 : ALL) {
            float g = x * (float)direction2.vector.getX() + y * (float)direction2.vector.getY() + z * (float)direction2.vector.getZ();
            if (!(g > f)) continue;
            f = g;
            direction = direction2;
        }
        return direction;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    private static DataResult<Direction> validateVertical(Direction direction) {
        return direction.getAxis().isVertical() ? DataResult.success(direction) : DataResult.error("Expected a vertical direction");
    }

    public static Direction get(AxisDirection direction, Axis axis) {
        for (Direction direction2 : ALL) {
            if (direction2.getDirection() != direction || direction2.getAxis() != axis) continue;
            return direction2;
        }
        throw new IllegalArgumentException("No such direction: " + direction + " " + axis);
    }

    public Vec3i getVector() {
        return this.vector;
    }

    /**
     * {@return whether the given yaw points to the direction}
     *
     * @implNote This returns whether the yaw can make an acute angle with the direction.
     *
     * <p>This always returns {@code false} for vertical directions.
     */
    public boolean pointsTo(float yaw) {
        float f = yaw * ((float)Math.PI / 180);
        float g = - Mathematics.sin(f);
        float h = Mathematics.cos(f);
        return (float)this.vector.getX() * g + (float)this.vector.getZ() * h > 0.0f;
    }

    static {
        CODEC = StringIdentifiable.createCodec(Direction::values, Direction::byName);
        VERTICAL_CODEC = CODEC.flatXmap(Direction::validateVertical, Direction::validateVertical);
        ALL = Direction.values();
        NAME_MAP = Arrays.stream(ALL).collect(Collectors.toMap(Direction::getName, direction -> direction));
        VALUES = Arrays.stream(ALL).sorted(Comparator.comparingInt(direction -> direction.id)).toArray(Direction[]::new);
        HORIZONTAL = Arrays.stream(ALL).filter(direction -> direction.getAxis().isHorizontal()).sorted(Comparator.comparingInt(direction -> direction.idHorizontal)).toArray(Direction[]::new);
        VECTOR_TO_DIRECTION = Arrays.stream(ALL).collect(Collectors.toMap(direction -> new BlockPos(direction.getVector()).asLong(), direction -> direction, (direction1, direction2) -> {
            throw new IllegalArgumentException("Duplicate keys");
        }, Long2ObjectOpenHashMap::new));
    }
    
    public enum Axis implements StringIdentifiable,
            Predicate<Direction>
    {
        X("x"){

            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }
        }
        ,
        Y("y"){

            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }
        }
        ,
        Z("z"){

            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }
        };

        public static final Axis[] VALUES;
        public static final Codec<Axis> CODEC;
        private static final Map<String, Axis> BY_NAME;
        private final String name;

        Axis(String name) {
            this.name = name;
        }

        @Nullable
        public static Axis fromName(String name) {
            return BY_NAME.get(name.toLowerCase(Locale.ROOT));
        }

        public String getName() {
            return this.name;
        }

        public boolean isVertical() {
            return this == Y;
        }

        public boolean isHorizontal() {
            return this == X || this == Z;
        }

        public String toString() {
            return this.name;
        }

        public static Axis pickRandomAxis(Random random) {
            return EntrustParser.select(VALUES, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis() == this;
        }

        public Type getType() {
            return switch (this) {
                default -> throw new IncompatibleClassChangeError();
                case X, Z -> Type.HORIZONTAL;
                case Y -> Type.VERTICAL;
            };
        }

        @Override
        public String asString() {
            return this.name;
        }

        public abstract int choose(int var1, int var2, int var3);

        public abstract double choose(double var1, double var3, double var5);

        static {
            VALUES = values();
            CODEC = StringIdentifiable.createCodec(Axis::values, Axis::fromName);
            BY_NAME = Arrays.stream(VALUES).collect(Collectors.toMap(Axis::getName, axis -> axis));
        }
    }

    public enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        AxisDirection(int offset, String description) {
            this.offset = offset;
            this.description = description;
        }

        public int offset() {
            return this.offset;
        }

        public String getDescription() {
            return this.description;
        }

        public String toString() {
            return this.description;
        }

        public AxisDirection getOpposite() {
            return this == POSITIVE ? NEGATIVE : POSITIVE;
        }
    }

    public enum Type implements Iterable<Direction>,
            Predicate<Direction>
    {
        HORIZONTAL(new Direction[]{NORTH, EAST, SOUTH, WEST}, new Axis[]{X, Z}),
        VERTICAL(new Direction[]{UP, DOWN}, new Axis[]{Y});

        private final Direction[] facingArray;
        private final Axis[] axisArray;

        Type(Direction[] facingArray, Axis[] axisArray) {
            this.facingArray = facingArray;
            this.axisArray = axisArray;
        }

        public Direction random(Random random) {
            return EntrustParser.select(this.facingArray, random);
        }

        public Axis randomAxis(Random random) {
            return EntrustParser.select(this.axisArray, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis().getType() == this;
        }

        @Override
        public Iterator<Direction> iterator() {
            return Iterators.forArray(this.facingArray);
        }

        public Stream<Direction> stream() {
            return Arrays.stream(this.facingArray);
        }
    }
}

