package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ServerPlayerInteractMessage {


    private InteractionHand hand;
    private BlockPos pos;

    public ServerPlayerInteractMessage(InteractionHand hand, BlockPos pos) {
        this.hand = hand;
        this.pos = pos;
    }

    public static void encode(ServerPlayerInteractMessage message, FriendlyByteBuf buf) {
        buf.writeEnum(message.hand);
        buf.writeBlockPos(message.pos);
    }

    public static ServerPlayerInteractMessage decode(FriendlyByteBuf buf) {
        ServerPlayerInteractMessage message = new ServerPlayerInteractMessage(buf.readEnum(InteractionHand.class),buf.readBlockPos());
        return message;
    }


    public static class Handler implements BiConsumer<ServerPlayerInteractMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ServerPlayerInteractMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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


        private static void processMessage(ServerPlayerInteractMessage message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                ItemStack itemStack = playerEntity.getItemInHand(message.hand);

            }
        }
    }
}
