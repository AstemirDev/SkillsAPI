package org.astemir.api.common.gfx;

import net.minecraft.network.FriendlyByteBuf;
import org.astemir.api.math.components.Color;
import org.astemir.api.common.entity.ClientSideValue;
import org.astemir.api.network.NetworkUtils;

public class GFXBlackOut extends GFXEffect{

    private ClientSideValue value = new ClientSideValue(1,0);
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
        return Math.abs(value.valueO) <= 0.001f;
    }


    public ClientSideValue getValue() {
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
