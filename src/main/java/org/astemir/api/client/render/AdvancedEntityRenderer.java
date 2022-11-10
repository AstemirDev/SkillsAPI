package org.astemir.api.client.render;


import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.client.model.EntityModelWrapper;
import org.astemir.api.common.animation.ITESRModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class AdvancedEntityRenderer<T extends Entity,M extends EntityModel<T>> extends EntityRenderer<T> {

    private EntityModelWrapper entityModelWrapper;
    private M model;
    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();


    public AdvancedEntityRenderer(EntityRendererProvider.Context context, EntityModelWrapper entityModel) {
        super(context);
        this.entityModelWrapper = entityModel;
        this.model = (M) entityModel;
    }

    public final boolean addLayer(RenderLayer<T, M> p_115327_) {
        return this.layers.add(p_115327_);
    }

    @Override
    public void render(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        entityModelWrapper.renderTarget = p_115308_;
        entityModelWrapper.multiBufferSource = p_115312_;
        float f1 = Mth.rotLerp(p_115310_, p_115308_.yRotO, p_115308_.getYRot());
        float f2 = Mth.rotLerp(p_115310_, p_115308_.xRotO, p_115308_.getXRot());
        p_115311_.pushPose();
        p_115311_.mulPose(Vector3f.YP.rotationDegrees(180.0F - f1));
        float tickCount = ((float)entityModelWrapper.getRenderTarget().tickCount)+Minecraft.getInstance().getPartialTick();
        p_115311_.scale(-1.0F, -1.0F, 1.0F);
        p_115311_.translate(0.0D, (double)-1.501F, 0.0D);
        entityModelWrapper.setupAnim(p_115308_,0,0,tickCount,f1,f2);

        VertexConsumer vertexconsumer = p_115312_.getBuffer(getRenderType(p_115308_));
        this.model.renderToBuffer(p_115311_, vertexconsumer, p_115313_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!p_115308_.isSpectator()) {
            for(RenderLayer<T, M> renderlayer : this.layers) {
                renderlayer.render(p_115311_, p_115312_, p_115313_, p_115308_, 0, 0, p_115310_, 0, f1, f2);
            }
        }
        p_115311_.popPose();

        super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
    }

    public RenderType getRenderType(T entity) {
        ResourceLocation texture = getTextureLocation(entity);
        return entityModelWrapper.getRenderType(entity,texture);
    }


    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entityModelWrapper.getTexture(entity);
    }


    @Override
    protected boolean shouldShowName(T p_115506_) {
        return super.shouldShowName(p_115506_) && (p_115506_.shouldShowName() || p_115506_.hasCustomName() && p_115506_ == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public boolean shouldRender(T p_115468_, Frustum p_115469_, double p_115470_, double p_115471_, double p_115472_) {
        if (super.shouldRender(p_115468_, p_115469_, p_115470_, p_115471_, p_115472_)) {
            return true;
        } else {
            if (p_115468_ instanceof Mob){
                Entity entity = ((Mob)p_115468_).getLeashHolder();
                return entity != null ? p_115469_.isVisible(entity.getBoundingBoxForCulling()) : false;
            }
        }
        return false;
    }
}
