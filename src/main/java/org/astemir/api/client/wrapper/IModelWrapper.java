package org.astemir.api.client.wrapper;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.common.animation.ITESRModel;

public interface IModelWrapper<M extends ITESRModel> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType();

    AdvancedModel<M> getModel();

    M getRenderTarget();
}
