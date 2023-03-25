package org.astemir.api.client.render;

import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.common.misc.ICustomRendered;

public interface ISkillsRenderer<T extends ICustomRendered,K extends IDisplayArgument> {

    IModelWrapper<T,K> getModelWrapper();
}
