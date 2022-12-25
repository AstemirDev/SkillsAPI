package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AnimatedBlockEntity extends BlockEntity implements ISARendered,IAnimated{

    private long ticks;

    public AnimatedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state, AnimatedBlockEntity entity) {
        getAnimationFactory().updateAnimations();
        ticks++;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        ((AnimatedBlockEntity)t).tick(level,pos,state, (AnimatedBlockEntity) t);
    }

    @Override
    public void onAnimationTick(Animation animation, int tick) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public long getTicks() {
        return ticks;
    }
}
