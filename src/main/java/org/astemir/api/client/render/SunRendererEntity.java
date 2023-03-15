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
import org.astemir.api.client.wrapper.MWEntity;
import org.astemir.api.common.misc.ICustomRendered;

import java.util.List;


public class SunRendererEntity<T extends Entity & ICustomRendered,M extends EntityModel<T>> extends EntityRenderer<T> {

    private MWEntity entityModelWrapper;
    private M model;
    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();


    public SunRendererEntity(EntityRendererProvider.Context context, MWEntity entityModel) {
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
        float tickCount = ((float)entityModelWrapper.getRenderTarget().tickCount)+Minecraft.getInstance().getPartialTick();
        setupRotations(entity,poseStack,yaw,partialTick);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        scale(entity,poseStack,partialTick);
        poseStack.translate(0.0D, (double)-1.501F, 0.0D);
        entityModelWrapper.setupAnim(entity,0,0,tickCount,f1,f2);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(entityModelWrapper.getRenderType());
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entity.isSpectator()) {
            for(RenderLayer<T, M> renderlayer : this.layers) {
                renderlayer.render(poseStack, bufferSource, packedLight, entity, 0, 0, partialTick, 0, f1, f2);
            }
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
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
}
