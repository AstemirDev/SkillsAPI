package org.astemir.api.common.animation.objects;

import org.astemir.api.common.animation.AnimationFactory;

public interface IAnimated {

    default AnimationFactory getAnimationFactory(){
        return getAnimationFactory(null);
    }

    public <K> AnimationFactory getAnimationFactory(K argument);
}
