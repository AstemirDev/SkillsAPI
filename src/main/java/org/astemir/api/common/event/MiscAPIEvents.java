package org.astemir.api.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.animation.AnimationEvent;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.common.state.IActionListener;
import org.astemir.example.SkillsAPIMod;

public class MiscAPIEvents {


    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        for (BlockPos pos : e.getChunk().getBlockEntitiesPos()) {
            BlockEntity blockEntity = e.getChunk().getBlockEntity(pos);
            if (blockEntity instanceof IAnimated){
                AnimationFactory factory = ((IAnimated) blockEntity).getAnimationFactory();
                if (e.getLevel().isClientSide()){
                    factory.syncClient();
                }else{
                    factory.stopAll();
                }
            }
            if (blockEntity instanceof IActionListener){
                ActionStateMachine machine = ((IActionListener)blockEntity).getActionStateMachine();
                if (e.getLevel().isClientSide()){
                    machine.sendClientSyncMessage((IActionListener) blockEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAdded(EntityJoinLevelEvent e){
        if (e.getEntity() instanceof IAnimated) {
            AnimationFactory factory = ((IAnimated) e.getEntity()).getAnimationFactory();
            if (e.getLevel().isClientSide()){
                factory.syncClient();
            }else{
                factory.stopAll();
            }
        }
        if (e.getEntity() instanceof IActionListener){
            ActionStateMachine machine = ((IActionListener)e.getEntity()).getActionStateMachine();
            if (e.getLevel().isClientSide()){
                machine.sendClientSyncMessage((IActionListener) e.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUpdate(TickEvent.LevelTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            SkillsAPIMod.INSTANCE.GLOBAL_TASK_HANDLER.update();
        }
    }

    @SubscribeEvent
    public static void onTickEntity(LivingEvent.LivingTickEvent e){
        if (e.getEntity() instanceof IAnimated){
            ((IAnimated)e.getEntity()).getAnimationFactory().updateAnimations();
        }
    }

    @SubscribeEvent
    public static void onTickAnimation(AnimationEvent.Tick e){
        e.getEntity().onAnimationTick(e.getAnimation(), (int) e.getTick());
    }

    @SubscribeEvent
    public static void onStartAnimation(AnimationEvent.Start e){
        e.getEntity().onAnimationStart(e.getAnimation());
    }

    @SubscribeEvent
    public static void onEndAnimation(AnimationEvent.End e){
        e.getEntity().onAnimationEnd(e.getAnimation());
    }

}
