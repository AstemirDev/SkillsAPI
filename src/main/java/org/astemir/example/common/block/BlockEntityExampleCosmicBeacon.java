package org.astemir.example.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.common.animation.AnimatedBlockEntity;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.AnimationList;

public class BlockEntityExampleCosmicBeacon extends AnimatedBlockEntity {


    public static Animation IDLE = new Animation("animation.model.rotation",2.08f).loop().layer(0);
    public static Animation OPEN_IDLE = new Animation("animation.model.loop",0.52f).loop().layer(0);
    public static Animation OPEN = new Animation("animation.model.open",2.6f).layer(1);

    private boolean opened = false;

    private AnimationFactory animationFactory = new AnimationFactory(this,new AnimationList(
        IDLE,OPEN_IDLE,OPEN
    ));

    public BlockEntityExampleCosmicBeacon(BlockPos pos, BlockState state) {
        super(ModBlocks.COSMIC_BEACON_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, AnimatedBlockEntity entity) {
        super.tick(level, pos, state, entity);
        if (!opened) {
            animationFactory.play(IDLE);
        }else{
            animationFactory.play(OPEN_IDLE);
        }
    }


    @Override
    public void onAnimationTick(Animation animation, int tick) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == OPEN){
            opened = true;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public AnimationFactory getAnimationFactory() {
        return animationFactory;
    }
}
