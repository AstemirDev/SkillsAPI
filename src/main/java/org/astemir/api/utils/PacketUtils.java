package org.astemir.api.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

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

}
