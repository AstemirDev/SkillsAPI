package org.astemir.api.common.animation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.event.AnimationEvent;

public class AnimationEventListener {


    @SubscribeEvent
    public static void onTickAnimation(AnimationEvent.Tick e){
        if (e.getAnimated() instanceof IAnimatedEntity animatedEntity){
            animatedEntity.getAnimationFactory().onAnimationTick(e.getAnimation(), (int) e.getTick());
        }else
        if (e.getAnimated() instanceof IAnimatedBlock animatedBlock){
            animatedBlock.getAnimationFactory().onAnimationTick(e.getAnimation(), (int) e.getTick());
        }
    }

    @SubscribeEvent
    public static void onStartAnimation(AnimationEvent.Start e){
        if (e.getAnimated() instanceof IAnimatedEntity animatedEntity){
            animatedEntity.getAnimationFactory().onAnimationStart(e.getAnimation());
        }else
        if (e.getAnimated() instanceof IAnimatedBlock animatedBlock){
            animatedBlock.getAnimationFactory().onAnimationStart(e.getAnimation());
        }
    }

    @SubscribeEvent
    public static void onEndAnimation(AnimationEvent.End e){
        if (e.getAnimated() instanceof IAnimatedEntity animatedEntity){
            animatedEntity.getAnimationFactory().onAnimationEnd(e.getAnimation());
        }else
        if (e.getAnimated() instanceof IAnimatedBlock animatedBlock){
            animatedBlock.getAnimationFactory().onAnimationEnd(e.getAnimation());
        }
    }
}
