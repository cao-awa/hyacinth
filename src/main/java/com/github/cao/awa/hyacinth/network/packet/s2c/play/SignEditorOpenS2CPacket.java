package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class SignEditorOpenS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final BlockPos pos;

    public SignEditorOpenS2CPacket(BlockPos pos) {
        this.pos = pos;
    }

    public SignEditorOpenS2CPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

