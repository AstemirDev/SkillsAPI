package org.astemir.api.client.event;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.IAnimated;

public class ClientAnimationEvent<T extends IAnimated> extends Event {

    protected Animation animation;
    private final T entity;

    ClientAnimationEvent(T entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() {
        return this.entity;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public static class Tick<T extends IAnimated> extends ClientAnimationEvent<T> {
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
    public static class End<T extends IAnimated> extends ClientAnimationEvent<T> {

        public End(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }


}