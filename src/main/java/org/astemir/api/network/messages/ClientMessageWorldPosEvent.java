package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.handler.WorldEventHandler;
import org.astemir.api.network.PacketArgument;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientMessageWorldPosEvent {

    private BlockPos pos;

    private int eventId;

    private PacketArgument[] arguments;

    public ClientMessageWorldPosEvent(BlockPos pos, int eventId, PacketArgument... arguments) {
        this.pos = pos;
        this.eventId = eventId;
        this.arguments = arguments;
    }

    public static void encode(ClientMessageWorldPosEvent message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeInt(message.eventId);
        buf.writeInt(message.arguments.length);
        for (PacketArgument argument : message.arguments) {
            argument.write(buf);
        }
    }

    public static ClientMessageWorldPosEvent decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int eventId = buf.readInt();
        int length = buf.readInt();
        PacketArgument[] arguments = new PacketArgument[length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = PacketArgument.read(buf);
        }
        ClientMessageWorldPosEvent message = new ClientMessageWorldPosEvent(pos,eventId,arguments);
        return message;
    }


    public static class Handler implements BiConsumer<ClientMessageWorldPosEvent, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ClientMessageWorldPosEvent message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                WorldEventHandler.getInstance().onClientHandleEvent(message.pos,message.eventId,message.arguments);
            });
            context.setPacketHandled(true);
        }
    }
}
