package net.minecraft.nbt;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.AbstractNbtList;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtTagSizeTracker;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Represents an NBT 64-bit integer array.
 */
public class NbtLongArray
extends AbstractNbtList<NbtLong> {
    private static final int SIZE = 192;
    public static final NbtType<NbtLongArray> TYPE = new NbtType.OfVariableSize<>() {

        @Override
        public NbtLongArray read(DataInput dataInput, int i, NbtTagSizeTracker nbtTagSizeTracker) throws IOException {
            nbtTagSizeTracker.add(192L);
            int j = dataInput.readInt();
            nbtTagSizeTracker.add(64L * (long) j);
            long[] ls = new long[j];
            for (int k = 0; k < j; ++ k) {
                ls[k] = dataInput.readLong();
            }
            return new NbtLongArray(ls);
        }

        @Override
        public NbtScanner.Result doAccept(DataInput input, NbtScanner visitor) throws IOException {
            int i = input.readInt();
            long[] ls = new long[i];
            for (int j = 0; j < i; ++ j) {
                ls[j] = input.readLong();
            }
            return visitor.visitLongArray(ls);
        }

        @Override
        public void skip(DataInput input) throws IOException {
            input.skipBytes(input.readInt() * 8);
        }

        @Override
        public String getCrashReportName() {
            return "LONG[]";
        }

        @Override
        public String getCommandFeedbackName() {
            return "TAG_Long_Array";
        }
    };
    private long[] value;

    public NbtLongArray(long[] value) {
        this.value = value;
    }

    public NbtLongArray(LongSet value) {
        this.value = value.toLongArray();
    }

    public NbtLongArray(List<Long> value) {
        this(NbtLongArray.toArray(value));
    }

    private static long[] toArray(List<Long> list) {
        long[] ls = new long[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            Long long_ = list.get(i);
            ls[i] = long_ == null ? 0L : long_;
        }
        return ls;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(this.value.length);
        for (long l : this.value) {
            output.writeLong(l);
        }
    }

    @Override
    public byte getType() {
        return 12;
    }

    public NbtType<NbtLongArray> getNbtType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.asString();
    }

    @Override
    public NbtLongArray copy() {
        long[] ls = new long[this.value.length];
        System.arraycopy(this.value, 0, ls, 0, this.value.length);
        return new NbtLongArray(ls);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof NbtLongArray && Arrays.equals(this.value, ((NbtLongArray)o).value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    public void accept(NbtElementVisitor visitor) {
        visitor.visitLongArray(this);
    }

    public long[] getLongArray() {
        return this.value;
    }

    @Override
    public int size() {
        return this.value.length;
    }

    @Override
    public NbtLong get(int i) {
        return NbtLong.of(this.value[i]);
    }

    public NbtLong set(int i, NbtLong nbtLong) {
        long l = this.value[i];
        this.value[i] = nbtLong.longValue();
        return NbtLong.of(l);
    }

    @Override
    public void add(int i, NbtLong nbtLong) {
        this.value = ArrayUtils.add(this.value, i, nbtLong.longValue());
    }

    @Override
    public boolean setElement(int index, NbtElement element) {
        if (element instanceof AbstractNbtNumber) {
            this.value[index] = ((AbstractNbtNumber)element).longValue();
            return true;
        }
        return false;
    }

    @Override
    public boolean addElement(int index, NbtElement element) {
        if (element instanceof AbstractNbtNumber) {
            this.value = ArrayUtils.add(this.value, index, ((AbstractNbtNumber)element).longValue());
            return true;
        }
        return false;
    }

    @Override
    public NbtLong remove(int i) {
        long l = this.value[i];
        this.value = ArrayUtils.remove(this.value, i);
        return NbtLong.of(l);
    }

    @Override
    public byte getHeldType() {
        return 4;
    }

    @Override
    public void clear() {
        this.value = new long[0];
    }

    @Override
    public NbtScanner.Result doAccept(NbtScanner visitor) {
        return visitor.visitLongArray(this.value);
    }
}

