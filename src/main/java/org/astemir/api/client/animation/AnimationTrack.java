package org.astemir.api.client.animation;

import org.astemir.api.common.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class AnimationTrack {

    private Animation.Loop loop = Animation.Loop.TRUE;
    private double length = 2;
    private List<AnimationBone> bonesList;
    private String name;

    public AnimationTrack(String name, Animation.Loop loop, double length) {
        this.name = name;
        this.loop = loop;
        this.length = length;
        this.bonesList = new ArrayList<>();
    }

    public void addBone(AnimationBone bone){
        this.bonesList.add(bone);
    }

    public Animation.Loop getLoop() {
        return loop;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public List<AnimationBone> getBonesList() {
        return bonesList;
    }

    public boolean hasBone(String name){
        return getBone(name) != null;
    }

    public AnimationBone getBone(String name){
        for (AnimationBone animationBone : bonesList) {
            if (animationBone.getBoneName().equals(name)){
                return animationBone;
            }
        }
        return null;
    }
}
