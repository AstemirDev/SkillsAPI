package org.astemir.api.lib.shimmer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IDynamicLightEntity {

    @OnlyIn(Dist.CLIENT)
    DynamicEntityLight getOrCreateLight();
}
