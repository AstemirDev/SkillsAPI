package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;

public class ResourceUtils {

    public static final ResourceLocation resource(String path){
        return new ResourceLocation(SkillsAPI.API.MOD_ID,path);
    }

    public static ResourceLocation animation(String path){
        return resource("animations/"+path);
    }

    public static ResourceLocation texture(String path){
        return resource("textures/"+path);
    }

    public static ResourceLocation model(String path){
        return resource("models/"+path);
    }
}
