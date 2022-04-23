package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;

public class UpdateDifficultyLockC2SPacket
implements Packet<ServerPlayPacketListener> {
    private final boolean difficultyLocked;

    public UpdateDifficultyLockC2SPacket(boolean difficultyLocked) {
        this.difficultyLocked = difficultyLocked;
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onUpdateDifficultyLock(this);
    }

    public UpdateDifficultyLockC2SPacket(PacketByteBuf buf) {
        this.difficultyLocked = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.difficultyLocked);
    }

    public boolean isDifficultyLocked() {
        return this.difficultyLocked;
    }
}

