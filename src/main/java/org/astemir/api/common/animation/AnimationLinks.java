package org.astemir.api.common.animation;


public class AnimationLinks {

    public AnimationLinks.Start onStart;
    public AnimationLinks.Tick onTick;
    public AnimationLinks.End onEnd;

    public void start(AnimationFactory factory){
        if (onStart != null){
            onStart.run(factory);
        }
    }

    public void tick(AnimationFactory factory, int tick){
        if (onTick != null){
            onTick.run(factory,tick);
        }
    }

    public void end(AnimationFactory factory){
        if (onEnd != null){
            onEnd.run(factory);
        }
    }

    public interface Start{
        void run(AnimationFactory factory);
    }

    public interface Tick{
        void run(AnimationFactory factory,int tick);
    }

    public interface End{
        void run(AnimationFactory factory);
    }
}
