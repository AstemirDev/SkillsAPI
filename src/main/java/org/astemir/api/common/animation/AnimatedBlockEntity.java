package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.api.common.animation.objects.IAnimatedBlock;

public abstract class AnimatedBlockEntity extends BlockEntity implements ICustomRendered, IAnimatedBlock {

    private long ticks;

    public AnimatedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, IAnimatedBlock block) {
        IAnimatedBlock.super.tick(level, pos, state, block);
        ticks++;
    }

    @Override
    public long getTicks() {
        return ticks;
    }
}
