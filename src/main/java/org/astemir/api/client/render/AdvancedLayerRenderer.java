package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import org.astemir.api.client.SkillsRenderTypes;
import org.astemir.api.client.wrapper.AbstractModelWrapperEntity;
import org.astemir.api.common.misc.ICustomRendered;

public class AdvancedLayerRenderer<T extends Entity & ICustomRendered, M extends AbstractModelWrapperEntity<T>> extends RenderLayer<T, M> {

    public AdvancedLayerRenderer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float swingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        renderWrapper(poseStack, bufferSource, packedLight, entity, limbSwing, swingAmount, partialTick, ageInTicks, netHeadYaw, headPitch,1,1,1,1);
    }

    public void renderWrapper(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float swingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, float r, float g, float b, float a){
        VertexConsumer vertexconsumer = bufferSource.getBuffer(getBuffer(entity));
        this.getParentModel().renderWrapper(poseStack, vertexconsumer, getEyeLight(packedLight), OverlayTexture.NO_OVERLAY, r, g, b, a, RenderCall.LAYER,false);
    }


    public RenderType getBuffer(T entity){
        return SkillsRenderTypes.eyesTransparent(getTextureLocation(entity));
    }


    public int getEyeLight(int light){
        return 15728640;
    }
}
