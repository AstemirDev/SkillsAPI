package org.astemir.api.common.animation;



public interface IAnimated{

    long getTicks();

    void onAnimationTick(Animation animation, int tick);

    void onAnimationEnd(Animation animation);

    void onAnimationStart(Animation animation);


    AnimationFactory getAnimationFactory();
}
