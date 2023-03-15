package org.astemir.api.common.animation.objects;

import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.common.animation.AnimationFactory;

public interface IAnimated {

    default AnimationFactory getAnimationFactory(){
        return getAnimationFactory(null);
    }

    public <K extends IDisplayArgument> AnimationFactory getAnimationFactory(K argument);
}
