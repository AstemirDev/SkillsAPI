package org.astemir.api.common.animation.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IAnimatedBlock extends IAnimated{

    static <T extends BlockEntity> void onTick(Level level, BlockPos pos, BlockState state, T t) {
        ((IAnimatedBlock)t).tick(level,pos,state,(IAnimatedBlock) t);
    }

    default void tick(Level level, BlockPos pos, BlockState state, IAnimatedBlock block){
        getAnimationFactory(null).updateAnimations();
    }

    long getTicks();
}
