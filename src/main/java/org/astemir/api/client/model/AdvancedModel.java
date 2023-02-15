package org.astemir.api.client.model;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.common.animation.ISARendered;
import org.astemir.api.utils.JsonUtils;
import org.astemir.api.utils.MathUtils;
import org.astemir.api.math.Vector2;
import org.astemir.api.math.Vector3;
import java.util.*;
import java.util.function.Function;

public abstract class AdvancedModel<T extends ISARendered> extends Model {

    public Set<ModelElement> renderers = new HashSet<>();
    public IModelWrapper<T> modelWrapper;
    public Vector2 textureSize = new Vector2(64,32);

    protected final List<ModelRenderLayer<T, AdvancedModel<T>>> layers = Lists.newArrayList();
    private Function<String,String> modelFunction;


    public AdvancedModel(ResourceLocation modelLoc) {
        super(RenderType::entityCutoutNoCull);
        if (modelLoc != null) {
            renderers = JsonUtils.getModelRenderers(modelLoc,modelFunction);
        }
    }


    public void copyParameters(AdvancedModel<T> model){
        for (ModelElement element : getElements()) {
            for (ModelElement otherElement : model.getElements()) {
                element.showModel = otherElement.showModel;
                element.rotationPoint = otherElement.rotationPoint;
                element.customRotationX = otherElement.customRotationX;
                element.customRotationY = otherElement.customRotationY;
                element.customRotationZ = otherElement.customRotationZ;
                element.defaultRotation = otherElement.defaultRotation;
                element.positionX = otherElement.positionX;
                element.positionY = otherElement.positionY;
                element.positionZ = otherElement.positionZ;
                element.rotationX = otherElement.rotationX;
                element.rotationY = otherElement.rotationY;
                element.rotationZ = otherElement.rotationZ;
                element.scaleX = otherElement.scaleX;
                element.scaleY = otherElement.scaleY;
                element.scaleZ = otherElement.scaleZ;
            }
        }
    }

    public void renderItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, int packedLightIn){
        ISARendered renderTarget = modelWrapper.getRenderTarget();
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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        renderWithLayers(poseStack,buffer,packedLight,packedOverlay,red,green,blue,alpha,RenderCall.MODEL,false);
    }


    public void renderWithLayers(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
        renderModel(stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha,renderCall,resetBuffer);
        renderLayers(stack,getModelWrapper().getMultiBufferSource(),getRenderTarget(),packedLightIn,Minecraft.getInstance().getPartialTick(),red,green,blue,alpha);
    }

    public void renderModel(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
        for (ModelElement modelRenderer : renderers) {
            if (modelRenderer.isRoot) {
                modelRenderer.render(this, stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha, renderCall, resetBuffer);
            }
        }
    }

    public void reset(){
        for (ModelElement renderer : renderers) {
            renderer.reset();
        }
    }

    public void setupAnim(T animated, float limbSwing, float limbSwingAmount, float ticks,float headYaw, float headPitch) {
        float partialTicks = Minecraft.getInstance().getPartialTick();
        if (!Minecraft.getInstance().isPaused()) {
            float smoothness = 2;
            float delta = partialTicks/smoothness;
            animate(animated, limbSwing, limbSwingAmount, ticks, delta, headYaw, headPitch);
        }
        customAnimate(animated,limbSwing,limbSwingAmount,ticks,partialTicks,headYaw,headPitch);
    }

    public void lookAt(ModelElement renderer, float headPitch, float headYaw){
        renderer.setCustomRotation(new Vector3(MathUtils.rad(headPitch),MathUtils.rad(headYaw),0));
    }

    public void setTextureSize(float width, float height) {
        this.textureSize = new Vector2(width,height);
    }


    public void onRenderModelCube(ModelElement cube, PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){}

    public void animate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch){};

    public void customAnimate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch){}


    public ModelElement getModelElement(String name){
        for (ModelElement renderer : renderers) {
            if (renderer.getName().equals(name)){
                return renderer;
            }
        }
        return null;
    }


    public Set<ModelElement> getElements() {
        return renderers;
    }


    public Vector3 getRotationPoint(ModelElement renderer){
        for (ModelElement advancedCubeRenderer : renderers) {
            if (advancedCubeRenderer.isChild(renderer)){
                return getRotationPoint(advancedCubeRenderer).add(renderer.rotationPoint);
            }
        }
        return renderer.rotationPoint;
    }

    public VertexConsumer returnDefaultBuffer(){
        return modelWrapper.getMultiBufferSource().getBuffer(modelWrapper.getRenderType());
    }

    public abstract ResourceLocation getTexture(T target);

    public T getRenderTarget() {
        return modelWrapper.getRenderTarget();
    }


    public void renderLayers(PoseStack poseStack, MultiBufferSource bufferSource, T instance, int packedLight, float partialTick, float r, float g, float b, float a){
        for(ModelRenderLayer<T,AdvancedModel<T>> layer : layers) {
            layer.render(poseStack, bufferSource,instance, packedLight,partialTick,r,g,b,a);
        }
    }

    public <M extends AdvancedModel<T>> void addLayer(ModelRenderLayer<T,M> layer){
        layers.add((ModelRenderLayer<T, AdvancedModel<T>>) layer);
    }

    public List<ModelRenderLayer<T,AdvancedModel<T>>> getLayers(){
        return layers;
    }

    public Vector2 getTextureSize() {
        return textureSize;
    }

    public IModelWrapper getModelWrapper() {
        return modelWrapper;
    }

    public void setModelFunction(Function<String, String> function) {
        this.modelFunction = function;
    }
}
