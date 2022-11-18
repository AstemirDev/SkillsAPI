package org.astemir.api.common.animation;



import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.network.messages.AnimationMessage;
import org.astemir.api.network.AnimationTarget;
import org.astemir.api.network.messages.ClientAnimationSyncMessage;
import org.astemir.example.SkillsAPIMod;


public enum AnimationHandler {

    INSTANCE;



    public <T extends IAnimated> void sendAnimationMessage(AnimationFactory factory, Animation animation, AnimationTarget target, AnimationMessage.Action type,int tick) {
        Level level = target.getLevel(factory);
        if (level.isClientSide) {
            return;
        }
        if (type == AnimationMessage.Action.START) {
            factory.addAnimation(animation);
        } else if (type == AnimationMessage.Action.STOP) {
            factory.removeAnimation(animation);
        }
        if (target == AnimationTarget.ENTITY) {
            Entity entity = (Entity) factory.getAnimated();
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new AnimationMessage(AnimationTarget.ENTITY,entity.getUUID(), type, animation.getUniqueId(),tick));
        }else
        if (target == AnimationTarget.BLOCK){
            BlockEntity blockEntity = (BlockEntity)factory.getAnimated();
            BlockPos pos = blockEntity.getBlockPos();
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.NEAR.with(()->new PacketDistributor.TargetPoint(pos.getX(),pos.getY(),pos.getZ(),128,blockEntity.getLevel().dimension())), new AnimationMessage(AnimationTarget.BLOCK,pos, type, animation.getUniqueId(),tick));
        }
    }

    public void sendClientSyncMessage(AnimationFactory factory,AnimationTarget target){
        Level level = target.getLevel(factory);
        if (!level.isClientSide) {
            return;
        }
        if (target == AnimationTarget.ENTITY) {
            Entity entity = (Entity) factory.getAnimated();
            SkillsAPIMod.INSTANCE.getAPINetwork().sendToServer(new ClientAnimationSyncMessage(target,entity.getUUID()));
        }else
        if (target == AnimationTarget.BLOCK){
            BlockEntity blockEntity = (BlockEntity)factory.getAnimated();
            SkillsAPIMod.INSTANCE.getAPINetwork().sendToServer(new ClientAnimationSyncMessage(target,blockEntity.getBlockPos()));
        }
    }

    public void sendServerSyncMessage(ServerPlayer player, AnimationFactory factory, Animation animation, AnimationTarget target, AnimationMessage.Action type, int tick) {
        Level level = target.getLevel(factory);
        if (level.isClientSide) {
            return;
        }
        if (target == AnimationTarget.ENTITY) {
            Entity entity = (Entity) factory.getAnimated();
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.PLAYER.with(() -> player), new AnimationMessage(AnimationTarget.ENTITY,entity.getUUID(), type, animation.getUniqueId(),tick));
        }else
        if (target == AnimationTarget.BLOCK){
            BlockEntity blockEntity = (BlockEntity)factory.getAnimated();
            BlockPos pos = blockEntity.getBlockPos();
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.PLAYER.with(()->player), new AnimationMessage(AnimationTarget.BLOCK,pos, type, animation.getUniqueId(),tick));
        }
    }

    public void updateAnimations(AnimationFactory factory) {
        IAnimated animatable = factory.getAnimated();
        factory.getAnimationTicks().forEach((animation,ticks)->{
            int time = (int)(animation.getLength()*20/animation.getSpeed());
            if (ticks == 0) {
                MinecraftForge.EVENT_BUS.post(new AnimationEvent.Start<>(animatable, animation));
            }
            if (ticks < time){
                MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick(animatable, animation, ticks));
                factory.getAnimationTicks().put(animation,ticks+1);
            }else{
                switch (animation.getLoop()){
                    case TRUE:{
                        factory.getAnimationTicks().put(animation,0);
                        MinecraftForge.EVENT_BUS.post(new AnimationEvent.End(animatable, animation));
                        break;
                    }
                    case FALSE:{
                        factory.stop(animation);
                        MinecraftForge.EVENT_BUS.post(new AnimationEvent.End(animatable, animation));
                        break;
                    }
                    case HOLD_ON_LAST_FRAME:{
                        MinecraftForge.EVENT_BUS.post(new AnimationEvent.End(animatable, animation));
                        break;
                    }
                }
            }
        });
    }


}
