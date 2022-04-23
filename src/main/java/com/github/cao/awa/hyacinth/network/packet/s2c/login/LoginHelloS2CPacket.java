package com.github.cao.awa.hyacinth.network.packet.s2c.login;

import com.github.cao.awa.hyacinth.network.encryption.NetworkEncryptionException;
import com.github.cao.awa.hyacinth.network.encryption.NetworkEncryptionUtils;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.ClientLoginPacketListener;

import java.security.PublicKey;

public class LoginHelloS2CPacket
        implements Packet<ClientLoginPacketListener> {
    private final String serverId;
    private final byte[] publicKey;
    private final byte[] nonce;

    public LoginHelloS2CPacket(String serverId, byte[] publicKey, byte[] nonce) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
    }

    public LoginHelloS2CPacket(PacketByteBuf buf) {
        this.serverId = buf.readString(20);
        this.publicKey = buf.readByteArray();
        this.nonce = buf.readByteArray();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.serverId);
        buf.writeByteArray(this.publicKey);
        buf.writeByteArray(this.nonce);
    }

    @Override
    public void apply(ClientLoginPacketListener clientLoginPacketListener) {
        clientLoginPacketListener.onHello(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() throws NetworkEncryptionException {
        return NetworkEncryptionUtils.readEncodedPublicKey(this.publicKey);
    }

    public byte[] getNonce() {
        return this.nonce;
    }
}

