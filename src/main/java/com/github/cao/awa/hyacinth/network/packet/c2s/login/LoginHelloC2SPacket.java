package com.github.cao.awa.hyacinth.network.packet.c2s.login;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.ServerLoginPacketListener;
import com.mojang.authlib.GameProfile;

public class LoginHelloC2SPacket
        implements Packet<ServerLoginPacketListener> {
    private final GameProfile profile;

    public LoginHelloC2SPacket(GameProfile profile) {
        this.profile = profile;
    }

    public LoginHelloC2SPacket(PacketByteBuf buf) {
        this.profile = new GameProfile(null, buf.readString(16));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.profile.getName());
    }

    @Override
    public void apply(ServerLoginPacketListener serverLoginPacketListener) {
        serverLoginPacketListener.onHello(this);
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}

