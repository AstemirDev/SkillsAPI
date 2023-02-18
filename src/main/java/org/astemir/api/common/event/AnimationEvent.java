package org.astemir.api.common.event;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.objects.IAnimated;

public class AnimationEvent<T extends IAnimated> extends Event {

    protected Animation animation;
    private final T animated;

    AnimationEvent(T animated, Animation animation) {
        this.animated = animated;
        this.animation = animation;
    }

    public T getAnimated() {
        return this.animated;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends IAnimated> extends AnimationEvent<T> {

        public Start(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends IAnimated> extends AnimationEvent<T> {
        protected float tick;

        public Tick(T entity, Animation animation, float tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public float getTick() {
            return this.tick;
        }
    }


    @Cancelable
    public static class End<T extends IAnimated> extends AnimationEvent<T> {

        public End(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }


}