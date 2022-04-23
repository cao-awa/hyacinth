package com.github.cao.awa.hyacinth.network.handler.encode;

import io.netty.handler.codec.EncoderException;

public class PacketEncoderException extends EncoderException {
    public PacketEncoderException(Throwable cause) {
        super(cause);
    }
}

