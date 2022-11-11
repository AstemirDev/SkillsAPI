package org.astemir.api.data.block;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.astemir.api.SkillsAPI;
import org.astemir.api.math.Pair;
import org.astemir.api.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.astemir.api.utils.DataUtils.*;


public class ModBlockStateProvider extends BlockStateProvider {

    private List<Pair<Block, BlockStateHolder>> blockStates = new ArrayList<>();

    public ModBlockStateProvider(DataGenerator gen, String modId,ExistingFileHelper exFileHelper) {
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
                case HORIZONTAL -> horizontalBlock(block, getDefaultBlockTexture(block),getDefaultBlockTexture(block),getMaterialBlockTexture(blockStateHolder));
                case CHISELED -> chiseledBlock(block,getDefaultBlockTexture(block),getMaterialBlockTexture(blockStateHolder));
                case COLUMN -> axisBlock((RotatedPillarBlock) block,getDefaultBlockTexture(block),getMaterialBlockTexture(blockStateHolder));
                case SLAB -> slabBlock((SlabBlock) block, getMaterialBlockTexture(blockStateHolder),getMaterialBlockTexture(blockStateHolder));
                case BUTTON -> buttonBlock((ButtonBlock) block,getMaterialBlockTexture(blockStateHolder));
                case FENCE -> fenceBlock((FenceBlock) block, getMaterialBlockTexture(blockStateHolder));
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, getMaterialBlockTexture(blockStateHolder));
                case WALL -> wallBlock((WallBlock) block, blockStateHolder,getMaterialBlockTexture(blockStateHolder));
                case STAIRS -> stairsBlock((StairBlock) block, getMaterialBlockTexture(blockStateHolder));
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, getMaterialBlockTexture(blockStateHolder));
            }
        }
    }

    public void wallBlock(WallBlock block, BlockStateHolder stateHolder,ResourceLocation texture) {
        wallBlock(block,texture);
        wallInventory(block,stateHolder);
    }

    public ResourceLocation getDefaultBlockTexture(Block block){
        return DataUtils.getBlockDefaultTextureLocation(DataUtils.getBlockResourceLocation(block));
    }

    public ResourceLocation getMaterialBlockTexture(BlockStateHolder blockStateHolder){
        return DataUtils.getBlockDefaultTextureLocation(DataUtils.getBlockResourceLocation(blockStateHolder.getMaterial()));
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
        simpleBlock(block, new ConfiguredModel(models().cubeColumn(name(block), side, end)));
    }

    public BlockModelBuilder cubeMirroredAll(Block block) {
        ResourceLocation location = getBlockResourceLocation(block);
        return models().getBuilder(location.toString()+"_mirrored")
                .parent(modelFile("block/cube_mirrored_all"))
                .texture("all", getBlockDefaultTextureLocation(location));
    }

    public BlockModelBuilder empty(Block block) {
        ResourceLocation location = getBlockResourceLocation(block);
        return models().getBuilder(location.toString())
                .texture("particle", getBlockDefaultTextureLocation(location));
    }


    public BlockModelBuilder wallInventory(Block block,BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockResourceLocation(block);
        return models().getBuilder(location.toString()+"_inventory")
                .parent(modelFile("block/wall_inventory"))
                .texture("wall", getMaterialBlockTexture(stateHolder));
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }


    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
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
