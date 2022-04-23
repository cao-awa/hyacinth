package com.github.cao.awa.hyacinth.server.world.listener;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.server.entity.player.ServerPlayerEntity;

/**
 * A listener to entity tracking within threaded anvil chunk storage.
 */
public interface EntityTrackingListener {
    ServerPlayerEntity getPlayer();

    void sendPacket(Packet<?> var1);
}

