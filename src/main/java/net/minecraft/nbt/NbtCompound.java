package net.minecraft.nbt;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an NBT compound object which holds unordered key-value pairs with distinct case-sensitive string keys.
 */
public class NbtCompound implements NbtElement {
    public static final Codec<NbtCompound> CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
        NbtElement nbtElement = dynamic.convert(NbtOps.INSTANCE).getValue();
        if (nbtElement instanceof NbtCompound) {
            return DataResult.success((NbtCompound) nbtElement);
        }
        return DataResult.error("Not a compound tag: " + nbtElement);
    }, nbt -> new Dynamic<NbtCompound>((DynamicOps) NbtOps.INSTANCE, nbt));
    public static final NbtType<NbtCompound> TYPE = new NbtType.OfVariableSize<>() {

        @Override
        public NbtCompound read(DataInput dataInput, int i, NbtTagSizeTracker nbtTagSizeTracker) throws IOException {
            byte b;
            nbtTagSizeTracker.add(384L);
            if (i > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            }
            HashMap<String, NbtElement> map = Maps.newHashMap();
            while ((b = NbtCompound.readByte(dataInput, nbtTagSizeTracker)) != 0) {
                String string = NbtCompound.readString(dataInput, nbtTagSizeTracker);
                nbtTagSizeTracker.add(224 + 16L * string.length());
                NbtElement nbtElement = NbtCompound.read(NbtTypes.byId(b), string, dataInput, i + 1, nbtTagSizeTracker);
                if (map.put(string, nbtElement) == null)
                    continue;
                nbtTagSizeTracker.add(288L);
            }
            return new NbtCompound(map);
        }

        @Override
        public NbtScanner.Result doAccept(DataInput input, NbtScanner visitor) throws IOException {
            byte b;
            block13:
            while ((b = input.readByte()) != 0) {
                NbtType<?> nbtType = NbtTypes.byId(b);
                switch (visitor.visitSubNbtType(nbtType)) {
                    case HALT: {
                        return NbtScanner.Result.HALT;
                    }
                    case BREAK: {
                        NbtString.skip(input);
                        nbtType.skip(input);
                        break block13;
                    }
                    case SKIP: {
                        NbtString.skip(input);
                        nbtType.skip(input);
                        continue block13;
                    }
                    default: {
                        String string = input.readUTF();
                        switch (visitor.startSubNbt(nbtType, string)) {
                            case HALT: {
                                return NbtScanner.Result.HALT;
                            }
                            case BREAK: {
                                nbtType.skip(input);
                                break block13;
                            }
                            case SKIP: {
                                nbtType.skip(input);
                                continue block13;
                            }
                        }
                        switch (nbtType.doAccept(input, visitor)) {
                            case HALT: {
                                return NbtScanner.Result.HALT;
                            }
                        }
                        continue block13;
                    }
                }
            }
            if (b != 0) {
                while ((b = input.readByte()) != 0) {
                    NbtString.skip(input);
                    NbtTypes.byId(b).skip(input);
                }
            }
            return visitor.endNested();
        }

        @Override
        public void skip(DataInput input) throws IOException {
            byte b;
            while ((b = input.readByte()) != 0) {
                NbtString.skip(input);
                NbtTypes.byId(b).skip(input);
            }
        }

        @Override
        public String getCrashReportName() {
            return "COMPOUND";
        }

        @Override
        public String getCommandFeedbackName() {
            return "TAG_Compound";
        }
    };
    private static final int SIZE = 384;
    private static final int field_33191 = 256;
    private final Map<String, NbtElement> entries;

    public NbtCompound() {
        this(Maps.newHashMap());
    }

    protected NbtCompound(Map<String, NbtElement> entries) {
        this.entries = entries;
    }

    static byte readByte(DataInput input, NbtTagSizeTracker tracker) throws IOException {
        return input.readByte();
    }

    static String readString(DataInput input, NbtTagSizeTracker tracker) throws IOException {
        return input.readUTF();
    }

    static NbtElement read(NbtType<?> reader, String key, DataInput input, int depth, NbtTagSizeTracker tracker) {
        try {
            return reader.read(input, depth, tracker);
        } catch (IOException iOException) {
//            CrashReport crashReport = CrashReport.create(iOException, "Loading NBT data");
//            CrashReportSection crashReportSection = crashReport.addElement("NBT Tag");
//            crashReportSection.add("Tag name", key);
//            crashReportSection.add("Tag type", reader.getCrashReportName());
//            throw new CrashException(crashReport);
            return null;
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        for (String string : this.entries.keySet()) {
            NbtElement nbtElement = this.entries.get(string);
            NbtCompound.write(string, nbtElement, output);
        }
        output.writeByte(0);
    }

    @Override
    public byte getType() {
        return 10;
    }

    public NbtType<NbtCompound> getNbtType() {
        return TYPE;
    }

    @Override
    public NbtCompound copy() {
        HashMap<String, NbtElement> map = Maps.newHashMap(Maps.transformValues(this.entries, NbtElement::copy));
        return new NbtCompound(map);
    }

    @Override
    public void accept(NbtElementVisitor visitor) {
        visitor.visitCompound(this);
    }

    @Override
    public NbtScanner.Result doAccept(NbtScanner visitor) {
        for (Map.Entry<String, NbtElement> entry : this.entries.entrySet()) {
            NbtElement nbtElement = entry.getValue();
            NbtType<?> nbtType = nbtElement.getNbtType();
            NbtScanner.NestedResult nestedResult = visitor.visitSubNbtType(nbtType);
            switch (nestedResult) {
                case HALT -> {
                    return NbtScanner.Result.HALT;
                }
                case BREAK -> {
                    return visitor.endNested();
                }
                case SKIP -> {
                    continue;
                }
            }
            nestedResult = visitor.startSubNbt(nbtType, entry.getKey());
            switch (nestedResult) {
                case HALT -> {
                    return NbtScanner.Result.HALT;
                }
                case BREAK -> {
                    return visitor.endNested();
                }
                case SKIP -> {
                    continue;
                }
            }
            NbtScanner.Result result = nbtElement.doAccept(visitor);
            switch (result) {
                case HALT -> {
                    return NbtScanner.Result.HALT;
                }
                case BREAK -> {
                    return visitor.endNested();
                }
            }
        }
        return visitor.endNested();
    }

    private static void write(String key, NbtElement element, DataOutput output) throws IOException {
        output.writeByte(element.getType());
        if (element.getType() == 0) {
            return;
        }
        output.writeUTF(key);
        element.write(output);
    }

    public Set<String> getKeys() {
        return this.entries.keySet();
    }

    public int getSize() {
        return this.entries.size();
    }

    @Nullable
    public NbtElement put(String key, NbtElement element) {
        return this.entries.put(key, element);
    }

    public void putByte(String key, byte value) {
        this.entries.put(key, NbtByte.of(value));
    }

    public void putShort(String key, short value) {
        this.entries.put(key, NbtShort.of(value));
    }

    public void putInt(String key, int value) {
        this.entries.put(key, NbtInt.of(value));
    }

    public void putLong(String key, long value) {
        this.entries.put(key, NbtLong.of(value));
    }

    /**
     * Writes a {@link UUID} to its NBT representation in this {@code NbtCompound}.
     */
    public void putUuid(String key, UUID value) {
        this.entries.put(key, NbtHelper.fromUuid(value));
    }

    /**
     * Reads a {@link UUID} from its NBT representation in this {@code NbtCompound}.
     */
    public UUID getUuid(String key) {
        return NbtHelper.toUuid(this.get(key));
    }

    @Nullable
    public NbtElement get(String key) {
        return this.entries.get(key);
    }

    /**
     * Returns {@code true} if this {@code NbtCompound} contains a valid UUID representation associated with the given key.
     * A valid UUID is represented by an int array of length 4.
     */
    public boolean containsUuid(String key) {
        NbtElement nbtElement = this.get(key);
        return nbtElement != null && nbtElement.getNbtType() == NbtIntArray.TYPE && ((NbtIntArray) nbtElement).getIntArray().length == 4;
    }

    public void putFloat(String key, float value) {
        this.entries.put(key, NbtFloat.of(value));
    }

    public void putDouble(String key, double value) {
        this.entries.put(key, NbtDouble.of(value));
    }

    public void putString(String key, String value) {
        this.entries.put(key, NbtString.of(value));
    }

    public void putByteArray(String key, byte[] value) {
        this.entries.put(key, new NbtByteArray(value));
    }

    public void putByteArray(String key, List<Byte> value) {
        this.entries.put(key, new NbtByteArray(value));
    }

    public void putIntArray(String key, int[] value) {
        this.entries.put(key, new NbtIntArray(value));
    }

    public void putIntArray(String key, List<Integer> value) {
        this.entries.put(key, new NbtIntArray(value));
    }

    public void putLongArray(String key, long[] value) {
        this.entries.put(key, new NbtLongArray(value));
    }

    public void putLongArray(String key, List<Long> value) {
        this.entries.put(key, new NbtLongArray(value));
    }

    public void putBoolean(String key, boolean value) {
        this.entries.put(key, NbtByte.of(value));
    }

    /**
     * Determines whether the NBT compound object contains the specified key.
     *
     * @return {@code true} if the key exists, else {@code false}
     */
    public boolean contains(String key) {
        return this.entries.containsKey(key);
    }

    public short getShort(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).shortValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0;
    }

    /**
     * Returns whether the NBT compound object contains an element of the specified type at the specified key.
     * <p>
     * The type restriction can also be {@link NbtElement#NUMBER_TYPE NUMBER_TYPE}, which only allows any type of number.
     *
     * @return {@code true} if the key exists and the element type is equivalent to the given {@code type}, else {@code false}
     */
    public boolean contains(String key, int type) {
        byte i = this.getType(key);
        if (i == type) {
            return true;
        }
        if (type == 99) {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
        return false;
    }

    /**
     * Gets the {@linkplain NbtElement#getType NBT type} of the element stored at the specified key.
     *
     * @return the element NBT type, or {@link NbtElement#NULL_TYPE NULL_TYPE} if it does not exist
     */
    public byte getType(String key) {
        NbtElement nbtElement = this.entries.get(key);
        if (nbtElement == null) {
            return 0;
        }
        return nbtElement.getType();
    }

    public int getInt(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).intValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0;
    }

    public long getLong(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).longValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0L;
    }

    public float getFloat(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).floatValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0.0f;
    }

    public double getDouble(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).doubleValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0.0;
    }

    public String getString(String key) {
        try {
            if (this.contains(key, 8)) {
                return this.entries.get(key).asString();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return "";
    }

    public byte[] getByteArray(String key) {
        try {
            if (this.contains(key, 7)) {
                return ((NbtByteArray) this.entries.get(key)).getByteArray();
            }
        } catch (ClassCastException classCastException) {
//            throw new CrashException(this.createCrashReport(key, NbtByteArray.TYPE, classCastException));
        }
        return new byte[0];
    }

    public int[] getIntArray(String key) {
        try {
            if (this.contains(key, 11)) {
                return ((NbtIntArray) this.entries.get(key)).getIntArray();
            }
        } catch (ClassCastException classCastException) {
//            throw new CrashException(this.createCrashReport(key, NbtIntArray.TYPE, classCastException));
        }
        return new int[0];
    }

    public long[] getLongArray(String key) {
        try {
            if (this.contains(key, 12)) {
                return ((NbtLongArray) this.entries.get(key)).getLongArray();
            }
        } catch (ClassCastException classCastException) {
//            throw new CrashException(this.createCrashReport(key, NbtLongArray.TYPE, classCastException));
        }
        return new long[0];
    }

//    private CrashReport createCrashReport(String key, NbtType<?> reader, ClassCastException exception) {
//        CrashReport crashReport = CrashReport.create(exception, "Reading NBT data");
//        CrashReportSection crashReportSection = crashReport.addElement("Corrupt NBT tag", 1);
//        crashReportSection.add("Tag type found", () -> this.entries.get(key).getNbtType().getCrashReportName());
//        crashReportSection.add("Tag type expected", reader::getCrashReportName);
//        crashReportSection.add("Tag name", key);
//        return crashReport;
//    }

    public NbtCompound getCompound(String key) {
        try {
            if (this.contains(key, 10)) {
                return (NbtCompound) this.entries.get(key);
            }
        } catch (ClassCastException classCastException) {
//            throw new CrashException(this.createCrashReport(key, TYPE, classCastException));
        }
        return new NbtCompound();
    }

    public NbtList getList(String key, int type) {
        try {
            if (this.getType(key) == 9) {
                NbtList nbtList = (NbtList) this.entries.get(key);
                if (nbtList.isEmpty() || nbtList.getHeldType() == type) {
                    return nbtList;
                }
                return new NbtList();
            }
        } catch (ClassCastException nbtList) {
//            throw new CrashException(this.createCrashReport(key, NbtList.TYPE, nbtList));
        }
        return new NbtList();
    }

    public boolean getBoolean(String key) {
        return this.getByte(key) != 0;
    }

    public byte getByte(String key) {
        try {
            if (this.contains(key, 99)) {
                return ((AbstractNbtNumber) this.entries.get(key)).byteValue();
            }
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return 0;
    }

    public void remove(String key) {
        this.entries.remove(key);
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public int hashCode() {
        return this.entries.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof NbtCompound && Objects.equals(this.entries, ((NbtCompound) o).entries);
    }

    @Override
    public String toString() {
        return this.asString();
    }

    public NbtCompound copyFrom(NbtCompound source) {
        for (String string : source.entries.keySet()) {
            NbtElement nbtElement = source.entries.get(string);
            if (nbtElement.getType() == 10) {
                if (this.contains(string, 10)) {
                    NbtCompound nbtCompound = this.getCompound(string);
                    nbtCompound.copyFrom((NbtCompound) nbtElement);
                    continue;
                }
                this.put(string, nbtElement.copy());
                continue;
            }
            this.put(string, nbtElement.copy());
        }
        return this;
    }

    protected Map<String, NbtElement> toMap() {
        return Collections.unmodifiableMap(this.entries);
    }
}

