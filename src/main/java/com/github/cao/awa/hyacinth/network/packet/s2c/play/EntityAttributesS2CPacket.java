package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.google.common.collect.Lists;

import java.rmi.registry.Registry;
import java.util.List;

//public class EntityAttributesS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int entityId;
//    private final List<Entry> entries;
//
//    public EntityAttributesS2CPacket(int entityId, Collection<EntityAttributeInstance> attributes) {
//        this.entityId = entityId;
//        this.entries = Lists.newArrayList();
//        for (EntityAttributeInstance entityAttributeInstance : attributes) {
//            this.entries.add(new Entry(entityAttributeInstance.getAttribute(), entityAttributeInstance.getBaseValue(), entityAttributeInstance.getModifiers()));
//        }
//    }
//
//    public EntityAttributesS2CPacket(PacketByteBuf buf2) {
//        this.entityId = buf2.readVarInt();
//        this.entries = buf2.readList(buf -> {
//            Identifier identifier = buf.readIdentifier();
//            EntityAttribute entityAttribute = Registry.ATTRIBUTE.get(identifier);
//            double d = buf.readDouble();
//            List<EntityAttributeModifier> list = buf.readList(modifiers -> new EntityAttributeModifier(modifiers.readUuid(), "Unknown synced attribute modifier", modifiers.readDouble(), EntityAttributeModifier.Operation.fromId(modifiers.readByte())));
//            return new Entry(entityAttribute, d, list);
//        });
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entityId);
//        buf.writeCollection(this.entries, (buf2, attribute) -> {
//            buf2.writeIdentifier(Registry.ATTRIBUTE.getId(attribute.getId()));
//            buf2.writeDouble(attribute.getBaseValue());
//            buf2.writeCollection(attribute.getModifiers(), (buf, modifier) -> {
//                buf.writeUuid(modifier.getId());
//                buf.writeDouble(modifier.getValue());
//                buf.writeByte(modifier.getOperation().getId());
//            });
//        });
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getEntityId() {
//        return this.entityId;
//    }
//
//    public List<Entry> getEntries() {
//        return this.entries;
//    }
//
//    public static class Entry {
//        private final EntityAttribute attribute;
//        private final double baseValue;
//        private final Collection<EntityAttributeModifier> modifiers;
//
//        public Entry(EntityAttribute attribute, double baseValue, Collection<EntityAttributeModifier> modifiers) {
//            this.attribute = attribute;
//            this.baseValue = baseValue;
//            this.modifiers = modifiers;
//        }
//
//        public EntityAttribute getId() {
//            return this.attribute;
//        }
//
//        public double getBaseValue() {
//            return this.baseValue;
//        }
//
//        public Collection<EntityAttributeModifier> getModifiers() {
//            return this.modifiers;
//        }
//    }
//}

