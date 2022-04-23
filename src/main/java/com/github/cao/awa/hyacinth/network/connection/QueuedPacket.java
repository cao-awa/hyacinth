package com.github.cao.awa.hyacinth.network.connection;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.Nullable;

public class QueuedPacket {
    final Packet<?> packet;
    @Nullable
    final GenericFutureListener<? extends Future<? super Void>> callback;

    public QueuedPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback) {
        this.packet = packet;
        this.callback = callback;
    }
}
