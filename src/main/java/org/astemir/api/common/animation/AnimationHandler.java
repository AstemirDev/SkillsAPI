package org.astemir.api.common.animation;



import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.SkillsAPI;
import org.astemir.api.network.messages.ClientMessageAnimation;
import org.astemir.api.network.messages.ServerMessageAnimationSync;
import org.astemir.api.utils.NetworkUtils;


public enum AnimationHandler {

    INSTANCE;



    public <T extends IAnimated> void sendAnimationMessage(AnimationFactory factory, Animation animation, HolderKey targetId, ClientMessageAnimation.Action type, int tick) {
        Level level = targetId.getTarget().getLevel(factory);
        if (level.isClientSide) {
            return;
        }
        if (type == ClientMessageAnimation.Action.START) {
            factory.addAnimation(animation);
        } else if (type == ClientMessageAnimation.Action.STOP) {
            factory.removeAnimation(animation);
        }
        NetworkUtils.sendToAllPlayers(new ClientMessageAnimation(targetId, type, animation.getUniqueId(),tick));
    }

    public void sendClientSyncMessage(AnimationFactory factory, HolderKey targetId){
        Level level = targetId.getTarget().getLevel(factory);
        if (!level.isClientSide) {
            return;
        }
        NetworkUtils.sendToServer(new ServerMessageAnimationSync(targetId));
    }

    public void sendServerSyncMessage(ServerPlayer player, AnimationFactory factory, Animation animation, HolderKey targetId, ClientMessageAnimation.Action type, int tick) {
        Level level = targetId.getTarget().getLevel(factory);
        if (level.isClientSide) {
            return;
        }
        NetworkUtils.sendToPlayer(player,new ClientMessageAnimation(targetId, type, animation.getUniqueId(),tick));
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
