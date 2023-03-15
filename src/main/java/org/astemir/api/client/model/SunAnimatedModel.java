package org.astemir.api.client.model;


import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.animation.*;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.animation.objects.IAnimated;
import org.astemir.api.math.Transform;
import org.astemir.api.math.vector.Vector3;
import org.astemir.api.client.JsonUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class SunAnimatedModel<T extends ICustomRendered & IAnimated,K extends IDisplayArgument> extends SunModel<T,K> {

    public Set<AnimationTrack> animations = new HashSet<>();
    private InterpolationType interpolation = InterpolationType.CATMULLROM;
    private SmoothnessType smoothnessType = SmoothnessType.DEFAULT;
    private float smoothness = 2;
    public static Function<String,String> ANIMATION_FUNCTION;

    public SunAnimatedModel(ResourceLocation modelLoc, ResourceLocation animationsLoc) {
        super(modelLoc);
        if (animationsLoc != null) {
            animations = JsonUtils.getAnimationTracks(animationsLoc,isEncrypted() ? ANIMATION_FUNCTION : null);
        }
    }


    public boolean isRendererUsed(T animated,K argument, ModelElement renderer){
        for (AnimationTrack track : animations) {
            if (isPlayingTrack(animated,argument,track)){
                if (track != null) {
                    if (track.hasBone(renderer.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPlayingTrack(T animated, K argument,AnimationTrack track){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            if (playingAnimation.getName().equals(track.getName())){
                return true;
            }
        }
        return false;
    }


    private boolean checkCanRotate(T animated,K argument,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track != null) {
                        if (track.hasBone(bone.getBoneName())) {
                            AnimationBone otherBone = track.getBone(bone.getBoneName());
                            if (otherBone.getRotations() != null) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCanMove(T animated,K argument,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track != null) {
                        if (track.hasBone(bone.getBoneName())) {
                            AnimationBone otherBone = track.getBone(bone.getBoneName());
                            if (otherBone.getPositions() != null) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public AnimationTrack getTrack(String name){
        for (AnimationTrack animation : animations) {
            if (animation.getName().equals(name)){
                return animation;
            }
        }
        return null;
    }

    private boolean checkCanScale(T animated,K argument,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track != null) {
                        if (track.hasBone(bone.getBoneName())) {
                            AnimationBone otherBone = track.getBone(bone.getBoneName());
                            if (otherBone.getScales() != null) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isRotatingInAnyTrack(T animated, K argument,ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track != null) {
                if (track.hasBone(renderer.getName())) {
                    return track.getBone(renderer.getName()).getRotations() != null;
                }
            }
        }
        return false;
    }


    private boolean isPositioningInAnyTrack(T animated, K argument,ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track != null) {
                if (track.hasBone(renderer.getName())) {
                    return track.getBone(renderer.getName()).getPositions() != null;
                }
            }
        }
        return false;
    }


    private boolean isScalingInAnyTrack(T animated, K argument,ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory(argument).getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track != null) {
                if (track.hasBone(renderer.getName())) {
                    return track.getBone(renderer.getName()).getScales() != null;
                }
            }
        }
        return false;
    }


    @Override
    public void setupAnim(T animated, K argument,float limbSwing, float limbSwingAmount, float ticks,float headYaw, float headPitch) {
        float partialTicks = Minecraft.getInstance().getPartialTick();
        if (!Minecraft.getInstance().isPaused()) {
            float delta = smoothnessType.deltaCalculate(partialTicks,smoothness);
            if (!animations.isEmpty()) {
                reset();
                Animator.BoneStates data = Animator.INSTANCE.getOrCreateData(animated);
                float deltaTime = (data.getTempTick() - data.getPrevTempTick()) / 20f;
                if (deltaTime < 0) {
                    deltaTime = 0;
                }
                data.update(animated, argument,ticks, deltaTime);
                for (ModelElement renderer : getElements()) {
                    Transform rendererTransform = Animator.INSTANCE.getTransformData(animated, renderer);
                    Vector3 rot = rendererTransform.getRotation();
                    Vector3 scale = rendererTransform.getScale();
                    Vector3 pos = rendererTransform.getPosition();
                    if (isRendererUsed(animated, argument,renderer)) {
                        AnimationFactory animationFactory = animated.getAnimationFactory(argument);
                        for (Animation animation : animationFactory.getPlayingAnimations()) {
                            AnimationTrack track = getTrack(animation.getName());
                            if (track != null) {
                                if (track.hasBone(renderer.getName())) {
                                    AnimationBone bone = track.getBone(renderer.getName());
                                    float animationTick = data.getAnimationTick(animation);
                                    float animationDelta = smoothnessType.deltaCalculate(partialTicks, animation.getSmoothness());
                                    if (bone.getRotations() != null) {
                                        if (checkCanRotate(animated, argument,animation, bone)) {
                                            rot = rot.rotLerp(interpolation.interpolate(bone.getRotations(), animationTick), animationDelta);
                                        }
                                    }
                                    if (bone.getScales() != null) {
                                        if (checkCanScale(animated,argument,animation, bone)) {
                                            boolean shouldBeHidden = false;
                                            if (bone.getScales().length == 1){
                                                AnimationFrame scaleFrame = bone.getScales()[0];
                                                Vector3 value = scaleFrame.getValue();
                                                if (scaleFrame.getPosition() == 0 && (value.x == 0 && value.y == 0 && value.z == 0)){
                                                    shouldBeHidden = true;
                                                }
                                            }
                                            if (!shouldBeHidden) {
                                                scale = scale.lerp(interpolation.interpolate(bone.getScales(), animationTick), animationDelta);
                                            }else{
                                                scale = new Vector3(0,0,0);
                                            }
                                        }
                                    }
                                    if (bone.getPositions() != null) {
                                        if (checkCanMove(animated, argument,animation, bone)) {
                                            pos = pos.lerp(interpolation.interpolate(bone.getPositions(), animationTick), animationDelta);
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        rot = rot.rotLerp(new Vector3(0, 0, 0), delta*delta);
                        scale = scale.lerp(new Vector3(1, 1, 1), delta*delta);
                        pos = pos.lerp(new Vector3(0, 0, 0), delta*delta);
                    }
                    if (!isRotatingInAnyTrack(animated, argument,renderer)) {
                        rot = rot.rotLerp(new Vector3(0, 0, 0), delta*delta);
                    }
                    if (!isScalingInAnyTrack(animated,argument, renderer)) {
                        scale = scale.lerp(new Vector3(1, 1, 1), delta*delta);
                    }
                    if (!isPositioningInAnyTrack(animated,argument, renderer)) {
                        pos = pos.lerp(new Vector3(0, 0, 0), delta*delta);
                    }
                    rendererTransform.setRotation(rot);
                    rendererTransform.setScale(scale);
                    rendererTransform.setPosition(pos);
                    renderer.apply(rendererTransform);
                }
            }
            animate(animated,argument, limbSwing, limbSwingAmount, ticks, delta, headYaw, headPitch);
        }
        customAnimate(animated,argument,limbSwing,limbSwingAmount,ticks,partialTicks,headYaw,headPitch);
    }

    public SunAnimatedModel smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    public SunAnimatedModel interpolation(InterpolationType interpolation) {
        this.interpolation = interpolation;
        return this;
    }

    public SunAnimatedModel smoothnessType(SmoothnessType type) {
        this.smoothnessType = type;
        return this;
    }
}
