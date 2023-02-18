package org.astemir.api.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.common.misc.ICustomRendered;

public abstract class ModelRenderLayer<T extends ICustomRendered,K,M extends AdvancedModel<T,K>> {

    private M model;

    public ModelRenderLayer(M model) {
        this.model = model;
    }

    protected void renderModel(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T instance, float pRed, float pGreen, float pBlue) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(getRenderType(instance));
        model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, 0, pRed, pGreen, pBlue, 1.0F);
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
