package org.astemir.api.client.wrapper;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.common.misc.ICustomRendered;

public interface IModelWrapper<T extends ICustomRendered,K> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType();

    AdvancedModel<T,K> getModel(T target);

    T getRenderTarget();
}
