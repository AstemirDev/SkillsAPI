package org.astemir.api.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkUtils {

    public static SimpleChannel createNetworkChannel(String modId,String name){
        String networkVersion = "1.3";
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modId,name), () -> networkVersion, (v) -> v.equals(networkVersion), (v) -> v.equals(networkVersion));
        return channel;
    }

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
