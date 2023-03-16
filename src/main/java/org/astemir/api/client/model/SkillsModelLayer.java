package org.astemir.api.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.misc.ICustomRendered;

public abstract class SkillsModelLayer<T extends ICustomRendered,K extends IDisplayArgument,M extends SkillsModel<T,K>> {

    private M model;

    public SkillsModelLayer(M model) {
        this.model = model;
    }

    public void renderModel(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T instance, float red, float green, float blue,float alpha) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(getRenderType(instance));
        model.renderModel(pMatrixStack, vertexconsumer, pPackedLight, 0, red, green, blue, alpha, RenderCall.LAYER,false);
    }

    public void renderModel(PoseStack pMatrixStack, MultiBufferSource pBuffer, RenderType type,int pPackedLight, float red, float green, float blue,float alpha) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(type);
        model.renderModel(pMatrixStack, vertexconsumer, pPackedLight, 0, red, green, blue, alpha, RenderCall.LAYER,false);
    }


    public abstract void render(PoseStack pPoseStack, MultiBufferSource pBuffer, T instance,int pPackedLight, float pPartialTick,float r,float g,float b,float a);

    public RenderType getRenderType(T instance){
        return RenderType.entityCutoutNoCull(getTexture(instance));
    }

    public abstract ResourceLocation getTexture(T instance);

    public M getModel() {
        return model;
    }
}
