package org.astemir.api.common.world;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.profiling.jfr.event.ChunkGenerationEvent;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LargeDripstoneFeature;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.animation.objects.IAnimated;
import org.astemir.api.common.entity.ai.ICustomAIEntity;
import org.astemir.api.common.item.ToolActionResult;
import org.astemir.api.common.misc.GlobalTaskHandler;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.action.IActionListener;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.world.schematic.ISchematicBuilder;
import org.astemir.api.math.collection.Couple;

import java.util.Set;

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
