package com.github.cao.awa.hyacinth.network.state;

public enum NetworkSide {
    SERVERBOUND,
    CLIENTBOUND;


    public NetworkSide getOpposite() {
        return this == CLIENTBOUND ? SERVERBOUND : CLIENTBOUND;
    }
}


