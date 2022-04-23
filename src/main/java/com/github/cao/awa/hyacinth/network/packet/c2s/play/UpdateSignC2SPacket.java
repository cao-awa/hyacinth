package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class UpdateSignC2SPacket
implements Packet<ServerPlayPacketListener> {
    private static final int MAX_LINE_LENGTH = 384;
    private final BlockPos pos;
    private final String[] text;

    public UpdateSignC2SPacket(BlockPos pos, String line1, String line2, String line3, String line4) {
        this.pos = pos;
        this.text = new String[]{line1, line2, line3, line4};
    }

    public UpdateSignC2SPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.text = new String[4];
        for (int i = 0; i < 4; ++i) {
            this.text[i] = buf.readString(384);
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        for (int i = 0; i < 4; ++i) {
            buf.writeString(this.text[i]);
        }
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onUpdateSign(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public String[] getText() {
        return this.text;
    }
}

