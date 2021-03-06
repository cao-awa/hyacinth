package com.github.cao.awa.hyacinth.network.packet.buf;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.google.common.collect.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.*;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.nbt.*;
import net.minecraft.util.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;
import java.util.function.IntConsumer;

public class PacketByteBuf extends ByteBuf {
    public static final short DEFAULT_MAX_STRING_LENGTH = Short.MAX_VALUE;
    public static final int MAX_TEXT_LENGTH = 262144;
    private static final int MAX_VAR_INT_LENGTH = 5;
    private static final int MAX_READ_NBT_SIZE = 2097152;
    private final ByteBuf parent;

    public PacketByteBuf(ByteBuf parent) {
        this.parent = parent;
    }

    public Identifier readIdentifier() {
        return new Identifier(this.readString(DEFAULT_MAX_STRING_LENGTH));
    }

    /**
     * Reads a string from this buf. A string is represented by a byte array of
     * its UTF-8 data. The string can have a maximum length of {@code maxLength}.
     *
     * @param maxLength
     *         the maximum length of the string read
     * @return the string read
     * @throws io.netty.handler.codec.DecoderException
     *         if the string read
     *         is longer than {@code maxLength}
     * @see #readString()
     * @see #writeString(String)
     * @see #writeString(String, int)
     */
    public String readString(int maxLength) {
        int i = this.readVarInt();
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        }
        if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        }
        String string = this.toString(this.readerIndex(), i, StandardCharsets.UTF_8);
        this.readerIndex(this.readerIndex() + i);
        if (string.length() > maxLength) {
            throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
        }
        return string;
    }

    public int readVarInt() {
        byte b;
        int i = 0;
        int j = 0;
        do {
            b = this.readByte();
            i |= (b & 127) << j++ * 7;
            if (j < 6)
                continue;
            throw new RuntimeException("VarInt too big");
        } while ((b & 0x80) == 128);
        return i;
    }

    /**
     * Reads a text from this buf. A text is represented by a JSON string with
     * max length {@value #MAX_TEXT_LENGTH}.
     *
     * @return the read text
     * @throws io.netty.handler.codec.DecoderException
     *         if the JSON string read
     *         exceeds {@value #MAX_TEXT_LENGTH} in length
     * @see #writeText(Text)
     * @see #MAX_TEXT_LENGTH
     */
    public Text readText() {
        return Text.Serializer.fromJson(this.readString(MAX_TEXT_LENGTH));
    }

    /**
     * Writes a text to this buf. A text is represented by a JSON string with
     * max length {@value #MAX_TEXT_LENGTH}.
     *
     * @param text
     *         the text to write
     * @return this buf, for chaining
     * @throws io.netty.handler.codec.EncoderException
     *         if the JSON string
     *         written exceeds {@value #MAX_TEXT_LENGTH} in length
     * @see #readText()
     * @see #MAX_TEXT_LENGTH
     */
    public PacketByteBuf writeText(Text text) {
        return this.writeString(text.toJSONObject().toString(), MAX_TEXT_LENGTH);
    }

    /**
     * Writes a string to this buf. A string is represented by a byte array of
     * its UTF-8 data. That byte array can have a maximum length of
     * {@code maxLength}.
     *
     * @param string
     *         the string to write
     * @param maxLength
     *         the max length of the byte array
     * @return this buf, for chaining
     * @throws io.netty.handler.codec.EncoderException
     *         if the byte array of the
     *         string to write is longer than {@code maxLength}
     * @see #readString()
     * @see #readString(int)
     * @see #writeString(String)
     */
    public PacketByteBuf writeString(String string, int maxLength) {
        byte[] bs = string.getBytes(StandardCharsets.UTF_8);
        if (bs.length > maxLength) {
            throw new EncoderException("String too big (was " + bs.length + " bytes encoded, max " + maxLength + ")");
        }
        this.writeVarInt(bs.length);
        this.writeBytes(bs);
        return this;
    }

    public PacketByteBuf writeVarInt(int value) {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                this.writeByte(value);
                return this;
            }
            this.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    /**
     * Writes an identifier to this buf. An identifier is represented by its
     * string form. The written identifier's byte array can have a max length of
     * {@value #DEFAULT_MAX_STRING_LENGTH}.
     *
     * @param id
     *         the identifier to write
     * @return the read identifier
     * @throws io.netty.handler.codec.EncoderException
     *         if the {@code id}'s
     *         byte array is longer than {@value #DEFAULT_MAX_STRING_LENGTH}
     * @see #readIdentifier()
     */
    public PacketByteBuf writeIdentifier(Identifier id) {
        this.writeString(id.toString());
        return this;
    }

    /**
     * Writes a string to this buf. A string is represented by a byte array of
     * its UTF-8 data. That byte array can have a maximum length of
     * {@value #DEFAULT_MAX_STRING_LENGTH}.
     *
     * @param string
     *         the string to write
     * @return this buf, for chaining
     * @throws io.netty.handler.codec.EncoderException
     *         if the byte array of the
     *         string to write is longer than {@value #DEFAULT_MAX_STRING_LENGTH}
     * @see #readString()
     * @see #readString(int)
     * @see #writeString(String, int)
     */
    public PacketByteBuf writeString(String string) {
        return this.writeString(string, DEFAULT_MAX_STRING_LENGTH);
    }

    /**
     * Writes an array of primitive bytes to this buf. The array first has a
     * var int indicating its length, followed by the actual bytes.
     *
     * @param array
     *         the array to write
     * @return this buf, for chaining
     * @see #readByteArray()
     */
    public PacketByteBuf writeByteArray(byte[] array) {
        this.writeVarInt(array.length);
        this.writeBytes(array);
        return this;
    }

    /**
     * Reads an array of primitive bytes from this buf. The array first has a
     * var int indicating its length, followed by the actual bytes. The array
     * does not have a length limit.
     *
     * @return the read byte array
     * @see #readByteArray(int)
     * @see #writeByteArray(byte[])
     */
    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    /**
     * Reads an array of primitive bytes from this buf. The array first has a
     * var int indicating its length, followed by the actual bytes. The array
     * has a length limit given by {@code maxSize}.
     *
     * @param maxSize
     *         the max length of the read array
     * @return the read byte array
     * @throws io.netty.handler.codec.DecoderException
     *         if the read array has a
     *         length over {@code maxSize}
     * @see #readByteArray()
     * @see #writeByteArray(byte[])
     */
    public byte[] readByteArray(int maxSize) {
        int i = this.readVarInt();
        if (i > maxSize) {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxSize);
        }
        byte[] bs = new byte[i];
        this.readBytes(bs);
        return bs;
    }

    /**
     * Reads a string from this buf. A string is represented by a byte array of
     * its UTF-8 data. The string can have a maximum length of {@value
     * #DEFAULT_MAX_STRING_LENGTH}.
     *
     * @return the string read
     * @throws io.netty.handler.codec.DecoderException
     *         if the string read
     *         exceeds the maximum length
     * @see #readString(int)
     * @see #writeString(String)
     * @see #writeString(String, int)
     */
    public String readString() {
        return this.readString(DEFAULT_MAX_STRING_LENGTH);
    }

    private int getVarIntLength(long value) {
        int length = 0;
        do {
            value = value >> 7;
            length++;
        } while (value > 0);
        return length;
    }

    /**
     * Returns the number of bytes needed to encode {@code value} as a
     * {@linkplain #writeVarInt(int) var int}. Guaranteed to be between {@code
     * 1} and {@value #MAX_VAR_INT_LENGTH}.
     *
     * @param value
     *         the value to encode
     * @return the number of bytes a var int {@code value} uses
     */
    public static int getVarIntLength(int value) {
        for (int i = 1; i < MAX_VAR_INT_LENGTH; ++ i) {
            if ((value & - 1 << i * 7) != 0)
                continue;
            return i;
        }
        return MAX_VAR_INT_LENGTH;
    }

    /**
     * Reads a block position from this buf. A block position is represented by
     * a regular long.
     *
     * @return the read block pos
     * @see #writeBlockPos(BlockPos)
     */
    public BlockPos readBlockPos() {
        return BlockPos.fromLong(this.readLong());
    }

    /**
     * Writes a block position to this buf. A block position is represented by
     * a regular long.
     *
     * @param pos
     *         the pos to write
     * @return this buf, for chaining
     * @see #readBlockPos()
     */
    public PacketByteBuf writeBlockPos(BlockPos pos) {
        this.writeLong(pos.asLong());
        return this;
    }

    /**
     * Writes a UUID (universally unique identifier) to this buf. A UUID is
     * represented by two regular longs.
     *
     * @param uuid
     *         the UUID to write
     * @return this buf, for chaining
     * @see #readUuid()
     */
    public PacketByteBuf writeUuid(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    /**
     * Reads a UUID (universally unique identifier) from this buf. A UUID is
     * represented by two regular longs.
     *
     * @return the read UUID
     * @see #writeUuid(UUID)
     */
    public UUID readUuid() {
        return new UUID(this.readLong(), this.readLong());
    }

    /**
     * Reads a collection from this buf. The collection is stored as a leading
     * {@linkplain #readVarInt() var int} size followed by the entries
     * sequentially.
     *
     * @param <T>
     *         the collection's entry type
     * @param <C>
     *         the collection's type
     * @param collectionFactory
     *         a factory that creates a collection with a given size
     * @param entryParser
     *         a parser that parses each entry for the collection given this buf
     * @return the read collection
     * @see #writeCollection(Collection, BiConsumer)
     * @see #readList(Function)
     */
    public <T, C extends Collection<T>> C readCollection(IntFunction<C> collectionFactory, Function<PacketByteBuf, T> entryParser) {
        int i = this.readVarInt();
        Collection<T> collection = collectionFactory.apply(i);
        for (int j = 0; j < i; ++ j) {
            collection.add(entryParser.apply(this));
        }
        return (C) collection;
    }

    /**
     * Writes a collection to this buf. The collection is stored as a leading
     * {@linkplain #readVarInt() var int} size followed by the entries
     * sequentially.
     *
     * @param <T>
     *         the list's entry type
     * @param entrySerializer
     *         a serializer that writes each entry to this buf
     * @param collection
     *         the collection to write
     * @see #readCollection(IntFunction, Function)
     */
    public <T> void writeCollection(Collection<T> collection, BiConsumer<PacketByteBuf, T> entrySerializer) {
        this.writeVarInt(collection.size());
        for (T object : collection) {
            entrySerializer.accept(this, object);
        }
    }

    /**
     * Reads a collection from this buf as an array list.
     *
     * @param <T>
     *         the list's entry type
     * @param entryParser
     *         a parser that parses each entry for the collection given this buf
     * @return the read list
     * @see #readCollection(IntFunction, Function)
     */
    public <T> List<T> readList(Function<PacketByteBuf, T> entryParser) {
        return this.readCollection(Lists::newArrayListWithCapacity, entryParser);
    }

    /**
     * Writes an object to this buf as a compound NBT with the given codec.
     *
     * @param <T>
     *         the encoded object's type
     * @param object
     *         the object to write to this buf
     * @param codec
     *         the codec to encode the object
     * @throws io.netty.handler.codec.EncoderException
     *         if the {@code codec} fails
     *         to encode the compound NBT
     * @see #decode(Codec)
     */
    public <T> void encode(Codec<T> codec, T object) {
        DataResult<NbtElement> dataResult = codec.encodeStart(NbtOps.INSTANCE, object);
        dataResult.error().ifPresent(partial -> {
            throw new EncoderException("Failed to encode: " + partial.message() + " " + object);
        });
        this.writeNbt((NbtCompound) dataResult.result().get());
    }

    /**
     * Reads an object from this buf as a compound NBT with the given codec.
     *
     * @param <T>
     *         the decoded object's type
     * @param codec
     *         the codec to decode the object
     * @return the read object
     * @throws io.netty.handler.codec.EncoderException
     *         if the {@code codec} fails
     *         to decode the compound NBT
     * @see #encode(Codec, Object)
     */
    public <T> T decode(Codec<T> codec) {
        NbtCompound nbtCompound = this.readUnlimitedNbt();
        DataResult<T> dataResult = codec.parse(NbtOps.INSTANCE, nbtCompound);
        dataResult.error().ifPresent(partial -> {
            throw new EncoderException("Failed to decode: " + partial.message() + " " + nbtCompound);
        });
        return dataResult.result().get();
    }

    /**
     * Writes an NBT compound to this buf. The binary representation of NBT is
     * handled by {@link net.minecraft.nbt.NbtIo}. If {@code compound} is {@code
     * null}, it is treated as an NBT null.
     *
     * @param compound
     *         the compound to write
     * @return this buf, for chaining
     * @throws io.netty.handler.codec.EncoderException
     *         if the NBT cannot be
     *         written
     * @see #readNbt()
     * @see #readUnlimitedNbt()
     * @see #readNbt(NbtTagSizeTracker)
     */
    public PacketByteBuf writeNbt(@Nullable NbtCompound compound) {
        if (compound == null) {
            this.writeByte(0);
        } else {
            try {
                NbtIo.write(compound, new ByteBufOutputStream(this));
            } catch (IOException iOException) {
                throw new EncoderException(iOException);
            }
        }
        return this;
    }

    /**
     * Reads an NBT compound from this buf. The binary representation of NBT is
     * handled by {@link net.minecraft.nbt.NbtIo}. If an NBT null is encountered,
     * this method returns {@code null}. The compound does not have a size limit.
     *
     * @return the read compound, may be {@code null}
     * @throws io.netty.handler.codec.EncoderException
     *         if the NBT cannot be read
     * @apiNote Since this version does not have a size limit, it may be
     * vulnerable to malicious NBT spam attacks.
     * @see #writeNbt(NbtCompound)
     * @see #readNbt()
     * @see #readNbt(NbtTagSizeTracker)
     */
    @Nullable
    public NbtCompound readUnlimitedNbt() {
        return this.readNbt(NbtTagSizeTracker.EMPTY);
    }

    /**
     * Reads an NBT compound from this buf. The binary representation of NBT is
     * handled by {@link net.minecraft.nbt.NbtIo}. If an NBT null is encountered,
     * this method returns {@code null}. The compound can have a maximum size of
     * {@value #MAX_READ_NBT_SIZE} bytes.
     *
     * @return the read compound, may be {@code null}
     * @throws io.netty.handler.codec.EncoderException
     *         if the NBT cannot be read
     * @throws RuntimeException
     *         if the compound exceeds the allowed maximum size
     * @see #writeNbt(NbtCompound)
     * @see #readUnlimitedNbt()
     * @see #readNbt(NbtTagSizeTracker)
     * @see #MAX_READ_NBT_SIZE
     */
    @Nullable
    public NbtCompound readNbt() {
        return this.readNbt(new NbtTagSizeTracker(MAX_READ_NBT_SIZE));
    }

    /**
     * Reads an NBT compound from this buf. The binary representation of NBT is
     * handled by {@link net.minecraft.nbt.NbtIo}. If an NBT null is encountered,
     * this method returns {@code null}. The compound can have a maximum size
     * controlled by the {@code sizeTracker}.
     *
     * @return the read compound, may be {@code null}
     * @throws io.netty.handler.codec.EncoderException
     *         if the NBT cannot be read
     * @throws RuntimeException
     *         if the compound exceeds the allowed maximum size
     * @see #writeNbt(NbtCompound)
     * @see #readNbt()
     * @see #readUnlimitedNbt()
     */
    @Nullable
    public NbtCompound readNbt(NbtTagSizeTracker sizeTracker) {
        int i = this.readerIndex();
        byte b = this.readByte();
        if (b == 0) {
            return null;
        }
        this.readerIndex(i);
        try {
            return NbtIo.read(new ByteBufInputStream(this), sizeTracker);
        } catch (IOException iOException) {
            throw new EncoderException(iOException);
        }
    }

    /**
     * Reads a map from this buf. The map is stored as a leading
     * {@linkplain #readVarInt() var int} size followed by each key and value
     * pair.
     *
     * @param <K>
     *         the key type
     * @param <V>
     *         the value type
     * @param <M>
     *         the map type
     * @param mapFactory
     *         a factory that creates a map with a given size
     * @param valueParser
     *         a parser that parses each value for the map given this buf
     * @param keyParser
     *         a parser that parses each key for the map given this buf
     * @return the read map
     * @see #writeMap(Map, BiConsumer, BiConsumer)
     * @see #readMap(Function, Function)
     */
    public <K, V, M extends Map<K, V>> M readMap(IntFunction<M> mapFactory, Function<PacketByteBuf, K> keyParser, Function<PacketByteBuf, V> valueParser) {
        int i = this.readVarInt();
        Map<K, V> map = mapFactory.apply(i);
        for (int j = 0; j < i; ++ j) {
            K object = keyParser.apply(this);
            V object2 = valueParser.apply(this);
            map.put(object, object2);
        }
        return (M) map;
    }

    /**
     * Reads a map from this buf as a hash map.
     *
     * @param <K>
     *         the key type
     * @param <V>
     *         the value type
     * @param valueParser
     *         a parser that parses each value for the map given this buf
     * @param keyParser
     *         a parser that parses each key for the map given this buf
     * @return the read map
     * @see #readMap(IntFunction, Function, Function)
     */
    public <K, V> Map<K, V> readMap(Function<PacketByteBuf, K> keyParser, Function<PacketByteBuf, V> valueParser) {
        return this.readMap(Maps::newHashMapWithExpectedSize, keyParser, valueParser);
    }

    /**
     * Writes a map to this buf. The map is stored as a leading
     * {@linkplain #readVarInt() var int} size followed by each key and value
     * pair.
     *
     * @param <K>
     *         the key type
     * @param <V>
     *         the value type
     * @param map
     *         the map to write
     * @param keySerializer
     *         a serializer that writes each key in the map to this buf
     * @param valueSerializer
     *         a serializer that writes each value in the map to this buf
     * @see #readMap(IntFunction, Function, Function)
     */
    public <K, V> void
    writeMap(Map<K, V> map, BiConsumer<PacketByteBuf, K> keySerializer, BiConsumer<PacketByteBuf, V> valueSerializer) {
        this.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keySerializer.accept(this, key);
            valueSerializer.accept(this, value);
        });
    }

    /**
     * Reads a list of primitive ints from this buf. The ints are stored as var
     * ints, with an extra var int in the beginning indicating the size.
     *
     * @return the read list
     * @apiNote To limit the length of the list or array read, use
     * {@link #readIntArray(int)}.
     * @implNote A list of ints has the same format as an int array.
     * @see #writeIntList(IntList)
     * @see #readIntArray()
     */
    public IntList readIntList() {
        int i = this.readVarInt();
        IntArrayList intList = new IntArrayList();
        for (int j = 0; j < i; ++ j) {
            intList.add(this.readVarInt());
        }
        return intList;
    }

    /**
     * Writes a list of primitive ints from this buf. The ints are stored as var
     * ints, with an extra var int in the beginning indicating the size.
     *
     * @param list
     *         the list to write
     * @implNote A list of ints has the same format as an int array.
     * @see #readIntList()
     * @see #writeIntArray(int[])
     */
    public void writeIntList(IntList list) {
        this.writeVarInt(list.size());
        list.forEach((IntConsumer) this::writeVarInt);
    }

    /**
     * Writes an array of primitive ints to this buf. The array first has a
     * var int indicating its length, followed by the var int entries.
     *
     * @param array
     *         the array to write
     * @return this buf, for chaining
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray(int)
     * @see #writeIntArray(int[])
     * @see #writeIntList(IntList)
     */
    public PacketByteBuf writeIntArray(int[] array) {
        this.writeVarInt(array.length);
        for (int i : array) {
            this.writeVarInt(i);
        }
        return this;
    }

    /**
     * Reads an array of primitive ints from this buf. The array first has a
     * var int indicating its length, followed by the var int entries. The array
     * does not have a length limit.
     *
     * @return the read byte array
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray(int)
     * @see #writeIntArray(int[])
     * @see #readIntList()
     */
    public int[] readIntArray() {
        return this.readIntArray(this.readableBytes());
    }

    /**
     * Reads an array of primitive ints from this buf. The array first has a
     * var int indicating its length, followed by the var int entries. The array
     * has a length limit given by {@code maxSize}.
     *
     * @param maxSize
     *         the max length of the read array
     * @return the read byte array
     * @throws io.netty.handler.codec.DecoderException
     *         if the read array has a
     *         length over {@code maxSize}
     * @implNote An int array has the same format as a list of ints.
     * @see #readIntArray()
     * @see #writeIntArray(int[])
     */
    public int[] readIntArray(int maxSize) {
        int i = this.readVarInt();
        if (i > maxSize) {
            throw new DecoderException("VarIntArray with size " + i + " is bigger than allowed " + maxSize);
        }
        int[] is = new int[i];
        for (int j = 0; j < is.length; ++ j) {
            is[j] = this.readVarInt();
        }
        return is;
    }

    /**
     * Writes an enum constant to this buf. An enum constant is represented
     * by a var int indicating its ordinal.
     *
     * @param instance
     *         the enum constant to write
     * @return this buf, for chaining
     * @see #readEnumConstant(Class)
     */
    public PacketByteBuf writeEnumConstant(Enum<?> instance) {
        return this.writeVarInt(instance.ordinal());
    }

    /**
     * Reads an enum constant from this buf. An enum constant is represented
     * by a var int indicating its ordinal.
     *
     * @param enumClass
     *         the enum class, for constant lookup
     * @return the read enum constant
     * @see #writeEnumConstant(Enum)
     */
    public <T extends Enum<T>> T readEnumConstant(Class<T> enumClass) {
        return (T) ((Enum[]) enumClass.getEnumConstants())[this.readVarInt()];
    }

    /**
     * Iterates a collection from this buf. The collection is stored as a leading
     * {@linkplain #readVarInt() var int} {@code size} followed by the entries
     * sequentially. The {@code consumer} will be called {@code size} times.
     *
     * @param consumer
     *         the consumer to read entries
     * @see #readCollection(IntFunction, Function)
     */
    public void forEachInCollection(Consumer<PacketByteBuf> consumer) {
        int i = this.readVarInt();
        for (int j = 0; j < i; ++ j) {
            consumer.accept(this);
        }
    }

    @Override
    public int capacity() {
        return this.parent.capacity();
    }

    @Override
    public ByteBuf capacity(int capacity) {
        return this.parent.capacity(capacity);
    }

    @Override
    public int maxCapacity() {
        return this.parent.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.parent.alloc();
    }

    @Override
    public ByteOrder order() {
        return this.parent.order();
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return this.parent.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return this.parent.unwrap();
    }

    @Override
    public boolean isDirect() {
        return this.parent.isDirect();
    }

    @Override
    public int readerIndex() {
        return this.parent.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int index) {
        return this.parent.readerIndex(index);
    }

    @Override
    public int writerIndex() {
        return this.parent.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int index) {
        return this.parent.writerIndex(index);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return this.parent.setIndex(readerIndex, writerIndex);
    }

    @Override
    public int readableBytes() {
        return this.parent.readableBytes();
    }

    @Override
    public int writableBytes() {
        return this.parent.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return this.parent.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return this.parent.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return this.parent.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return this.parent.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return this.parent.isWritable(size);
    }

    @Override
    public ByteBuf clear() {
        return this.parent.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return this.parent.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return this.parent.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return this.parent.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return this.parent.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return this.parent.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return this.parent.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minBytes) {
        return this.parent.ensureWritable(minBytes);
    }

    @Override
    public int ensureWritable(int minBytes, boolean force) {
        return this.parent.ensureWritable(minBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return this.parent.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return this.parent.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return this.parent.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return this.parent.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return this.parent.getUnsignedShort(index);
    }

    @Override
    public int getMedium(int index) {
        return this.parent.getMedium(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return this.parent.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {
        return this.parent.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return this.parent.getUnsignedInt(index);
    }

    @Override
    public long getLong(int index) {
        return this.parent.getLong(index);
    }

    @Override
    public char getChar(int index) {
        return this.parent.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return this.parent.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return this.parent.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf buf) {
        return this.parent.getBytes(index, buf);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf buf, int length) {
        return this.parent.getBytes(index, buf, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf buf, int outputIndex, int length) {
        return this.parent.getBytes(index, buf, outputIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] bytes) {
        return this.parent.getBytes(index, bytes);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] bytes, int outputIndex, int length) {
        return this.parent.getBytes(index, bytes, outputIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer buf) {
        return this.parent.getBytes(index, buf);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream stream, int length) throws IOException {
        return this.parent.getBytes(index, stream, length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel channel, int length) throws IOException {
        return this.parent.getBytes(index, channel, length);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return this.parent.setBoolean(index, value);
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return this.parent.setByte(index, value);
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return this.parent.setShort(index, value);
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return this.parent.setMedium(index, value);
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return this.parent.setInt(index, value);
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return this.parent.setLong(index, value);
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return this.parent.setChar(index, value);
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return this.parent.setFloat(index, value);
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return this.parent.setDouble(index, value);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf buf) {
        return this.parent.setBytes(index, buf);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf buf, int length) {
        return this.parent.setBytes(index, buf, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf buf, int sourceIndex, int length) {
        return this.parent.setBytes(index, buf, sourceIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] bytes) {
        return this.parent.setBytes(index, bytes);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] bytes, int sourceIndex, int length) {
        return this.parent.setBytes(index, bytes, sourceIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer buf) {
        return this.parent.setBytes(index, buf);
    }

    @Override
    public int setBytes(int index, InputStream stream, int length) throws IOException {
        return this.parent.setBytes(index, stream, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel channel, int length) throws IOException {
        return this.parent.setBytes(index, channel, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        return this.parent.setZero(index, length);
    }

    @Override
    public boolean readBoolean() {
        return this.parent.readBoolean();
    }

    @Override
    public byte readByte() {
        return this.parent.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return this.parent.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return this.parent.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return this.parent.readUnsignedShort();
    }

    @Override
    public int readMedium() {
        return this.parent.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return this.parent.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return this.parent.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return this.parent.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return this.parent.readLong();
    }

    @Override
    public char readChar() {
        return this.parent.readChar();
    }

    @Override
    public float readFloat() {
        return this.parent.readFloat();
    }

    @Override
    public double readDouble() {
        return this.parent.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return this.parent.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return this.parent.readSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf buf) {
        return this.parent.readBytes(buf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf buf, int length) {
        return this.parent.readBytes(buf, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf buf, int outputIndex, int length) {
        return this.parent.readBytes(buf, outputIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes) {
        return this.parent.readBytes(bytes);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes, int outputIndex, int length) {
        return this.parent.readBytes(bytes, outputIndex, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer buf) {
        return this.parent.readBytes(buf);
    }

    @Override
    public ByteBuf readBytes(OutputStream stream, int length) throws IOException {
        return this.parent.readBytes(stream, length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return parent.readBytes(out, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return this.parent.skipBytes(length);
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return this.parent.writeBoolean(value);
    }

    @Override
    public ByteBuf writeByte(int value) {
        return this.parent.writeByte(value);
    }

    @Override
    public ByteBuf writeShort(int value) {
        return this.parent.writeShort(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return this.parent.writeMedium(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return this.parent.writeInt(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return this.parent.writeLong(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return this.parent.writeChar(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return this.parent.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return this.parent.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf) {
        return this.parent.writeBytes(buf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf, int length) {
        return this.parent.writeBytes(buf, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf buf, int sourceIndex, int length) {
        return this.parent.writeBytes(buf, sourceIndex, length);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes) {
        return this.parent.writeBytes(bytes);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes, int sourceIndex, int length) {
        return this.parent.writeBytes(bytes, sourceIndex, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer buf) {
        return this.parent.writeBytes(buf);
    }

    @Override
    public int writeBytes(InputStream stream, int length) throws IOException {
        return this.parent.writeBytes(stream, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel channel, int length) throws IOException {
        return this.parent.writeBytes(channel, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        return this.parent.writeZero(length);
    }

    @Override
    public int indexOf(int from, int to, byte value) {
        return this.parent.indexOf(from, to, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return this.parent.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return this.parent.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return this.parent.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteBufProcessor byteProcessor) {
        return this.parent.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int index, int length, ByteBufProcessor byteProcessor) {
        return this.parent.forEachByte(index, length, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor byteProcessor) {
        return this.parent.forEachByteDesc(byteProcessor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteBufProcessor byteProcessor) {
        return this.parent.forEachByteDesc(index, length, byteProcessor);
    }

    @Override
    public ByteBuf copy() {
        return this.parent.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return this.parent.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return this.parent.slice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return this.parent.slice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return this.parent.duplicate();
    }

    @Override
    public int nioBufferCount() {
        return this.parent.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return this.parent.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return this.parent.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return this.parent.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return this.parent.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return this.parent.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return this.parent.hasArray();
    }

    @Override
    public byte[] array() {
        return this.parent.array();
    }

    @Override
    public int arrayOffset() {
        return this.parent.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return this.parent.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return this.parent.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return this.parent.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return this.parent.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return this.parent.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ByteBuf) {
            return this.parent.equals(o);
        }
        return false;
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return this.parent.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return this.parent.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return this.parent.retain(i);
    }

    @Override
    public ByteBuf retain() {
        return this.parent.retain();
    }

    @Override
    public ByteBuf touch() {
        return this.parent.touch();
    }

    @Override
    public ByteBuf touch(Object object) {
        return this.parent.touch(object);
    }

    @Override
    public int refCnt() {
        return this.parent.refCnt();
    }

    @Override
    public boolean release() {
        return this.parent.release();
    }

    @Override
    public boolean release(int decrement) {
        return this.parent.release(decrement);
    }
}
