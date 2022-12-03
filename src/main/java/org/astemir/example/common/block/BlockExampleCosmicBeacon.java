package org.astemir.example.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.astemir.api.common.animation.AnimatedBlockEntity;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.IAnimated;
import org.jetbrains.annotations.Nullable;

public class BlockExampleCosmicBeacon extends BaseEntityBlock {


    protected BlockExampleCosmicBeacon() {
        super(BlockBehaviour.Properties.of(Material.BARRIER));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityExampleCosmicBeacon(pos,state);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            AnimationFactory factory = ((IAnimated)level.getBlockEntity(pos)).getAnimationFactory();
            if (!factory.isPlayingLayer(BlockEntityExampleCosmicBeacon.OPEN) && !factory.isPlaying(BlockEntityExampleCosmicBeacon.OPEN_IDLE)) {
                factory.play(BlockEntityExampleCosmicBeacon.OPEN);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.INVISIBLE;
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type,type,AnimatedBlockEntity::tick);
    }
}
