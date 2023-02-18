package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.entity.IEventEntity;
import org.astemir.api.network.PacketArgument;

import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageEntityEvent {

    private int entityId;

    private int eventId;

    private PacketArgument[] arguments;

    public ServerMessageEntityEvent(int entityId, int eventId, PacketArgument... arguments) {
        this.entityId = entityId;
        this.eventId = eventId;
        this.arguments = arguments;
    }

    public static void encode(ServerMessageEntityEvent message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.eventId);
        buf.writeInt(message.arguments.length);
        for (PacketArgument argument : message.arguments) {
            argument.write(buf);
        }
    }

    public static ServerMessageEntityEvent decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int eventId = buf.readInt();
        int length = buf.readInt();
        PacketArgument[] arguments = new PacketArgument[length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = PacketArgument.read(buf);
        }
        ServerMessageEntityEvent message = new ServerMessageEntityEvent(entityId,eventId,arguments);
        return message;
    }


    public static class Handler implements BiConsumer<ServerMessageEntityEvent, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ServerMessageEntityEvent message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            LogicalSide side = context.getDirection().getReceptionSide();
            context.setPacketHandled(true);

            if(!side.isServer()) {
                return;
            }

            final ServerPlayer playerEntity = context.getSender();
            if(playerEntity == null) {
                return;
            }

            context.enqueueWork(() -> processMessage(message, playerEntity));
        }


        private static void processMessage(ServerMessageEntityEvent message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                if (playerEntity.level instanceof ServerLevel serverLevel){
                    Entity entity = serverLevel.getEntity(message.entityId);
                    if (entity != null) {
                        if (entity instanceof IEventEntity) {
                            ((IEventEntity) entity).onHandleServerEvent(message.eventId,message.arguments);
                        }
                    }
                }
            }
        }
    }
}
