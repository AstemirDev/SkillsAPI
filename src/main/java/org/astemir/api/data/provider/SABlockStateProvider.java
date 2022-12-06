package org.astemir.api.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.misc.DataBlockState;
import org.astemir.api.math.Couple;
import org.astemir.api.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


import static org.astemir.api.utils.ResourceUtils.*;


public class SABlockStateProvider extends BlockStateProvider implements IProvider{

    private List<Couple<Block, BlockStateHolder>> blockStates = new ArrayList<>();

    public SABlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper) {
        super(gen, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Couple<Block, BlockStateHolder> blockStatePair : blockStates) {
            Block block = blockStatePair.getKey();
            BlockStateHolder blockStateHolder = blockStatePair.getValue();
            switch (blockStateHolder.getType()){
                case EMPTY -> emptyBlock(block);
                case LOG -> logBlock((RotatedPillarBlock) block);
                case DOOR -> doorBlock(block);
                case DEFAULT -> simpleBlock(block);
                case TRAPDOOR -> trapdoorBlock(block);
                case MIRRORED -> mirroredBlock(block);
                case HORIZONTAL -> horizontalBlock(block, getBlockTexture(block),getBlockTexture(block),blockStateHolder.getMaterial());
                case CHISELED -> chiseledBlock(block,getBlockTexture(block),blockStateHolder.getMaterial());
                case COLUMN -> axisBlock((RotatedPillarBlock) block,getBlockTexture(block),blockStateHolder.getMaterial());
                case SLAB -> slabBlock((SlabBlock) block, blockStateHolder.getMaterial(),blockStateHolder.getMaterial());
                case BUTTON -> buttonBlock((ButtonBlock) block,blockStateHolder.getMaterial());
                case FENCE -> fenceBlock((FenceBlock) block, blockStateHolder.getMaterial());
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, blockStateHolder.getMaterial());
                case WALL -> wallBlock((WallBlock) block, blockStateHolder.getMaterial());
                case STAIRS -> stairsBlock((StairBlock) block, blockStateHolder.getMaterial());
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, blockStateHolder.getMaterial());
            }
        }
    }

    public void wallBlock(WallBlock block, BlockStateHolder stateHolder,ResourceLocation texture) {
        wallBlock(block,texture);
        wallInventory(block,stateHolder);
    }

    public void addState(Block block, BlockStateHolder customHolder){
        this.blockStates.add(new Couple<>(block,customHolder));
    }

    public void addState(Block block, DataBlockState type){
        this.blockStates.add(new Couple<>(block,new BlockStateHolder(type)));
    }

    public void addState(Supplier<Block> blockSupplier, DataBlockState type){
        this.blockStates.add(new Couple<>(blockSupplier.get(),new BlockStateHolder(type)));
    }

    public void addState(Block block, DataBlockState type, Block material){
        this.blockStates.add(new Couple<>(block,new BlockStateHolder(type,material)));
    }

    public void addState(Supplier<Block> blockSupplier, DataBlockState type, Block material){
        this.blockStates.add(new Couple<>(blockSupplier.get(),new BlockStateHolder(type,material)));
    }

    public void mirroredBlock(Block block){
        simpleBlock(block,new ConfiguredModel(cubeAll(block)),new ConfiguredModel(cubeMirroredAll(block)),new ConfiguredModel(cubeAll(block),0,180,false),new ConfiguredModel(cubeMirroredAll(block),0,180,false));
    }

    public void doorBlock(Block block){
        ResourceLocation top = new ResourceLocation(ResourceUtils.getBlockTexture(block)+"_top");
        ResourceLocation bottom = new ResourceLocation(ResourceUtils.getBlockTexture(block)+"_bottom");
        doorBlock((DoorBlock) block,bottom,top);
    }

    public void trapdoorBlock(Block block){
        trapdoorBlock((TrapDoorBlock) block,ResourceUtils.getBlockTexture(block),true);
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

        private DataBlockState type;
        private ResourceLocation[] materials;

        public BlockStateHolder(DataBlockState type) {
            this.type = type;
        }

        public BlockStateHolder(DataBlockState type, Block... blocks) {
            this.type = type;
            this.materials = new ResourceLocation[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                materials[i] = ResourceUtils.getBlockTexture(blocks[i]);
            }
        }

        public BlockStateHolder(DataBlockState type, Supplier<Block>... blocks) {
            this.type = type;
            this.materials = new ResourceLocation[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                materials[i] = ResourceUtils.getBlockTexture(blocks[i].get());
            }
        }

        public BlockStateHolder(DataBlockState type, ResourceLocation... materials) {
            this.type = type;
            this.materials = materials;
        }

        public DataBlockState getType() {
            return type;
        }

        public ResourceLocation getMaterial() {
            return materials[0];
        }

        public ResourceLocation getMaterial(int id) {
            return materials[id];
        }

        public ResourceLocation[] getMaterials() {
            return materials;
        }
    }
}
