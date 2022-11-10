package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.item.IBlockClickListener;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockClickMessage {


    private BlockPos pos;

    public BlockClickMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(BlockClickMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static BlockClickMessage decode(FriendlyByteBuf buf) {
        BlockClickMessage message = new BlockClickMessage(buf.readBlockPos());
        return message;
    }


    public static class Handler implements BiConsumer<BlockClickMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(BlockClickMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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


        private static void processMessage(BlockClickMessage message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                ItemStack itemStack = playerEntity.getItemInHand(InteractionHand.MAIN_HAND);
                if (itemStack.getItem() instanceof IBlockClickListener){
                    ((IBlockClickListener)itemStack.getItem()).onBlockClick(itemStack,playerEntity,message.pos);
                }
            }
        }
    }
}
