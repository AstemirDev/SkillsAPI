package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;

public class ResourceUtils {

    public static final ResourceLocation resource(String modId,String path){
        return new ResourceLocation(modId,path);
    }

    public static ResourceLocation animation(String modId,String path){
        return resource(modId,"animations/"+path);
    }

    public static ResourceLocation texture(String modId,String path){
        return resource(modId,"textures/"+path);
    }

    public static ResourceLocation model(String modId,String path){
        return resource(modId,"models/"+path);
    }
}
