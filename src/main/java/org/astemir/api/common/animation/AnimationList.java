package org.astemir.api.common.animation;

import java.util.HashSet;
import java.util.Set;

public class AnimationList {

    private Set<Animation> animations = new HashSet<>();

    public AnimationList(Animation... animationList) {
        for (int i = 0; i < animationList.length; i++) {
            Animation animation = animationList[i];
            animation.setUniqueId(i);
            animations.add(animation);
        }
    }

    public void addAnimation(Animation animation){
        animation.setUniqueId(animations.size());
        animations.add(animation);
    }

    public Animation getAnimation(String name){
        for (Animation animation : animations) {
            if (animation.getName().equals(name)){
                return animation;
            }
        }
        return null;
    }

    public Animation getAnimation(int id){
        for (Animation animation : animations){
            if (animation.getUniqueId() == id){
                return animation;
            }
        }
        return null;
    }

    public Set<Animation> getAnimations() {
        return animations;
    }
}
