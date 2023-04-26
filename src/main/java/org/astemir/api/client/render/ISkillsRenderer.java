package org.astemir.api.client.render;

import net.minecraft.client.Minecraft;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.common.misc.ICustomRendered;

public interface ISkillsRenderer<T extends ICustomRendered,K extends IDisplayArgument> {

    default float getTicks(int ticks){
        return ((float)ticks)+ Minecraft.getInstance().getPartialTick();
    }

    void animate(T instance,float partialTicks);

    IModelWrapper<T,K> getModelWrapper();
}
