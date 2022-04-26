package com.github.cao.awa.hyacinth.server.entity.data;

import com.github.cao.awa.hyacinth.network.packet.buf.*;

public interface TrackedDataHandler<T> {
    void write(PacketByteBuf var1, T var2);

    T read(PacketByteBuf var1);

    default TrackedData<T> create(int i) {
        return new TrackedData<>(i, this);
    }

    T copy(T var1);
}

