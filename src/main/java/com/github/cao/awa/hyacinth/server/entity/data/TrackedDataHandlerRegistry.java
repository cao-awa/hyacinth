package com.github.cao.awa.hyacinth.server.entity.data;

import com.github.cao.awa.hyacinth.math.block.*;
import com.github.cao.awa.hyacinth.math.direction.*;
import com.github.cao.awa.hyacinth.network.packet.buf.*;
import com.github.cao.awa.hyacinth.network.text.*;
import com.github.cao.awa.hyacinth.server.block.*;
import com.github.cao.awa.hyacinth.server.entity.pose.*;
import net.minecraft.nbt.*;
import net.minecraft.util.collection.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TrackedDataHandlerRegistry {
    private static final Int2ObjectBiMap<TrackedDataHandler<?>> DATA_HANDLERS = Int2ObjectBiMap.create(16);
    public static final TrackedDataHandler<Byte> BYTE = new TrackedDataHandler<>() {

        @Override
        public void write(PacketByteBuf packetByteBuf, Byte byte_) {
            packetByteBuf.writeByte(byte_);
        }

        @Override
        public Byte read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readByte();
        }

        @Override
        public Byte copy(Byte byte_) {
            return byte_;
        }
    };
    public static final TrackedDataHandler<Integer> INTEGER = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Integer integer) {
            packetByteBuf.writeVarInt(integer);
        }

        @Override
        public Integer read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readVarInt();
        }

        @Override
        public Integer copy(Integer integer) {
            return integer;
        }
    };
    public static final TrackedDataHandler<Float> FLOAT = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Float float_) {
            packetByteBuf.writeFloat(float_);
        }

        @Override
        public Float read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readFloat();
        }

        @Override
        public Float copy(Float float_) {
            return float_;
        }
    };
    public static final TrackedDataHandler<String> STRING = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, String string) {
            packetByteBuf.writeString(string);
        }

        @Override
        public String read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readString();
        }

        @Override
        public String copy(String string) {
            return string;
        }
    };
    public static final TrackedDataHandler<Text> TEXT_COMPONENT = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Text text) {
            packetByteBuf.writeText(text);
        }

        @Override
        public Text read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readText();
        }

        @Override
        public Text copy(Text text) {
            return text;
        }
    };
    public static final TrackedDataHandler<Optional<Text>> OPTIONAL_TEXT_COMPONENT = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Optional<Text> optional) {
            if (optional.isPresent()) {
                packetByteBuf.writeBoolean(true);
                packetByteBuf.writeText(optional.get());
            } else {
                packetByteBuf.writeBoolean(false);
            }
        }

        @Override
        public Optional<Text> read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readBoolean() ? Optional.of(packetByteBuf.readText()) : Optional.empty();
        }

        @Override
        public Optional<Text> copy(Optional<Text> optional) {
            return optional;
        }
    };
//    public static final TrackedDataHandler<ItemStack> ITEM_STACK = new TrackedDataHandler<>(){
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, ItemStack itemStack) {
//            packetByteBuf.writeItemStack(itemStack);
//        }
//
//        @Override
//        public ItemStack read(PacketByteBuf packetByteBuf) {
//            return packetByteBuf.readItemStack();
//        }
//
//        @Override
//        public ItemStack copy(ItemStack itemStack) {
//            return itemStack.copy();
//        }
//    };
//    public static final TrackedDataHandler<Optional<BlockState>> OPTIONAL_BLOCK_STATE = new TrackedDataHandler<>(){
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, Optional<BlockState> optional) {
//            if (optional.isPresent()) {
//                packetByteBuf.writeVarInt(Block.getRawIdFromState(optional.get()));
//            } else {
//                packetByteBuf.writeVarInt(0);
//            }
//        }
//
//        @Override
//        public Optional<BlockState> read(PacketByteBuf packetByteBuf) {
//            int i = packetByteBuf.readVarInt();
//            if (i == 0) {
//                return Optional.empty();
//            }
//            return Optional.of(Block.getStateFromRawId(i));
//        }
//
//        @Override
//        public Optional<BlockState> copy(Optional<BlockState> optional) {
//            return optional;
//        }
//    };
//    public static final TrackedDataHandler<Boolean> BOOLEAN = new TrackedDataHandler<>(){
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, Boolean boolean_) {
//            packetByteBuf.writeBoolean(boolean_);
//        }
//
//        @Override
//        public Boolean read(PacketByteBuf packetByteBuf) {
//            return packetByteBuf.readBoolean();
//        }
//
//        @Override
//        public Boolean copy(Boolean boolean_) {
//            return boolean_;
//        }
//    };
//    public static final TrackedDataHandler<ParticleEffect> PARTICLE = new TrackedDataHandler<>(){
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, ParticleEffect particleEffect) {
//            packetByteBuf.writeVarInt(Registry.PARTICLE_TYPE.getRawId(particleEffect.getType()));
//            particleEffect.write(packetByteBuf);
//        }
//
//        @Override
//        public ParticleEffect read(PacketByteBuf packetByteBuf) {
//            return this.read(packetByteBuf, Objects.requireNonNull(Registry.PARTICLE_TYPE.get(packetByteBuf.readVarInt())));
//        }
//
//        private <T extends ParticleEffect> T read(PacketByteBuf buf, ParticleType<T> type) {
//            return type.getParametersFactory().read(type, buf);
//        }
//
//        @Override
//        public ParticleEffect copy(ParticleEffect particleEffect) {
//            return particleEffect;
//        }
//    };
//    public static final TrackedDataHandler<EulerAngle> ROTATION = new TrackedDataHandler<>() {
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, EulerAngle eulerAngle) {
//            packetByteBuf.writeFloat(eulerAngle.getPitch());
//            packetByteBuf.writeFloat(eulerAngle.getYaw());
//            packetByteBuf.writeFloat(eulerAngle.getRoll());
//        }
//
//        @Override
//        public EulerAngle read(PacketByteBuf packetByteBuf) {
//            return new EulerAngle(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
//        }
//
//        @Override
//        public EulerAngle copy(EulerAngle eulerAngle) {
//            return eulerAngle;
//        }
//    };
    public static final TrackedDataHandler<BlockPos> BLOCK_POS = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, BlockPos blockPos) {
            packetByteBuf.writeBlockPos(blockPos);
        }

        @Override
        public BlockPos read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readBlockPos();
        }

        @Override
        public BlockPos copy(BlockPos blockPos) {
            return blockPos;
        }
    };
    public static final TrackedDataHandler<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Optional<BlockPos> optional) {
            packetByteBuf.writeBoolean(optional.isPresent());
            optional.ifPresent(packetByteBuf::writeBlockPos);
        }

        @Override
        public Optional<BlockPos> read(PacketByteBuf packetByteBuf) {
            if (!packetByteBuf.readBoolean()) {
                return Optional.empty();
            }
            return Optional.of(packetByteBuf.readBlockPos());
        }

        @Override
        public Optional<BlockPos> copy(Optional<BlockPos> optional) {
            return optional;
        }
    };
    public static final TrackedDataHandler<Direction> FACING = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Direction direction) {
            packetByteBuf.writeEnumConstant(direction);
        }

        @Override
        public Direction read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readEnumConstant(Direction.class);
        }

        @Override
        public Direction copy(Direction direction) {
            return direction;
        }
    };
    public static final TrackedDataHandler<Optional<UUID>> OPTIONAL_UUID = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, Optional<UUID> optional) {
            packetByteBuf.writeBoolean(optional.isPresent());
            optional.ifPresent(packetByteBuf::writeUuid);
        }

        @Override
        public Optional<UUID> read(PacketByteBuf packetByteBuf) {
            if (!packetByteBuf.readBoolean()) {
                return Optional.empty();
            }
            return Optional.of(packetByteBuf.readUuid());
        }

        @Override
        public Optional<UUID> copy(Optional<UUID> optional) {
            return optional;
        }
    };
    public static final TrackedDataHandler<NbtCompound> TAG_COMPOUND = new TrackedDataHandler<>() {

        @Override
        public void write(PacketByteBuf packetByteBuf, NbtCompound nbtCompound) {
            packetByteBuf.writeNbt(nbtCompound);
        }

        @Override
        public NbtCompound read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readNbt();
        }

        @Override
        public NbtCompound copy(NbtCompound nbtCompound) {
            return nbtCompound.copy();
        }
    };
//    public static final TrackedDataHandler<VillagerData> VILLAGER_DATA = new TrackedDataHandler<>() {
//
//        @Override
//        public void write(PacketByteBuf packetByteBuf, VillagerData villagerData) {
//            packetByteBuf.writeVarInt(Registry.VILLAGER_TYPE.getRawId(villagerData.getType()));
//            packetByteBuf.writeVarInt(Registry.VILLAGER_PROFESSION.getRawId(villagerData.getProfession()));
//            packetByteBuf.writeVarInt(villagerData.getLevel());
//        }
//
//        @Override
//        public VillagerData read(PacketByteBuf packetByteBuf) {
//            return new VillagerData(Registry.VILLAGER_TYPE.get(packetByteBuf.readVarInt()), Registry.VILLAGER_PROFESSION.get(packetByteBuf.readVarInt()), packetByteBuf.readVarInt());
//        }
//
//        @Override
//        public VillagerData copy(VillagerData villagerData) {
//            return villagerData;
//        }
//    };
    public static final TrackedDataHandler<OptionalInt> FIREWORK_DATA = new TrackedDataHandler<>() {

        @Override
        public void write(PacketByteBuf packetByteBuf, OptionalInt optionalInt) {
            packetByteBuf.writeVarInt(optionalInt.orElse(- 1) + 1);
        }

        @Override
        public OptionalInt read(PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            return i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1);
        }

        @Override
        public OptionalInt copy(OptionalInt optionalInt) {
            return optionalInt;
        }
    };
    public static final TrackedDataHandler<EntityPose> ENTITY_POSE = new TrackedDataHandler<>() {

        @Override
        public void write(PacketByteBuf packetByteBuf, EntityPose entityPose) {
            packetByteBuf.writeEnumConstant(entityPose);
        }

        @Override
        public EntityPose read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readEnumConstant(EntityPose.class);
        }

        @Override
        public EntityPose copy(EntityPose entityPose) {
            return entityPose;
        }
    };

    public static void register(TrackedDataHandler<?> handler) {
        DATA_HANDLERS.add(handler);
    }

    @Nullable
    public static TrackedDataHandler<?> get(int id) {
        return DATA_HANDLERS.get(id);
    }

    public static int getId(TrackedDataHandler<?> handler) {
        return DATA_HANDLERS.getRawId(handler);
    }

    private TrackedDataHandlerRegistry() {
    }

    static {
        TrackedDataHandlerRegistry.register(BYTE);
        TrackedDataHandlerRegistry.register(INTEGER);
        TrackedDataHandlerRegistry.register(FLOAT);
        TrackedDataHandlerRegistry.register(STRING);
        TrackedDataHandlerRegistry.register(TEXT_COMPONENT);
        TrackedDataHandlerRegistry.register(OPTIONAL_TEXT_COMPONENT);
//        TrackedDataHandlerRegistry.register(ITEM_STACK);
//        TrackedDataHandlerRegistry.register(BOOLEAN);
//        TrackedDataHandlerRegistry.register(ROTATION);
        TrackedDataHandlerRegistry.register(BLOCK_POS);
        TrackedDataHandlerRegistry.register(OPTIONAL_BLOCK_POS);
        TrackedDataHandlerRegistry.register(FACING);
        TrackedDataHandlerRegistry.register(OPTIONAL_UUID);
//        TrackedDataHandlerRegistry.register(OPTIONAL_BLOCK_STATE);
        TrackedDataHandlerRegistry.register(TAG_COMPOUND);
//        TrackedDataHandlerRegistry.register(PARTICLE);
//        TrackedDataHandlerRegistry.register(VILLAGER_DATA);
        TrackedDataHandlerRegistry.register(FIREWORK_DATA);
        TrackedDataHandlerRegistry.register(ENTITY_POSE);
    }
}


