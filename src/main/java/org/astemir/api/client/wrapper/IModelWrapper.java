package org.astemir.api.client.wrapper;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.common.animation.ISARendered;

public interface IModelWrapper<M extends ISARendered> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType();

    AdvancedModel<M> getModel(M target);

    M getRenderTarget();
}
