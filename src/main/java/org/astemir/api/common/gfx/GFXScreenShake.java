package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;

public class GFXScreenShake extends GFXEffect{

    private double shakePower = 0.25f;
    private int ticks = 0;

    public GFXScreenShake(double shakePower, int ticks) {
        this.shakePower = shakePower;
        this.ticks = ticks;
    }

    public GFXScreenShake() {
    }

    @Override
    public void update() {
        if (ticks > 0){
            ticks--;
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.shakePower = buf.readDouble();
        this.ticks = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(shakePower);
        buf.writeInt(ticks);
    }


    public double getShakePower() {
        return shakePower;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public boolean isRemoved() {
        return ticks <= 0;
    }

    @Override
    public GFXEffectType getEffectType() {
        return GFXEffectType.SCREEN_SHAKE;
    }
}
