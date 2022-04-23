package com.github.cao.awa.hyacinth.network.packet.listener;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.text.Text;

public interface PacketListener {
    /**
     * Called when the connection this listener listens to has disconnected.
     * Can be used to display the disconnection reason.
     *
     * @param reason the reason of disconnection; may be a generic message
     */
    void onDisconnected(Text reason);

    /**
     * Returns the connection this packet listener intends to listen to.
     *
     * @apiNote The returned connection may or may not have this listener as
     * its current packet listener.
     *
     * @see ClientConnection#getPacketListener()
     */
    ClientConnection getConnection();
}

