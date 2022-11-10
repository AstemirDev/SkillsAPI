package org.astemir.api.network.messages;


import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.entity.IEventEntity;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EntityEventMessage {

    private int entityId;

    private int eventId;

    public EntityEventMessage(int entityId, int eventId) {
        this.entityId = entityId;
        this.eventId = eventId;
    }

    public static void encode(EntityEventMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.eventId);
    }

    public static EntityEventMessage decode(FriendlyByteBuf buf) {
        EntityEventMessage message = new EntityEventMessage(buf.readInt(), buf.readInt());
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
                        ((IEventEntity) entity).onHandleClientEvent(message.eventId);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
