package org.astemir.api.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.item.ToolActionResult;
import org.astemir.api.common.misc.GlobalTaskHandler;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.animation.objects.IAnimatedEntity;


public class WorldEventListener {


    @SubscribeEvent
    public static void onToolUse(BlockEvent.BlockToolModificationEvent e){
        BlockPos pos = e.getContext().getClickedPos();
        BlockState blockState = e.getState();
        ToolAction action = e.getToolAction();
        if (ToolActionResult.canBeProcessed(action,blockState.getBlock())) {
            Block result = ToolActionResult.getVariant(action, blockState.getBlock());
            SoundEvent soundEvent = ToolActionResult.getActionSound(action);
            if (soundEvent != null) {
                e.getLevel().playSound(e.getPlayer(), pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            e.setFinalState(result.defaultBlockState());
            e.getPlayer().swing(e.getContext().getHand());
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        LevelAccessor level = e.getLevel();
        ChunkAccess chunk = e.getChunk();
        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            BlockEntity blockEntity = chunk.getBlockEntity(pos);
            if (blockEntity instanceof IAnimatedEntity){
                AnimationFactory factory = ((IAnimatedEntity) blockEntity).getAnimationFactory();
                if (level.isClientSide()){
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
