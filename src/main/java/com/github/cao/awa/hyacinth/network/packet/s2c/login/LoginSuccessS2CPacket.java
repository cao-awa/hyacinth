package com.github.cao.awa.hyacinth.network.packet.s2c.login;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.ClientLoginPacketListener;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.dynamic.DynamicSerializableUuid;

import java.util.UUID;

public class LoginSuccessS2CPacket
        implements Packet<ClientLoginPacketListener> {
    private final GameProfile profile;

    public LoginSuccessS2CPacket(GameProfile profile) {
        this.profile = profile;
    }

    public LoginSuccessS2CPacket(PacketByteBuf buf) {
        int[] is = new int[4];
        for (int i = 0; i < is.length; ++i) {
            is[i] = buf.readInt();
        }
        UUID i = DynamicSerializableUuid.toUuid(is);
        String string = buf.readString(16);
        this.profile = new GameProfile(i, string);
    }

    @Override
    public void write(PacketByteBuf buf) {
        for (int i : DynamicSerializableUuid.toIntArray(this.profile.getId())) {
            buf.writeInt(i);
        }
        buf.writeString(this.profile.getName());
    }

    @Override
    public void apply(ClientLoginPacketListener clientLoginPacketListener) {
        clientLoginPacketListener.onSuccess(this);
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}


