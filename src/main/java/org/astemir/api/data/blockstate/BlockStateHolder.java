package org.astemir.api.data.blockstate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.astemir.api.utils.ResourceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.astemir.api.utils.ResourceUtils.getBlockTexture;

public class BlockStateHolder {

    private BlockStateType type;
    private Block block;
    private Map<String, ResourceLocation> materials = new HashMap<>();

    public BlockStateHolder(Block block, BlockStateType type) {
        this.block = block;
        this.type = type;
    }

    public BlockStateHolder(Supplier<Block> blockSupplier, BlockStateType type) {
        this.block = blockSupplier.get();
        this.type = type;
    }

    public void loadDefaultTexturePaths(){
        ResourceLocation texture = getBlockTexture(block);
        material("texture",texture);
        material("particle",texture);
        material("side",texture);
        material("front",texture);
        material("top",new ResourceLocation(texture.getNamespace(),texture.getPath()+"_top"));
        material("bottom",new ResourceLocation(texture.getNamespace(),texture.getPath()+"_bottom"));
        material("double",texture);
    }

    public void customBuild(){}

    public BlockStateType getType() {
        return type;
    }

    public BlockStateHolder material(String name, Block block){
        this.materials.put(name, ResourceUtils.getBlockTexture(block));
        return this;
    }

    public BlockStateHolder material(String name, Supplier<Block> block){
        this.materials.put(name, ResourceUtils.getBlockTexture(block.get()));
        return this;
    }

    public BlockStateHolder material(String name, ResourceLocation texture){
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
