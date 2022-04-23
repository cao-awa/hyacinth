package com.github.cao.awa.hyacinth.network.packet;

import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;

public interface Packet<T extends PacketListener> {
    void write(PacketByteBuf var1);

    void apply(T var1);

    /**
     * Returns whether a throwable in writing of this packet allows the
     * connection to simply skip the packet's sending than disconnecting.
     */
    default boolean isWritingErrorSkippable() {
        return false;
    }
}

