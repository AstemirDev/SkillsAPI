package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;

public class ResourceUtils {

    public static ResourceLocation animation(String path){
        return SkillsAPI.resource("animations/"+path);
    }

    public static ResourceLocation texture(String path){
        return SkillsAPI.resource("textures/"+path);
    }

    public static ResourceLocation model(String path){
        return SkillsAPI.resource("models/"+path);
    }
}
