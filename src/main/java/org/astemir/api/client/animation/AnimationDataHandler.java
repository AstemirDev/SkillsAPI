package org.astemir.api.client.animation;

import org.astemir.api.client.ClientStateHandler;
import org.astemir.api.client.render.AdvancedCubeRenderer;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.IAnimatedID;
import org.astemir.api.math.Transform;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationDataHandler {

    private CopyOnWriteArrayList<BoneStates> data = new CopyOnWriteArrayList();
    private int globalTick = 0;


    public static AnimationDataHandler getInstance(){
        return ClientStateHandler.ANIMATION_DATA_HANDLER;
    }

    public void removeUnusedData(){
        for (BoneStates states : data) {
            int tick = states.getLastTick();
            if (states.getKey().getPos() != null){
                if (globalTick - tick > 100) {
                    data.remove(states);
                }
            }else
            if (states.getKey().getUniqueID() != null){
                if (globalTick - tick > 200) {
                    data.remove(states);
                }
            }
        }
        globalTick++;
    }

    public Transform getTransformData(IAnimated animated,AdvancedCubeRenderer renderer){
        BoneStates states = getOrCreateData(animated);
        states.begin();
        return states.getOrAddBoneState(renderer).getTransform();
    }


    public Transform getTransformData(IAnimated animated,String cubeName){
        BoneStates states = getOrCreateData(animated);
        for (BoneStateHolder boneState : states.getBoneStates()) {
            if (boneState.getBone().getName().equals(cubeName)){
                return boneState.getTransform();
            }
        }
        return null;
    }


    public BoneStates getOrCreateData(IAnimated animated){
        for (BoneStates states : data) {
            if (states.getKey().equalsKey(new IAnimatedID(animated))){
                return states;
            }
        }
        BoneStates newStates = new BoneStates(new IAnimatedID(animated));
        data.add(newStates);
        return newStates;
    }

    public class BoneStates{

        private Set<BoneStateHolder> boneStates = new HashSet<>();
        private IAnimatedID key;
        private int lastTick;
        private float tempTick;
        private float prevTempTick;
        private ConcurrentHashMap<Animation,Float> animationTicks = new ConcurrentHashMap<>();


        public BoneStates(IAnimatedID key) {
            this.key = key;
        }

        public void begin(){
            lastTick = globalTick;
        }

        public void update(IAnimated animated, float tempTick, float delta){
            if (this.tempTick > 0) {
                this.prevTempTick = this.tempTick;
            }else{
                this.prevTempTick = tempTick;
            }
            this.tempTick = tempTick;
            List<Animation> playingAnimations = animated.getAnimationFactory().getPlayingAnimations();
            for (Animation animation : animationTicks.keySet()) {
                if (playingAnimations.contains(animation)){
                    float animationTick = getAnimationTick(animation);
                    float deltaSpeed = (delta*animation.getSpeed());
                    if (animationTick < animation.getLength()) {
                        animationTicks.put(animation,animationTick+deltaSpeed);
                    }else{
                        if (animation.getLoop() != Animation.Loop.HOLD_ON_LAST_FRAME) {
                            animationTicks.put(animation, 0f);
                        }
                    }
                }else{
                    animationTicks.remove(animation);
                }
            }
        }

        public float getTempTick() {
            return tempTick;
        }

        public float getPrevTempTick() {
            return prevTempTick;
        }

        public float getAnimationTick(Animation animation){
            if (animationTicks.containsKey(animation)){
                return animationTicks.get(animation);
            }else{
                animationTicks.put(animation,0f);
                return 0;
            }
        }


        public BoneStateHolder getOrAddBoneState(AdvancedCubeRenderer renderer){
            if (!hasState(renderer)){
                BoneStateHolder holder = new BoneStateHolder(renderer);
                boneStates.add(holder);
                return holder;
            }else{
                return getState(renderer);
            }
        }

        public IAnimatedID getKey() {
            return key;
        }

        public int getLastTick() {
            return lastTick;
        }

        public boolean hasState(AdvancedCubeRenderer renderer){
            return getState(renderer) != null;
        }

        public BoneStateHolder getState(AdvancedCubeRenderer renderer){
            for (BoneStateHolder boneState : boneStates) {
                if (renderer.getName().equals(boneState.bone.getName())){
                    return boneState;
                }
            }
            return null;
        }

        public Set<BoneStateHolder> getBoneStates() {
            return boneStates;
        }
    }

    public class BoneStateHolder {

        private AdvancedCubeRenderer bone;
        private Transform transform;

        public BoneStateHolder(AdvancedCubeRenderer cubeRenderer) {
            this.bone = cubeRenderer;
            this.transform = new Transform(cubeRenderer.getRotation(),cubeRenderer.getPosition(),cubeRenderer.getScale());
        }

        public AdvancedCubeRenderer getBone() {
            return bone;
        }

        public Transform getTransform() {
            return transform;
        }
    }
}
