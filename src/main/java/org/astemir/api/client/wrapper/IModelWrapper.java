package org.astemir.api.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.model.ModelRenderLayer;
import org.astemir.api.common.animation.ISARendered;

import java.util.List;

public interface IModelWrapper<T extends ISARendered> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType();

    AdvancedModel<T> getModel(T target);

    T getRenderTarget();
}
