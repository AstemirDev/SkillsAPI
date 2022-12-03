package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.AnimationHandler;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.network.AnimationTarget;
import org.astemir.api.network.PacketUtils;


import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageAnimationSync {

    private UUID uuid;
    private BlockPos pos;
    private AnimationTarget target;

    public ServerMessageAnimationSync(AnimationTarget target, UUID uuid) {
        this.uuid = uuid;
        this.target = target;
    }

    public ServerMessageAnimationSync(AnimationTarget target, BlockPos pos) {
        this.pos = pos;
        this.target = target;
    }

    public static void encode(ServerMessageAnimationSync message, FriendlyByteBuf buf) {
        buf.writeEnum(message.target);
        if (message.target == AnimationTarget.ENTITY) {
            buf.writeUUID(message.uuid);
        }else
        if (message.target == AnimationTarget.BLOCK){
            buf.writeBlockPos(message.pos);
        }
    }

    public static ServerMessageAnimationSync decode(FriendlyByteBuf buf) {
        AnimationTarget target = buf.readEnum(AnimationTarget.class);
        if (target == AnimationTarget.ENTITY) {
            return new ServerMessageAnimationSync(target, buf.readUUID());
        }else
        if (target == AnimationTarget.BLOCK){
            return new ServerMessageAnimationSync(target, buf.readBlockPos());
        }
        return null;
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
                switch (message.target){
                    case ENTITY:{
                        for (Entity entity : playerEntity.level.getEntities(playerEntity,playerEntity.getBoundingBox().inflate(100,100,100))) {
                            if (entity instanceof IAnimated){
                                IAnimated animatedEntity = (IAnimated)entity;
                                if (((Entity)animatedEntity).getUUID().equals(message.uuid)){
                                    factory = animatedEntity.getAnimationFactory();
                                }
                            }
                        }
                        break;
                    }
                    case BLOCK:{
                        BlockEntity blockEntity = playerEntity.level.getBlockEntity(message.pos);
                        if (blockEntity instanceof IAnimated){
                            factory = ((IAnimated) blockEntity).getAnimationFactory();
                        }
                    }
                }
                if (factory != null){
                    for (Animation playingAnimation : factory.getPlayingAnimations()) {
                        AnimationHandler.INSTANCE.sendServerSyncMessage(playerEntity,factory,playingAnimation,message.target, ClientMessageAnimation.Action.START,factory.getAnimationTick(playingAnimation));
                    }
                }
            }
        }
    }
}