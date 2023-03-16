package org.astemir.api.common.animation.objects;

import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;

public interface IAnimated {

    default AnimationFactory getAnimationFactory(){
        return getAnimationFactory(null);
    }

    default void onAnimationTick(Animation animation, int tick){};

    default void onAnimationEnd(Animation animation){};

    default void onAnimationStart(Animation animation){};

    public <K extends IDisplayArgument> AnimationFactory getAnimationFactory(K argument);
}
