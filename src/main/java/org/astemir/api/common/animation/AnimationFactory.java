package org.astemir.api.common.animation;


import org.astemir.api.network.messages.ClientMessageAnimation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationFactory {

    private ConcurrentHashMap<Animation,Integer> animationTicks = new ConcurrentHashMap<>();
    private AnimationList animationList = new AnimationList();
    private IAnimated animated;

    public AnimationFactory(IAnimated animated,AnimationList animations) {
        this.animated = animated;
        this.animationList = animations;
    }

    public void play(Animation animation){
        if (!isPlaying(animation)) {
            for (Animation playingAnimation : getPlayingAnimations()) {
                if (playingAnimation.getLayer() == animation.getLayer()){
                    stop(playingAnimation);
                }
            }
            sendAnimation(animation,0);
        }
    }


    public void sendAnimation(Animation animation,int tick){
        AnimationHandler.INSTANCE.sendAnimationMessage(this,animation,new HolderKey(animated), ClientMessageAnimation.Action.START,tick);
    }

    public void syncClient(){
        AnimationHandler.INSTANCE.sendClientSyncMessage(this,new HolderKey(animated));
    }

    public void stop(Animation animation){
        if (isPlaying(animation)) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this,animation,new HolderKey(animated), ClientMessageAnimation.Action.STOP,0);
        }
    }

    public void stopAll(){
        for (Animation playingAnimation : getPlayingAnimations()) {
            stop(playingAnimation);
        }
    }

    @Deprecated
    public void addAnimation(Animation animation){
        if (!animationTicks.contains(animation)){
            animationTicks.put(animation,0);
        }
    }

    @Deprecated
    public void setAnimation(Animation animation,int tick){
        if (!animationTicks.contains(animation)){
            animationTicks.put(animation,tick);
        }
    }

    public int getAnimationTick(Animation animation){
        if (isPlaying(animation)) {
            return animationTicks.get(animation);
        }else{
            return 0;
        }
    }

    public boolean isPlayingLayer(Animation animation){
        for (Animation anim : animationTicks.keySet()) {
            if (!anim.getName().equals(animation.getName())) {
                if (anim.getLayer() == animation.getLayer()) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isPlaying(Animation animation){
        for (Animation anim : animationTicks.keySet()) {
            if (anim.getName().equals(animation.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean isPlaying(Animation... animations){
        for (Animation animation : animations) {
            if (isPlaying(animation)){
                return true;
            }
        }
        return false;
    }

    public void updateAnimations() {
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Deprecated
    public void removeAnimation(Animation animation){
        animationTicks.remove(animation);
    }

    public IAnimated getAnimated() {
        return animated;
    }

    public AnimationList getAnimationList() {
        return animationList;
    }

    public ConcurrentHashMap<Animation, Integer> getAnimationTicks() {
        return animationTicks;
    }

    public List<Animation> getPlayingAnimations() {
        List<Animation> res = new ArrayList<>();
        for (Animation animation : animationTicks.keySet()) {
            res.add(animation);
        }
        res.sort(Comparator.comparing(Animation::getPriority));
        return res;
    }
}
