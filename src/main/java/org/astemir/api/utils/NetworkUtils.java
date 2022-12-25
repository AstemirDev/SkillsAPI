package org.astemir.api.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.astemir.api.math.Color;

public class NetworkUtils {

    public static SimpleChannel createNetworkChannel(String modId,String name,String protocolVersion){
        return NetworkRegistry.ChannelBuilder.
                named(new ResourceLocation(modId,name)).
                clientAcceptedVersions(protocolVersion::equals).
                serverAcceptedVersions(protocolVersion::equals).
                networkProtocolVersion(() -> protocolVersion).
                simpleChannel();
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
