package org.astemir.api.client.model;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.common.animation.ITESRModel;

public interface IModelWrapper<M extends ITESRModel> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType(M renderTarget, ResourceLocation texture);

    AdvancedModel<M> getModel(M target);

    ResourceLocation getTexture(M target);

    M getRenderTarget();
}
