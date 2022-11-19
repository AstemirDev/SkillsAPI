package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;

public abstract class GFXEffect{

    public abstract void update();

    public abstract void read(FriendlyByteBuf buf);

    public abstract void write(FriendlyByteBuf buf);

    public abstract boolean isRemoved();

    public abstract GFXEffectType getEffectType();
}
