package org.astemir.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.math.Color;

import java.util.HashMap;
import java.util.Map;

public class PacketUtils {

    public static Vec3 readVec3(FriendlyByteBuf buffer){
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new Vec3(x,y,z);
    }

    public static void writeVec3(FriendlyByteBuf buffer,Vec3 vec3){
        buffer.writeDouble(vec3.x);
        buffer.writeDouble(vec3.y);
        buffer.writeDouble(vec3.z);
    }

    public static Color readColor(FriendlyByteBuf buffer){
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        float a = buffer.readFloat();
        return new Color(r,g,b,a);
    }

    public static void writeColor(FriendlyByteBuf buffer,Color color){
        buffer.writeFloat(color.r);
        buffer.writeFloat(color.g);
        buffer.writeFloat(color.b);
        buffer.writeFloat(color.a);
    }

}
