package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class DataUtils {


    public static ResourceLocation getBlockDefaultTextureLocation(ResourceLocation location){
        return new ResourceLocation(location.getNamespace(), "block/" + location.getPath());
    }

    public static ModelFile.UncheckedModelFile modelFile(String path){
        return new ModelFile.UncheckedModelFile(path);
    }


    public static ModelFile.UncheckedModelFile modelFile(String modId,String path){
        return new ModelFile.UncheckedModelFile(modId+":"+path);
    }

    public static ResourceLocation getItemDefaultTextureLocation(ResourceLocation location){
        return new ResourceLocation(location.getNamespace(), "item/" + location.getPath());
    }

    public static ResourceLocation getItemResourceLocation(Item item){
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static ResourceLocation getBlockResourceLocation(Block block){
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
