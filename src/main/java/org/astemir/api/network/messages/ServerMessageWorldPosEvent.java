package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.misc.WorldEventHandler;
import org.astemir.api.network.PacketArgument;

import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageWorldPosEvent {

    private BlockPos pos;

    private int eventId;

    private PacketArgument[] arguments;

    public ServerMessageWorldPosEvent(BlockPos pos, int eventId, PacketArgument... arguments) {
        this.pos = pos;
        this.eventId = eventId;
        this.arguments = arguments;
    }

    public static void encode(ServerMessageWorldPosEvent message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeInt(message.eventId);
        buf.writeInt(message.arguments.length);
        for (PacketArgument argument : message.arguments) {
            argument.write(buf);
        }
    }

    public static ServerMessageWorldPosEvent decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int eventId = buf.readInt();
        int length = buf.readInt();
        PacketArgument[] arguments = new PacketArgument[length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = PacketArgument.read(buf);
        }
        ServerMessageWorldPosEvent message = new ServerMessageWorldPosEvent(pos,eventId,arguments);
        return message;
    }


    public static class Handler implements BiConsumer<ServerMessageWorldPosEvent, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ServerMessageWorldPosEvent message, Supplier<NetworkEvent.Context> contextSupplier) {
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


        private static void processMessage(ServerMessageWorldPosEvent message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                if (playerEntity.level instanceof ServerLevel serverLevel){
                    WorldEventHandler.getInstance().onServerHandleEvent(serverLevel,message.pos,message.eventId,message.arguments);
                }
            }
        }
    }
}
