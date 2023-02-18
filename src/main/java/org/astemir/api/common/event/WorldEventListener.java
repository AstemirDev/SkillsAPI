package org.astemir.api.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.misc.GlobalTaskHandler;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.action.IActionListener;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.animation.objects.IAnimatedEntity;

public class WorldEventListener {


    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        for (BlockPos pos : e.getChunk().getBlockEntitiesPos()) {
            BlockEntity blockEntity = e.getChunk().getBlockEntity(pos);
            if (blockEntity instanceof IAnimatedEntity){
                AnimationFactory factory = ((IAnimatedEntity) blockEntity).getAnimationFactory();
                if (e.getLevel().isClientSide()){
                    factory.syncClient();
                }else{
                    factory.stopAll();
                }
            }
        }
    }


    @SubscribeEvent
    public static void onWorldUpdate(TickEvent.LevelTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            GlobalTaskHandler.getInstance().update();
        }
    }

}
