package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;
import org.astemir.api.math.Color;
import org.astemir.api.math.InterpoleValue;
import org.astemir.api.network.PacketUtils;

public class GFXBlackInOut extends GFXEffect{

    private InterpoleValue firstValue = new InterpoleValue(0,1);
    private InterpoleValue secondValue = new InterpoleValue(1,0);
    private Color color;
    private double speed;
    private boolean out = false;

    public GFXBlackInOut(Color color, double speed) {
        this.color = color;
        this.speed = (float) speed/10f;
    }

    public GFXBlackInOut() {
    }

    @Override
    public void update() {
        if (out){
            secondValue.update((float)speed);
        }else{
            if (firstValue.valueO >= 0.99f){
                out = true;
            }
            firstValue.update((float)speed);
        }
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
        if (out) {
            return Math.abs(secondValue.valueO) <= 0.001f;
        }
        return false;
    }


    public InterpoleValue getValue() {
        if (out){
            return secondValue;
        }else{
            return firstValue;
        }
    }

    public Color getColor() {
        return color;
    }

    @Override
    public GFXEffectType getEffectType() {
        return GFXEffectType.BLACK_IN_OUT;
    }
}
