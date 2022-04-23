package com.github.cao.awa.hyacinth.network.connection;

import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.state.NetworkSide;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A connection that disconnects from the backing netty channel if too
 * many packets are received.
 */
public class RateLimitedConnection extends ClientConnection {
    private static final Logger LOGGER = LogManager.getLogger("Connection");
    private static final Text RATE_LIMIT_EXCEEDED_MESSAGE = new TranslatableText("disconnect.exceeded_packet_rate");
    private final int rateLimit;

    public RateLimitedConnection(int rateLimit) {
        super(NetworkSide.SERVERBOUND);
        this.rateLimit = rateLimit;
    }

    @Override
    protected void updateStats() {
        super.updateStats();
        float f = getAveragePacketsReceived();
        if (f > (float) this.rateLimit) {
            LOGGER.warn("Player exceeded rate-limit (sent {} packets per second)", f);
            this.send(new DisconnectS2CPacket(RATE_LIMIT_EXCEEDED_MESSAGE), future -> this.disconnect(RATE_LIMIT_EXCEEDED_MESSAGE));
            this.disableAutoRead();
        }
    }
}

