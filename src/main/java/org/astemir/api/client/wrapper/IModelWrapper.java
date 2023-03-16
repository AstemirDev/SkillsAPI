package org.astemir.api.client.wrapper;


import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.common.misc.ICustomRendered;

public interface IModelWrapper<T extends ICustomRendered,K extends IDisplayArgument> {

    MultiBufferSource getMultiBufferSource();

    RenderType getRenderType();

    SkillsModel<T,K> getModel(T target);

    T getRenderTarget();
}
