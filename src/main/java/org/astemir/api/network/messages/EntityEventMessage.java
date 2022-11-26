package org.astemir.api.network.messages;


import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.entity.IEventEntity;
import org.astemir.api.network.PacketArgument;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EntityEventMessage {

    private int entityId;

    private int eventId;

    private PacketArgument[] arguments;

    public EntityEventMessage(int entityId, int eventId,PacketArgument... arguments) {
        this.entityId = entityId;
        this.eventId = eventId;
        this.arguments = arguments;
    }

    public static void encode(EntityEventMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.eventId);
        buf.writeInt(message.arguments.length);
        for (PacketArgument argument : message.arguments) {
            argument.write(buf);
        }
    }

    public static EntityEventMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int eventId = buf.readInt();
        int length = buf.readInt();
        PacketArgument[] arguments = new PacketArgument[length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = PacketArgument.read(buf);
        }
        EntityEventMessage message = new EntityEventMessage(entityId,eventId,arguments);
        return message;
    }


    public static class Handler implements BiConsumer<EntityEventMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(EntityEventMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().level.getEntity(message.entityId);
                if (entity != null) {
                    if (entity instanceof IEventEntity) {
                        ((IEventEntity) entity).onHandleClientEvent(message.eventId,message.arguments);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
