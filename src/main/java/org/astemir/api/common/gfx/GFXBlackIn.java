package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;
import org.astemir.api.math.Color;
import org.astemir.api.math.InterpoleValue;
import org.astemir.api.utils.NetworkUtils;

public class GFXBlackIn extends GFXEffect{

    private InterpoleValue value = new InterpoleValue(0,1);
    private Color color;
    private double speed;

    public GFXBlackIn(Color color, double speed) {
        this.color = color;
        this.speed = (float) speed/10f;
    }

    public GFXBlackIn() {
    }

    @Override
    public void update() {
        value.update((float) speed);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.color = NetworkUtils.readColor(buf);
        this.speed = buf.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        NetworkUtils.writeColor(buf,color);
        buf.writeDouble(speed);
    }


    @Override
    public boolean isRemoved() {
        return Math.abs(value.valueO) >= 0.99f;
    }


    public InterpoleValue getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public GFXEffectType getEffectType() {
        return GFXEffectType.BLACK_IN;
    }
}
