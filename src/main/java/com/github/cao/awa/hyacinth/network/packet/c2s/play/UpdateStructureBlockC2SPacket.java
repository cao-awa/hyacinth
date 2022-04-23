package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.block.rotation.BlockRotation;
import com.github.cao.awa.hyacinth.math.vec.Vec3i;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

//public class UpdateStructureBlockC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private static final int IGNORE_ENTITIES_MASK = 1;
//    private static final int SHOW_AIR_MASK = 2;
//    private static final int SHOW_BOUNDING_BOX_MASK = 4;
//    private final BlockPos pos;
//    private final StructureBlockBlockEntity.Action action;
//    private final StructureBlockMode mode;
//    private final String structureName;
//    private final BlockPos offset;
//    private final Vec3i size;
//    private final BlockMirror mirror;
//    private final BlockRotation rotation;
//    private final String metadata;
//    private final boolean ignoreEntities;
//    private final boolean showAir;
//    private final boolean showBoundingBox;
//    private final float integrity;
//    private final long seed;
//
//    public UpdateStructureBlockC2SPacket(BlockPos pos, StructureBlockBlockEntity.Action action, StructureBlockMode mode, String structureName, BlockPos offset, Vec3i size, BlockMirror mirror, BlockRotation rotation, String metadata, boolean ignoreEntities, boolean showAir, boolean showBoundingBox, float integrity, long seed) {
//        this.pos = pos;
//        this.action = action;
//        this.mode = mode;
//        this.structureName = structureName;
//        this.offset = offset;
//        this.size = size;
//        this.mirror = mirror;
//        this.rotation = rotation;
//        this.metadata = metadata;
//        this.ignoreEntities = ignoreEntities;
//        this.showAir = showAir;
//        this.showBoundingBox = showBoundingBox;
//        this.integrity = integrity;
//        this.seed = seed;
//    }
//
//    public UpdateStructureBlockC2SPacket(PacketByteBuf buf) {
//        this.pos = buf.readBlockPos();
//        this.action = buf.readEnumConstant(StructureBlockBlockEntity.Action.class);
//        this.mode = buf.readEnumConstant(StructureBlockMode.class);
//        this.structureName = buf.readString();
//        int i = 48;
//        this.offset = new BlockPos(MathHelper.clamp((int)buf.readByte(), -48, 48), MathHelper.clamp((int)buf.readByte(), -48, 48), MathHelper.clamp((int)buf.readByte(), -48, 48));
//        int j = 48;
//        this.size = new Vec3i(MathHelper.clamp((int)buf.readByte(), 0, 48), MathHelper.clamp((int)buf.readByte(), 0, 48), MathHelper.clamp((int)buf.readByte(), 0, 48));
//        this.mirror = buf.readEnumConstant(BlockMirror.class);
//        this.rotation = buf.readEnumConstant(BlockRotation.class);
//        this.metadata = buf.readString(128);
//        this.integrity = MathHelper.clamp(buf.readFloat(), 0.0f, 1.0f);
//        this.seed = buf.readVarLong();
//        byte k = buf.readByte();
//        this.ignoreEntities = (k & 1) != 0;
//        this.showAir = (k & 2) != 0;
//        this.showBoundingBox = (k & 4) != 0;
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeBlockPos(this.pos);
//        buf.writeEnumConstant(this.action);
//        buf.writeEnumConstant(this.mode);
//        buf.writeString(this.structureName);
//        buf.writeByte(this.offset.getX());
//        buf.writeByte(this.offset.getY());
//        buf.writeByte(this.offset.getZ());
//        buf.writeByte(this.size.getX());
//        buf.writeByte(this.size.getY());
//        buf.writeByte(this.size.getZ());
//        buf.writeEnumConstant(this.mirror);
//        buf.writeEnumConstant(this.rotation);
//        buf.writeString(this.metadata);
//        buf.writeFloat(this.integrity);
//        buf.writeVarLong(this.seed);
//        int i = 0;
//        if (this.ignoreEntities) {
//            i |= 1;
//        }
//        if (this.showAir) {
//            i |= 2;
//        }
//        if (this.showBoundingBox) {
//            i |= 4;
//        }
//        buf.writeByte(i);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onUpdateStructureBlock(this);
//    }
//
//    public BlockPos getPos() {
//        return this.pos;
//    }
//
//    public StructureBlockBlockEntity.Action getAction() {
//        return this.action;
//    }
//
//    public StructureBlockMode getMode() {
//        return this.mode;
//    }
//
//    public String getStructureName() {
//        return this.structureName;
//    }
//
//    public BlockPos getOffset() {
//        return this.offset;
//    }
//
//    public Vec3i getSize() {
//        return this.size;
//    }
//
//    public BlockMirror getMirror() {
//        return this.mirror;
//    }
//
//    public BlockRotation getRotation() {
//        return this.rotation;
//    }
//
//    public String getMetadata() {
//        return this.metadata;
//    }
//
//    public boolean shouldIgnoreEntities() {
//        return this.ignoreEntities;
//    }
//
//    public boolean shouldShowAir() {
//        return this.showAir;
//    }
//
//    public boolean shouldShowBoundingBox() {
//        return this.showBoundingBox;
//    }
//
//    public float getIntegrity() {
//        return this.integrity;
//    }
//
//    public long getSeed() {
//        return this.seed;
//    }
//}
//
