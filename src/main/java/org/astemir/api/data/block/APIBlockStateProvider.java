package org.astemir.api.data.block;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.math.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


import static org.astemir.api.utils.ResourceUtils.*;


public class APIBlockStateProvider extends BlockStateProvider {

    private List<Pair<Block, BlockStateHolder>> blockStates = new ArrayList<>();

    public APIBlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper) {
        super(gen, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Pair<Block, BlockStateHolder> blockStatePair : blockStates) {
            Block block = blockStatePair.getKey();
            BlockStateHolder blockStateHolder = blockStatePair.getValue();
            switch (blockStateHolder.getType()){
                case EMPTY -> emptyBlock(block);
                case DEFAULT -> simpleBlock(block);
                case MIRRORED -> mirroredBlock(block);
                case HORIZONTAL -> horizontalBlock(block, getBlockTexture(block),getBlockTexture(block),getBlockTexture(blockStateHolder.getMaterial()));
                case CHISELED -> chiseledBlock(block,getBlockTexture(block),getBlockTexture(blockStateHolder.getMaterial()));
                case COLUMN -> axisBlock((RotatedPillarBlock) block,getBlockTexture(block),getBlockTexture(blockStateHolder.getMaterial()));
                case SLAB -> slabBlock((SlabBlock) block, getBlockTexture(blockStateHolder.getMaterial()),getBlockTexture(blockStateHolder.getMaterial()));
                case BUTTON -> buttonBlock((ButtonBlock) block,getBlockTexture(blockStateHolder.getMaterial()));
                case FENCE -> fenceBlock((FenceBlock) block, getBlockTexture(blockStateHolder.getMaterial()));
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, getBlockTexture(blockStateHolder.getMaterial()));
                case WALL -> wallBlock((WallBlock) block, blockStateHolder,getBlockTexture(blockStateHolder.getMaterial()));
                case STAIRS -> stairsBlock((StairBlock) block, getBlockTexture(blockStateHolder.getMaterial()));
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, getBlockTexture(blockStateHolder.getMaterial()));
            }
        }
    }

    public void wallBlock(WallBlock block, BlockStateHolder stateHolder,ResourceLocation texture) {
        wallBlock(block,texture);
        wallInventory(block,stateHolder);
    }

    public void load(Block block, BlockStateType type){
        this.blockStates.add(new Pair<>(block,new BlockStateHolder(type)));
    }

    public void load(Supplier<Block> blockSupplier, BlockStateType type){
        this.blockStates.add(new Pair<>(blockSupplier.get(),new BlockStateHolder(type)));
    }

    public void load(Block block, BlockStateType type,Block material){
        this.blockStates.add(new Pair<>(block,new BlockStateHolder(type,material)));
    }

    public void load(Supplier<Block> blockSupplier, BlockStateType type,Block material){
        this.blockStates.add(new Pair<>(blockSupplier.get(),new BlockStateHolder(type,material)));
    }

    public void mirroredBlock(Block block){
        simpleBlock(block,new ConfiguredModel(cubeAll(block)),new ConfiguredModel(cubeMirroredAll(block)),new ConfiguredModel(cubeAll(block),0,180,false),new ConfiguredModel(cubeMirroredAll(block),0,180,false));
    }

    public void emptyBlock(Block block){
        simpleBlock(block,new ConfiguredModel(empty(block)));
    }

    public void chiseledBlock(Block block, ResourceLocation side, ResourceLocation end) {
        simpleBlock(block, new ConfiguredModel(models().cubeColumn(getBlockId(block), side, end)));
    }

    public BlockModelBuilder cubeMirroredAll(Block block) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString()+"_mirrored")
                .parent(createModelFile("block/cube_mirrored_all"))
                .texture("all", getBlockTexture(location));
    }

    public BlockModelBuilder empty(Block block) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString())
                .texture("particle", getBlockTexture(location));
    }


    public BlockModelBuilder wallInventory(Block block,BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString()+"_inventory")
                .parent(createModelFile("block/wall_inventory"))
                .texture("wall", getBlockTexture(stateHolder.getMaterial()));
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    private class BlockStateHolder{

        private BlockStateType type;
        private Block material;

        public BlockStateHolder(BlockStateType type) {
            this.type = type;
        }

        public BlockStateHolder(BlockStateType type, Block material) {
            this.type = type;
            this.material = material;
        }

        public BlockStateType getType() {
            return type;
        }

        public Block getMaterial() {
            return material;
        }
    }
}
