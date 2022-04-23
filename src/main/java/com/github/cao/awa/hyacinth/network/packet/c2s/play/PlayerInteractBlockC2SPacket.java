package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.server.block.hit.BlockHitResult;

//public class PlayerInteractBlockC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final BlockHitResult blockHitResult;
//    private final Hand hand;
//
//    public PlayerInteractBlockC2SPacket(Hand hand, BlockHitResult blockHitResult) {
//        this.hand = hand;
//        this.blockHitResult = blockHitResult;
//    }
//
//    public PlayerInteractBlockC2SPacket(PacketByteBuf buf) {
//        this.hand = buf.readEnumConstant(Hand.class);
//        this.blockHitResult = buf.readBlockHitResult();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeEnumConstant(this.hand);
//        buf.writeBlockHitResult(this.blockHitResult);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onPlayerInteractBlock(this);
//    }
//
//    public Hand getHand() {
//        return this.hand;
//    }
//
//    public BlockHitResult getBlockHitResult() {
//        return this.blockHitResult;
//    }
//}
//
