package org.astemir.api.client.model;


import com.mojang.datafixers.types.Func;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.astemir.api.client.animation.*;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ISARendered;
import org.astemir.api.math.Transform;
import org.astemir.api.math.Vector3;
import org.astemir.api.utils.JsonUtils;
import org.astemir.api.utils.MathUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public abstract class AnimatedAdvancedModel<T extends ISARendered & IAnimated> extends AdvancedModel<T> {

    public Set<AnimationTrack> animations = new HashSet<>();
    private InterpolationType interpolation = InterpolationType.CATMULLROM;
    private SmoothnessType smoothnessType = SmoothnessType.DEFAULT;
    private float smoothness = 2;
    public static Function<String,String> ANIMATION_FUNCTION;

    public AnimatedAdvancedModel(ResourceLocation modelLoc, ResourceLocation animationsLoc) {
        super(modelLoc);
        if (animationsLoc != null) {
            animations = JsonUtils.getAnimationTracks(animationsLoc,ANIMATION_FUNCTION);
        }
    }


    public boolean isRendererUsed(IAnimated animated, ModelElement renderer){
        for (AnimationTrack track : animations) {
            if (isPlayingTrack(animated,track)){
                if (track != null) {
                    if (track.hasBone(renderer.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPlayingTrack(IAnimated animated, AnimationTrack track){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
            if (playingAnimation.getName().equals(track.getName())){
                return true;
            }
        }
        return false;
    }


    private boolean checkCanRotate(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
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

    private boolean checkCanMove(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
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

    private boolean checkCanScale(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
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

    private boolean isRotatingInAnyTrack(T animated, ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track != null) {
                if (track.hasBone(renderer.getName())) {
                    return track.getBone(renderer.getName()).getRotations() != null;
                }
            }
        }
        return false;
    }


    private boolean isPositioningInAnyTrack(T animated, ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track != null) {
                if (track.hasBone(renderer.getName())) {
                    return track.getBone(renderer.getName()).getPositions() != null;
                }
            }
        }
        return false;
    }


    private boolean isScalingInAnyTrack(T animated, ModelElement renderer){
        for (Animation playingAnimation : animated.getAnimationFactory().getPlayingAnimations()) {
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
    public void setupAnim(T animated, float limbSwing, float limbSwingAmount, float ticks,float headYaw, float headPitch) {
        float partialTicks = Minecraft.getInstance().getPartialTick();
        if (!Minecraft.getInstance().isPaused()) {
            float delta = smoothnessType.deltaCalculate(partialTicks,smoothness);
            if (!animations.isEmpty()) {
                reset();
                AnimatorDataHandler animationManager = AnimatorDataHandler.getInstance();
                AnimatorDataHandler.BoneStates data = animationManager.getOrCreateData(animated);
                float deltaTime = (data.getTempTick() - data.getPrevTempTick()) / 20f;
                if (deltaTime < 0) {
                    deltaTime = 0;
                }
                data.update(animated, ticks, deltaTime);
                for (ModelElement renderer : getElements()) {
                    Transform rendererTransform = animationManager.getTransformData(animated, renderer);
                    Vector3 rot = rendererTransform.getRotation();
                    Vector3 scale = rendererTransform.getScale();
                    Vector3 pos = rendererTransform.getPosition();
                    if (isRendererUsed(animated, renderer)) {
                        AnimationFactory animationFactory = animated.getAnimationFactory();
                        for (Animation animation : animationFactory.getPlayingAnimations()) {
                            AnimationTrack track = getTrack(animation.getName());
                            if (track != null) {
                                if (track.hasBone(renderer.getName())) {
                                    AnimationBone bone = track.getBone(renderer.getName());
                                    float animationTick = data.getAnimationTick(animation);
                                    float animationDelta = smoothnessType.deltaCalculate(partialTicks, animation.getSmoothness());
                                    if (bone.getRotations() != null) {
                                        if (checkCanRotate(animated, animation, bone)) {
                                            rot = rot.rotLerp(interpolation.interpolate(bone.getRotations(), animationTick), animationDelta);
                                        }
                                    }
                                    if (bone.getScales() != null) {
                                        if (checkCanScale(animated, animation, bone)) {
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
                                        if (checkCanMove(animated, animation, bone)) {
                                            pos = pos.lerp(interpolation.interpolate(bone.getPositions(), animationTick), animationDelta);
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        rot = rot.rotLerp(new Vector3(0, 0, 0), delta);
                        scale = scale.lerp(new Vector3(1, 1, 1), delta);
                        pos = pos.lerp(new Vector3(0, 0, 0), delta);
                    }
                    if (!isRotatingInAnyTrack(animated, renderer)) {
                        rot = rot.rotLerp(new Vector3(0, 0, 0), delta);
                    }
                    if (!isScalingInAnyTrack(animated, renderer)) {
                        scale = scale.lerp(new Vector3(1, 1, 1), delta);
                    }
                    if (!isPositioningInAnyTrack(animated, renderer)) {
                        pos = pos.lerp(new Vector3(0, 0, 0), delta);
                    }
                    rendererTransform.setRotation(rot);
                    rendererTransform.setScale(scale);
                    rendererTransform.setPosition(pos);
                    renderer.apply(rendererTransform);
                }
            }
            animate(animated, limbSwing, limbSwingAmount, ticks, delta, headYaw, headPitch);
        }
        customAnimate(animated,limbSwing,limbSwingAmount,ticks,partialTicks,headYaw,headPitch);
    }

    public AnimatedAdvancedModel smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    public AnimatedAdvancedModel interpolation(InterpolationType interpolation) {
        this.interpolation = interpolation;
        return this;
    }

    public AnimatedAdvancedModel smoothnessType(SmoothnessType type) {
        this.smoothnessType = type;
        return this;
    }
}
