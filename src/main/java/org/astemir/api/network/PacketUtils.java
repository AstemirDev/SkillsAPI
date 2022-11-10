package org.astemir.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;

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

    public static void writeAnimationsMap(Map<String,Integer> map,FriendlyByteBuf buffer){
        buffer.writeInt(map.size());
        map.forEach((key,tick)->{
            buffer.writeUtf(key);
            buffer.writeInt(tick);
        });
    }

    public static Map<String,Integer> readAnimationsMap(FriendlyByteBuf buffer){
        Map<String,Integer> animationsMap = new HashMap<>();
        int count = buffer.readInt();
        for (int i = 0;i<count;i++){
            animationsMap.put(buffer.readUtf(),buffer.readInt());
        }
        return animationsMap;
    }

}
