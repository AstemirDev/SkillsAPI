package org.astemir.api.client.animation;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.ClientStateHandler;
import org.astemir.api.client.event.ClientAnimationEvent;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.HolderKey;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.math.Transform;
import org.astemir.api.utils.EntityUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorDataHandler {

    private CopyOnWriteArrayList<BoneStates> data = new CopyOnWriteArrayList();
    private int globalTick = 0;


    public static AnimatorDataHandler getInstance(){
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
            if (states.getKey().getId() != -1){
                if (globalTick - tick > 300) {
                    data.remove(states);
                }
            }
        }
        globalTick++;
    }

    public Transform getTransformData(IAnimated animated, ModelElement renderer){
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
            if (states.getKey().equalsKey(new HolderKey(animated))){
                return states;
            }
        }
        BoneStates newStates = new BoneStates(new HolderKey(animated));
        data.add(newStates);
        return newStates;
    }

    public class BoneStates{

        private Set<BoneStateHolder> boneStates = new HashSet<>();
        private HolderKey key;
        private int lastTick;
        private float tempTick;
        private float prevTempTick;
        private ConcurrentHashMap<Animation,Float> animationTicks = new ConcurrentHashMap<>();


        public BoneStates(HolderKey key) {
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
                    float animationLength = animation.getLength();
                    float animationTick = getAnimationTick(animation);
                    float deltaSpeed = (delta*animation.getSpeed());
                    float nextDelta = animationTick+deltaSpeed;
                    if (nextDelta < animationLength) {
                        MinecraftForge.EVENT_BUS.post(new ClientAnimationEvent.Tick<>(animated,animation,animationTick));
                        animationTicks.put(animation,nextDelta);
                    }else{
                        if (animation.getLoop() != Animation.Loop.HOLD_ON_LAST_FRAME) {
                            animationTicks.put(animation, 0f);
                            MinecraftForge.EVENT_BUS.post(new ClientAnimationEvent.End<>(animated,animation));
                        }
                    }
                }else{
                    animationTicks.remove(animation);
                    MinecraftForge.EVENT_BUS.post(new ClientAnimationEvent.End<>(animated,animation));
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


        public BoneStateHolder getOrAddBoneState(ModelElement renderer){
            if (!hasState(renderer)){
                BoneStateHolder holder = new BoneStateHolder(renderer);
                boneStates.add(holder);
                return holder;
            }else{
                return getState(renderer);
            }
        }

        public HolderKey getKey() {
            return key;
        }

        public int getLastTick() {
            return lastTick;
        }

        public boolean hasState(ModelElement renderer){
            return getState(renderer) != null;
        }

        public BoneStateHolder getState(ModelElement renderer){
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

        private ModelElement bone;
        private Transform transform;

        public BoneStateHolder(ModelElement cubeRenderer) {
            this.bone = cubeRenderer;
            this.transform = new Transform(cubeRenderer.getRotation(),cubeRenderer.getPosition(),cubeRenderer.getScale());
        }

        public ModelElement getBone() {
            return bone;
        }

        public Transform getTransform() {
            return transform;
        }
    }
}
