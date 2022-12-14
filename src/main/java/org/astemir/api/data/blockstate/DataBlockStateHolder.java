package org.astemir.api.data.blockstate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.astemir.api.utils.ResourceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataBlockStateHolder {

    private DataBlockState type;
    private Block block;
    private Map<String, ResourceLocation> materials = new HashMap<>();

    public DataBlockStateHolder(Block block, DataBlockState type) {
        this.block = block;
        this.type = type;
    }

    public DataBlockStateHolder(Supplier<Block> blockSupplier, DataBlockState type) {
        this.block = blockSupplier.get();
        this.type = type;
    }

    public void customBuild(){}

    public DataBlockState getType() {
        return type;
    }

    public DataBlockStateHolder material(String name, Block block){
        this.materials.put(name, ResourceUtils.getBlockTexture(block));
        return this;
    }

    public DataBlockStateHolder material(String name, Supplier<Block> block){
        this.materials.put(name, ResourceUtils.getBlockTexture(block.get()));
        return this;
    }

    public DataBlockStateHolder material(String name, ResourceLocation texture){
        this.materials.put(name,texture);
        return this;
    }

    public Block getBlock() {
        return block;
    }

    public ResourceLocation getMaterial(String name) {
        return materials.get(name);
    }
}
