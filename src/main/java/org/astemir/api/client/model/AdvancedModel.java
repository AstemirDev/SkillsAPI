package org.astemir.api.client.model;

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
import org.astemir.api.client.misc.AdvancedCubeRenderer;
import org.astemir.api.client.misc.RenderCall;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.common.animation.ITESRModel;
import org.astemir.api.utils.JsonUtils;
import org.astemir.api.utils.MathUtils;
import org.astemir.api.math.Vector2;
import org.astemir.api.math.Vector3;
import java.util.*;

public abstract class AdvancedModel<T extends ITESRModel> extends Model {

    public Set<AdvancedCubeRenderer> renderers = new HashSet<>();
    public IModelWrapper<T> modelWrapper;

    public Vector2 textureSize = new Vector2(64,32);

    public AdvancedModel(ResourceLocation modelLoc) {
        super(RenderType::entityCutoutNoCull);
        if (modelLoc != null) {
            renderers = JsonUtils.getModelRenderers(modelLoc);
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
        for (AdvancedCubeRenderer modelRenderer : renderers) {
            if (modelRenderer.isRoot) {
                modelRenderer.render(this, stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha, renderCall, resetBuffer);
            }
        }
    }

    public void reset(){
        for (AdvancedCubeRenderer renderer : renderers) {
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

    public void lookAt(AdvancedCubeRenderer renderer,float headPitch,float headYaw){
        renderer.setCustomRotation(new Vector3(MathUtils.rad(headPitch),MathUtils.rad(headYaw),0));
    }

    public void setTextureSize(float width, float height) {
        this.textureSize = new Vector2(width,height);
    }


    public void onRenderModelCube(AdvancedCubeRenderer cube,PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall,int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha){}

    public void animate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch){};

    public void customAnimate(T animated, float limbSwing, float limbSwingAmount, float ticks,float delta, float headYaw, float headPitch){}


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


    public Vector3 getRotationPoint(AdvancedCubeRenderer renderer){
        for (AdvancedCubeRenderer advancedCubeRenderer : renderers) {
            if (advancedCubeRenderer.isChild(renderer)){
                return getRotationPoint(advancedCubeRenderer).add(renderer.rotationPoint);
            }
        }
        return renderer.rotationPoint;
    }

    public VertexConsumer returnDefaultBuffer(){
        return modelWrapper.getMultiBufferSource().getBuffer(modelWrapper.getRenderType());
    }

    public abstract ResourceLocation getTexture();

    public T getRenderTarget() {
        return modelWrapper.getRenderTarget();
    }


    public Vector2 getTextureSize() {
        return textureSize;
    }

    public IModelWrapper getModelWrapper() {
        return modelWrapper;
    }
}
