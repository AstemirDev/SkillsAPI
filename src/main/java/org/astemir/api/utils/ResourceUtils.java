package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

public class ResourceUtils {

    public static final ResourceLocation loadResource(String modId,String path){
        return new ResourceLocation(modId,path);
    }

    public static ResourceLocation loadAnimation(String modId,String path){
        return loadResource(modId,"animations/"+path);
    }

    public static ResourceLocation loadTexture(String modId,String path){
        return loadResource(modId,"textures/"+path);
    }

    public static ResourceLocation loadModel(String modId,String path){
        return loadResource(modId,"models/"+path);
    }


    public static ModelFile.UncheckedModelFile createModelFile(String path){
        return new ModelFile.UncheckedModelFile(path);
    }

    public static ModelFile.UncheckedModelFile createModelFile(String modId,String path){
        return new ModelFile.UncheckedModelFile(modId+":"+path);
    }

    public static ResourceLocation getItemTexture(ResourceLocation location){
        return new ResourceLocation(location.getNamespace(), "item/" + location.getPath());
    }

    public static ResourceLocation getBlockTexture(ResourceLocation location){
        return new ResourceLocation(location.getNamespace(), "block/" + location.getPath());
    }

    public static ResourceLocation getItemTexture(Item item){
        ResourceLocation location = getItemLocation(item);
        return new ResourceLocation(location.getNamespace(), "item/" + location.getPath());
    }

    public static ResourceLocation getBlockTexture(Block block){
        ResourceLocation location = getBlockLocation(block);
        return new ResourceLocation(location.getNamespace(), "block/" + location.getPath());
    }

    public static String getItemId(Item item){
        return getItemLocation(item).getPath();
    }

    public static String getBlockId(Block block){
        return getBlockLocation(block).getPath();
    }

    public static ResourceLocation getItemLocation(Item item){
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static ResourceLocation getBlockLocation(Block block){
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
