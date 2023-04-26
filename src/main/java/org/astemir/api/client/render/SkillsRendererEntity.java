package org.astemir.api.client.render;


import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.client.wrapper.SkillsWrapperEntity;
import org.astemir.api.common.misc.ICustomRendered;

import java.util.List;


public class SkillsRendererEntity<T extends Entity & ICustomRendered,M extends EntityModel<T>> extends EntityRenderer<T> implements ISkillsRenderer<T, IDisplayArgument>{

    private SkillsWrapperEntity<T> entityModelWrapper;
    private M model;
    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();


    public SkillsRendererEntity(EntityRendererProvider.Context context, SkillsWrapperEntity entityModel) {
        super(context);
        this.entityModelWrapper = entityModel;
        this.model = (M) entityModel;
    }

    public final boolean addLayer(RenderLayer<T, M> layer) {
        return this.layers.add(layer);
    }

    @Override
    public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        entityModelWrapper.renderTarget = entity;
        entityModelWrapper.multiBufferSource = bufferSource;
        float f1 = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float f2 = Mth.rotLerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.pushPose();
        setupRotations(entity,poseStack,yaw,partialTick);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        scale(entity,poseStack,partialTick);
        poseStack.translate(0.0D, (double)-1.501F, 0.0D);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(entityModelWrapper.getRenderType());
        entityModelWrapper.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entity.isSpectator()) {
            for(RenderLayer<T, M> renderLayer : this.layers) {
                renderLayer.render(poseStack, bufferSource, packedLight, entity, 0, 0, partialTick, 0, f1, f2);
            }
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void animate(T instance, float partialTicks) {
        float f1 = Mth.rotLerp(partialTicks, instance.yRotO, instance.getYRot());
        float f2 = Mth.rotLerp(partialTicks, instance.xRotO, instance.getXRot());
        float tickCount = getTicks(entityModelWrapper.getRenderTarget().tickCount);
        entityModelWrapper.animate(instance,0,0,tickCount,f1,f2);
    }

    protected void setupRotations(T entity, PoseStack stack, float yaw, float partialTicks) {
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
    }

    protected void scale(T entity,PoseStack stack, float partialTicks){}

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entityModelWrapper.getModel(entity).getTexture(entity);
    }


    @Override
    protected boolean shouldShowName(T entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(entity, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (entity instanceof Mob mob){
                Entity leashHolder = mob.getLeashHolder();
                return leashHolder != null ? camera.isVisible(entity.getBoundingBoxForCulling()) : false;
            }
        }
        return false;
    }

    @Override
    public IModelWrapper getModelWrapper() {
        return entityModelWrapper;
    }
}
