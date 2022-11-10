package org.astemir.api.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.animation.*;
import org.astemir.api.client.render.AdvancedCubeRenderer;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ITESRModel;
import org.astemir.api.utils.AnimationUtils;
import org.astemir.api.utils.JsonUtils;
import org.astemir.api.utils.MathUtils;
import org.astemir.api.math.Transform;
import org.astemir.api.math.Vector2;
import org.astemir.api.math.Vector3;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

import static org.astemir.api.utils.JsonUtils.*;

public abstract class AdvancedModel<T extends ITESRModel> extends Model {

    public Set<AdvancedCubeRenderer> renderers = new HashSet<>();
    public Set<AdvancedCubeRenderer> root = new HashSet<>();
    public Set<AnimationTrack> animations = new HashSet<>();

    public IModelWrapper modelWrapper;

    public Vector2 textureSize = new Vector2(64,32);

    public AdvancedModel(Function<ResourceLocation, RenderType> p_103110_,ResourceLocation model,ResourceLocation animations) {
        super(p_103110_);
        if (model != null) {
            loadModelFile(model);
        }
        if (animations != null) {
            loadAnimationsFile(animations);
        }
    }


    public void renderItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, int packedLightIn){
        ITESRModel renderTarget = modelWrapper.getRenderTarget();
        int overlayCoords = 0;
        if (renderTarget instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity)renderTarget;
            overlayCoords = LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F);
            Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, itemStack, transformType, false, matrixStack, modelWrapper.getMultiBufferSource(), null, packedLightIn, overlayCoords, ((Entity)renderTarget).getId());
        }else{
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, transformType, packedLightIn, overlayCoords,matrixStack, modelWrapper.getMultiBufferSource(), ((Entity)renderTarget).getId());
        }
    }

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        renderModel(p_103111_,p_103112_,p_103113_,p_103114_,p_103115_,p_103116_,p_103117_,p_103118_,RenderCall.MODEL,false);
    }

    public void renderModel(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
        for (AdvancedCubeRenderer modelRenderer : root) {
            modelRenderer.render(this,stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha,renderCall,resetBuffer);
        }
    }

    public boolean isRendererUsed(IAnimated animated,AdvancedCubeRenderer renderer){
        for (AnimationTrack track : animations) {
            if (isPlayingTrack(animated,track)){
                if (track.hasBone(renderer.getName())){
                    return true;
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

    public void reset(){
        for (AdvancedCubeRenderer renderer : renderers) {
            renderer.reset();
        }
    }


    private boolean checkCanRotate(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track.hasBone(bone.getBoneName())) {
                        AnimationBone otherBone = track.getBone(bone.getBoneName());
                        if (otherBone.getRotations() != null) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCanMove(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track.hasBone(bone.getBoneName())) {
                        AnimationBone otherBone = track.getBone(bone.getBoneName());
                        if (otherBone.getPositions() != null) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkCanScale(T animated,Animation animation,AnimationBone bone){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            if (!playingAnimation.getName().equals(animation.getName())) {
                if (playingAnimation.getPriority() > animation.getPriority()) {
                    AnimationTrack track = getTrack(playingAnimation.getName());
                    if (track.hasBone(bone.getBoneName())) {
                        AnimationBone otherBone = track.getBone(bone.getBoneName());
                        if (otherBone.getScales() != null) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isRotatingInAnyTrack(T animated,AdvancedCubeRenderer renderer){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track.hasBone(renderer.getName())){
                return track.getBone(renderer.getName()).getRotations() != null;
            }
        }
        return false;
    }


    private boolean isPositioningInAnyTrack(T animated,AdvancedCubeRenderer renderer){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track.hasBone(renderer.getName())){
                return track.getBone(renderer.getName()).getPositions() != null;
            }
        }
        return false;
    }


    private boolean isScalingInAnyTrack(T animated,AdvancedCubeRenderer renderer){
        for (Animation playingAnimation : ((IAnimated)animated).getAnimationFactory().getPlayingAnimations()) {
            AnimationTrack track = getTrack(playingAnimation.getName());
            if (track.hasBone(renderer.getName())){
                return track.getBone(renderer.getName()).getScales() != null;
            }
        }
        return false;
    }

    public void setupAnim(T animated, float limbSwing, float limbSwingAmount, float ticks,float headYaw, float headPitch) {
        if (animated instanceof IAnimated) {
            if (!Minecraft.getInstance().isPaused()) {
                float delta = (ticks - ((IAnimated) animated).getTicks()) / 1.5f;
                if (!animations.isEmpty()) {
                    reset();
                    AnimationDataHandler animationManager = AnimationDataHandler.getInstance();
                    AnimationDataHandler.BoneStates data = animationManager.getOrCreateData((IAnimated) animated);
                    float deltaTime = (data.getTempTick() - data.getPrevTempTick()) / 20f;
                    if (deltaTime < 0) {
                        deltaTime = 0;
                    }
                    data.update(((IAnimated) animated), ticks, deltaTime);

                    for (AdvancedCubeRenderer renderer : getRenderers()) {
                        Transform rendererTransform = animationManager.getTransformData((IAnimated) animated, renderer);

                        Vector3 rot = rendererTransform.getRotation();
                        Vector3 scale = rendererTransform.getScale();
                        Vector3 pos = rendererTransform.getPosition();
                        if (isRendererUsed(((IAnimated) animated), renderer)) {
                            AnimationFactory animationFactory = ((IAnimated) animated).getAnimationFactory();
                            for (Animation animation : animationFactory.getPlayingAnimations()) {
                                AnimationTrack track = getTrack(animation.getName());
                                if (track.hasBone(renderer.getName())) {
                                    AnimationBone bone = track.getBone(renderer.getName());
                                    float animationTick = data.getAnimationTick(animation);
                                    if (bone.getRotations() != null) {
                                        if (checkCanRotate(animated, animation, bone)) {
                                            AnimationFrame[] frames = bone.getRotations();
                                            rot = rot.rotLerp(AnimationUtils.interpolatePoints(frames, animationTick), delta);
                                        }
                                    }
                                    if (bone.getScales() != null) {
                                        if (checkCanScale(animated, animation, bone)) {
                                            AnimationFrame[] frames = bone.getScales();
                                            scale = scale.lerp(AnimationUtils.interpolatePoints(frames, animationTick), delta);
                                        }
                                    }
                                    if (bone.getPositions() != null) {
                                        if (checkCanMove(animated, animation, bone)) {
                                            AnimationFrame[] frames = bone.getPositions();
                                            pos = pos.lerp(AnimationUtils.interpolatePoints(frames, animationTick), delta);
                                        }
                                    }
                                }
                            }
                        } else {
                            rot = rot.lerp(new Vector3(0, 0, 0), delta);
                            scale = scale.lerp(new Vector3(1, 1, 1), delta);
                            pos = pos.lerp(new Vector3(0, 0, 0), delta);
                        }
                        if (!isRotatingInAnyTrack(animated, renderer)) {
                            rot = rot.lerp(new Vector3(0, 0, 0), delta);
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
        }
        customAnimate(animated,limbSwing,limbSwingAmount,ticks,Minecraft.getInstance().getPartialTick(),headYaw,headPitch);
    }


    public Vector3 getRotationPoint(AdvancedCubeRenderer renderer){
        for (AdvancedCubeRenderer advancedCubeRenderer : renderers) {
            if (advancedCubeRenderer.isChild(renderer)){
                return getRotationPoint(advancedCubeRenderer).add(renderer.rotationPoint);
            }
        }
        return renderer.rotationPoint;
    }

    public abstract void animate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch);

    public void customAnimate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch){}


    public void lookAt(AdvancedCubeRenderer renderer,float headPitch,float headYaw){
        renderer.setRotation(renderer.getRotation().add(new Vector3(MathUtils.rad(headPitch),MathUtils.rad(headYaw),0)));
    }

    public void root(AdvancedCubeRenderer renderer){
        this.root.add(renderer);
    }

    public AdvancedCubeRenderer cube(String name, int textureWidth, int textureHeight, int textureOffsetX, int textureOffsetY){
        AdvancedCubeRenderer modelRenderer = new AdvancedCubeRenderer(name,textureWidth,textureHeight,textureOffsetX,textureOffsetY);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer cube(String name, int textureWidth, int textureHeight){
        AdvancedCubeRenderer modelRenderer = new AdvancedCubeRenderer(name,textureWidth,textureHeight,0,0);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer cube(String name, AdvancedModel model){
        AdvancedCubeRenderer modelRenderer = new AdvancedCubeRenderer(name,(int)model.getTextureSize().x,(int)model.getTextureSize().y,0,0);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer cube(String name, AdvancedModel model, int textureOffsetX, int textureOffsetY){
        AdvancedCubeRenderer modelRenderer = new AdvancedCubeRenderer(name,(int)model.getTextureSize().x,(int)model.getTextureSize().y,textureOffsetX,textureOffsetY);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer rootCube(String name, int textureWidth, int textureHeight, int textureOffsetX, int textureOffsetY){
        AdvancedCubeRenderer modelRenderer = cube(name,textureWidth,textureHeight,textureOffsetX,textureOffsetY);
        root.add(modelRenderer);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer rootCube(String name, int textureWidth, int textureHeight){
        AdvancedCubeRenderer modelRenderer = cube(name,textureWidth,textureHeight);
        root.add(modelRenderer);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer rootCube(String name, AdvancedModel model){
        AdvancedCubeRenderer modelRenderer = cube(name,model);
        root.add(modelRenderer);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer rootCube(String name, AdvancedModel model, int textureOffsetX, int textureOffsetY){
        AdvancedCubeRenderer modelRenderer = cube(name,model,textureOffsetX,textureOffsetY);
        root.add(modelRenderer);
        renderers.add(modelRenderer);
        return modelRenderer;
    }

    public AdvancedCubeRenderer getModelRenderer(String name){
        for (AdvancedCubeRenderer renderer : renderers) {
            if (renderer.getName().equals(name)){
                return renderer;
            }
        }
        return null;
    }

    public Set<AdvancedCubeRenderer> getRenderers() {
        return renderers;
    }

    public Vector2 getTextureSize() {
        return textureSize;
    }

    public void setTextureSize(float width, float height) {
        this.textureSize = new Vector2(width,height);
    }


    public AnimationTrack getTrack(String name){
        for (AnimationTrack animation : animations) {
            if (animation.getName().equals(name)){
                return animation;
            }
        }
        return null;
    }

    public void onRenderModelCube(AdvancedCubeRenderer cube,PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall,int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){}


    public VertexConsumer returnDefaultBuffer(){
        return modelWrapper.getMultiBufferSource().getBuffer(modelWrapper.getRenderType(modelWrapper.getRenderTarget(),modelWrapper.getTexture(modelWrapper.getRenderTarget())));
    }

    public void loadModelFile(ResourceLocation resourceLocation){
        JsonParser parser = new JsonParser();
        InputStream stream = null;
        try {
            stream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).get().open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement parsed = parser.parse(new InputStreamReader(stream));
        JsonElement geometryJson = parsed.getAsJsonObject().get("minecraft:geometry").getAsJsonArray().get(0);
        JsonObject descriptionJson = geometryJson.getAsJsonObject().get("description").getAsJsonObject();
        setTextureSize(JsonUtils.getIntOrDefault(descriptionJson,"texture_width",32),JsonUtils.getIntOrDefault(descriptionJson,"texture_height",32));
        JsonArray bonesJson = geometryJson.getAsJsonObject().get("bones").getAsJsonArray();
        for (JsonElement bone : bonesJson) {
            JsonObject boneJson = bone.getAsJsonObject();
            String name = boneJson.get("name").getAsString();
            boolean isRoot = !boneJson.has("parent");
            Vector3 rotationPoint = getBedrockPivot(bonesJson,boneJson,isRoot);
            Vector3 rotation = JsonUtils.getVec3OrDefault(boneJson,"rotation",true,new Vector3(0,0,0));
            AdvancedCubeRenderer cubeRenderer;
            if (isRoot){
                cubeRenderer = rootCube(name,this);
            }else{
                cubeRenderer = cube(name,this);
            }
            cubeRenderer.rotationPoint(rotationPoint.x,rotationPoint.y,rotationPoint.z).defaultRotation(rotation.x,rotation.y,rotation.z);
            if (boneJson.has("cubes")) {
                for (JsonElement cubeElement : boneJson.get("cubes").getAsJsonArray()) {
                    JsonObject cubeJson = cubeElement.getAsJsonObject();
                    Vector3 pos = getBedrockOrigin(boneJson,cubeJson);
                    Vector3 size = JsonUtils.getVec3OrDefault(cubeJson, "size", false, new Vector3(0, 0, 0));
                    Vector2 uv = JsonUtils.getVec2OrDefault(cubeJson, "uv", false, new Vector2(0, 0));
                    double inflate = JsonUtils.getDoubleOrDefault(cubeJson, "inflate", 0);
                    boolean mirror = JsonUtils.getBoolOrDefault(cubeJson, "mirror", false);
                    cubeRenderer.textureOffset((int) uv.x, (int) uv.y).cube(pos.x, pos.y, pos.z, size.x, size.y, size.z, (float) inflate, mirror);
                }
            }
        }
        for (JsonElement bone : bonesJson){
            JsonObject boneJson = bone.getAsJsonObject();
            String name = boneJson.get("name").getAsString();
            if (boneJson.has("parent")) {
                String parent = boneJson.get("parent").getAsString();
                getModelRenderer(parent).addChild(getModelRenderer(name));
            }
        }
    }

    public void loadAnimationsFile(ResourceLocation resourceLocation) {
        JsonParser parser = new JsonParser();
        InputStream stream = null;
        try {
            stream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).get().open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement parsed = parser.parse(new InputStreamReader(stream));
        for (Map.Entry<String, JsonElement> animationEntry : JsonUtils.getEntries(parsed, "animations")) {
            String animationName = animationEntry.getKey();
            JsonObject animationJson = animationEntry.getValue().getAsJsonObject();
            AnimationTrack track = new AnimationTrack(animationName, Animation.Loop.parse(JsonUtils.getStringOrDefault(animationJson, "loop", "false")), JsonUtils.getDoubleOrDefault(animationJson, "animation_length", 0));
            for (Map.Entry<String, JsonElement> bonesEntry : JsonUtils.getEntries(animationJson, "bones")) {
                String boneName = bonesEntry.getKey();
                JsonObject boneJsonObject = bonesEntry.getValue().getAsJsonObject();
                AnimationBone bone = new AnimationBone(boneName, getAnimationProperty(boneJsonObject, "rotation", true), getAnimationProperty(boneJsonObject, "scale", false), getAnimationProperty(boneJsonObject, "position", false));
                track.addBone(bone);
            }
            animations.add(track);
        }
    }

    public IModelWrapper getModelWrapper() {
        return modelWrapper;
    }
}
