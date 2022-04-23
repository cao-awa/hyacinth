//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import com.github.cao.awa.hyacinth.network.text.LiteralText;
//import com.github.cao.awa.hyacinth.network.text.Text;
//
//public class ScoreboardObjectiveUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    public static final int ADD_MODE = 0;
//    public static final int REMOVE_MODE = 1;
//    public static final int UPDATE_MODE = 2;
//    private final String name;
//    private final Text displayName;
//    private final ScoreboardCriterion.RenderType type;
//    private final int mode;
//
//    public ScoreboardObjectiveUpdateS2CPacket(ScoreboardObjective objective, int mode) {
//        this.name = objective.getName();
//        this.displayName = objective.getDisplayName();
//        this.type = objective.getRenderType();
//        this.mode = mode;
//    }
//
//    public ScoreboardObjectiveUpdateS2CPacket(PacketByteBuf buf) {
//        this.name = buf.readString();
//        this.mode = buf.readByte();
//        if (this.mode == 0 || this.mode == 2) {
//            this.displayName = buf.readText();
//            this.type = buf.readEnumConstant(ScoreboardCriterion.RenderType.class);
//        } else {
//            this.displayName = LiteralText.EMPTY;
//            this.type = ScoreboardCriterion.RenderType.INTEGER;
//        }
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeString(this.name);
//        buf.writeByte(this.mode);
//        if (this.mode == 0 || this.mode == 2) {
//            buf.writeText(this.displayName);
//            buf.writeEnumConstant(this.type);
//        }
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public Text getDisplayName() {
//        return this.displayName;
//    }
//
//    public int getMode() {
//        return this.mode;
//    }
//
//    public ScoreboardCriterion.RenderType getType() {
//        return this.type;
//    }
//}
//
