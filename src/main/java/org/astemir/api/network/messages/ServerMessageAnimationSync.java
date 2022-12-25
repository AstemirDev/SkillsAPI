package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.*;


import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageAnimationSync {

    private IAnimatedKey targetId;

    public ServerMessageAnimationSync(IAnimatedKey id) {
        this.targetId = id;
    }


    public static void encode(ServerMessageAnimationSync message, FriendlyByteBuf buf) {
        message.targetId.write(buf);
    }

    public static ServerMessageAnimationSync decode(FriendlyByteBuf buf) {
        return new ServerMessageAnimationSync(IAnimatedKey.read(buf));
    }


    public static class Handler implements BiConsumer<ServerMessageAnimationSync, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ServerMessageAnimationSync message, Supplier<NetworkEvent.Context> contextSupplier) {
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


        private static void processMessage(ServerMessageAnimationSync message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                AnimationFactory factory = null;
                switch (message.targetId.getTarget()){
                    case ENTITY:{
                        if (playerEntity.level.getEntity(message.targetId.getId()) instanceof IAnimated animatedEntity){
                            factory = animatedEntity.getAnimationFactory();
                        }
                        break;
                    }
                    case BLOCK:{
                        BlockEntity blockEntity = playerEntity.level.getBlockEntity(message.targetId.getPos());
                        if (blockEntity instanceof IAnimated){
                            factory = ((IAnimated) blockEntity).getAnimationFactory();
                        }
                    }
                }
                if (factory != null){
                    for (Animation playingAnimation : factory.getPlayingAnimations()) {
                        AnimationHandler.INSTANCE.sendServerSyncMessage(playerEntity,factory,playingAnimation,message.targetId, ClientMessageAnimation.Action.START,factory.getAnimationTick(playingAnimation));
                    }
                }
            }
        }
    }
}
