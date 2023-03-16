package org.astemir.api.common.animation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.event.AnimationEvent;

public class AnimationEventListener {


    @SubscribeEvent
    public static void onTickAnimation(AnimationEvent.Tick e){
        e.getAnimated().onAnimationTick(e.getAnimation(),(int)e.getTick());
        e.getAnimated().getAnimationFactory().onAnimationTick(e.getAnimation(), (int) e.getTick());
        e.getAnimation().onTick(e.getAnimated().getAnimationFactory(),(int)e.getTick());
        e.getAnimation().getLinks().tick(e.getAnimated().getAnimationFactory(),(int)e.getTick());
    }

    @SubscribeEvent
    public static void onStartAnimation(AnimationEvent.Start e){
        e.getAnimated().getAnimationFactory().onAnimationStart(e.getAnimation());
        e.getAnimated().onAnimationStart(e.getAnimation());
        e.getAnimation().onStart(e.getAnimated().getAnimationFactory());
        e.getAnimation().getLinks().start(e.getAnimated().getAnimationFactory());
    }

    @SubscribeEvent
    public static void onEndAnimation(AnimationEvent.End e){
        e.getAnimated().getAnimationFactory().onAnimationEnd(e.getAnimation());
        e.getAnimated().onAnimationEnd(e.getAnimation());
        e.getAnimation().onEnd(e.getAnimated().getAnimationFactory());
        e.getAnimation().getLinks().end(e.getAnimated().getAnimationFactory());
    }
}
