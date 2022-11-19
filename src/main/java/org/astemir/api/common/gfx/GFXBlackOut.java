package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;
import org.astemir.api.math.Color;
import org.astemir.api.math.InterpoleValue;
import org.astemir.api.network.PacketUtils;

public class GFXBlackOut extends GFXEffect{

    private InterpoleValue value = new InterpoleValue(1,0);
    private Color color;
    private double speed;

    public GFXBlackOut(Color color, double speed) {
        this.color = color;
        this.speed = (float) speed/10f;
    }

    public GFXBlackOut() {
    }

    @Override
    public void update() {
        value.update((float) speed);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.color = PacketUtils.readColor(buf);
        this.speed = buf.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        PacketUtils.writeColor(buf,color);
        buf.writeDouble(speed);
    }


    @Override
    public boolean isRemoved() {
        return Math.abs(value.valueO) <= 0.001f;
    }


    public InterpoleValue getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public GFXEffectType getEffectType() {
        return GFXEffectType.BLACK_OUT;
    }
}
