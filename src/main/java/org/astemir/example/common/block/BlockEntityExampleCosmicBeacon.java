package org.astemir.example.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.animation.objects.IAnimatedBlock;

public class BlockEntityExampleCosmicBeacon extends AnimatedBlockEntity {


    private AnimationFactory animationFactory = new AnimationFactory(this,IDLE,OPEN_IDLE,OPEN);
    public static Animation IDLE = new Animation("animation.model.rotation",2.08f).loop().layer(0);
    public static Animation OPEN_IDLE = new Animation("animation.model.loop",0.52f).loop().layer(0);
    public static Animation OPEN = new Animation("animation.model.open",2.6f).layer(1).onEnd((f)->{
        BlockEntityExampleCosmicBeacon cosmicBeacon = f.getAnimated();
        cosmicBeacon.open();
    });

    private boolean opened = false;

    public BlockEntityExampleCosmicBeacon(BlockPos pos, BlockState state) {
        super(ExampleModBlocks.COSMIC_BEACON_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, IAnimatedBlock block) {
        super.tick(level, pos, state, block);
        if (!opened) {
            animationFactory.play(IDLE);
        }else{
            animationFactory.play(OPEN_IDLE);
        }
    }

    public void open(){
        opened = true;
    }

    @Override
    public <K extends IDisplayArgument> AnimationFactory getAnimationFactory(K argument) {
        return animationFactory;
    }
}
