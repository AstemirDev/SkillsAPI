package org.astemir.api.data.provider;

import net.minecraft.client.renderer.RenderType;
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
                case EMPTY -> createEmptyBlock(block);
                case LOG -> logBlock((RotatedPillarBlock) block);
                case DOOR -> createDoorBlock(block);
                case DEFAULT -> simpleBlock(block);
                case TRAPDOOR -> createTrapdoorBlock(block);
                case MIRRORED -> createMirroredBlock(block);
                case HORIZONTAL -> horizontalBlock(block, getBlockTexture(block),getBlockTexture(block),blockStateHolder.getMaterial());
                case CHISELED -> createChiseledBlock(block,getBlockTexture(block),blockStateHolder.getMaterial());
                case COLUMN -> axisBlock((RotatedPillarBlock) block,getBlockTexture(block),blockStateHolder.getMaterial());
                case SLAB -> slabBlock((SlabBlock) block, blockStateHolder.getMaterial(),blockStateHolder.getMaterial());
                case BUTTON -> buttonBlock((ButtonBlock) block,blockStateHolder.getMaterial());
                case FENCE -> fenceBlock((FenceBlock) block, blockStateHolder.getMaterial());
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, blockStateHolder.getMaterial());
                case WALL -> createWallBlock((WallBlock) block, blockStateHolder,blockStateHolder.getMaterial());
                case STAIRS -> stairsBlock((StairBlock) block, blockStateHolder.getMaterial());
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, blockStateHolder.getMaterial());
            }
        }
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

    public void addState(Block block, DataBlockState type, Block... material){
        this.blockStates.add(new Couple<>(block,new BlockStateHolder(type,material)));
    }

    public void addState(Supplier<Block> blockSupplier, DataBlockState type, Block... material){
        this.blockStates.add(new Couple<>(blockSupplier.get(),new BlockStateHolder(type,material)));
    }

    public void addState(Block block, DataBlockState type, ResourceLocation... material){
        this.blockStates.add(new Couple<>(block,new BlockStateHolder(type,material)));
    }

    public void addState(Supplier<Block> blockSupplier, DataBlockState type, ResourceLocation... material){
        this.blockStates.add(new Couple<>(blockSupplier.get(),new BlockStateHolder(type,material)));
    }

    public void createMirroredBlock(Block block){
        simpleBlock(block,new ConfiguredModel(cubeAll(block)),new ConfiguredModel(createCubeMirroredAll(block)),new ConfiguredModel(cubeAll(block),0,180,false),new ConfiguredModel(createCubeMirroredAll(block),0,180,false));
    }

    public void createDoorBlock(Block block){
        ResourceLocation top = new ResourceLocation(ResourceUtils.getBlockTexture(block)+"_top");
        ResourceLocation bottom = new ResourceLocation(ResourceUtils.getBlockTexture(block)+"_bottom");
        doorBlockWithRenderType((DoorBlock) block,bottom,top,"cutout");
    }

    public void createWallBlock(WallBlock block, BlockStateHolder stateHolder,ResourceLocation texture) {
        wallBlock(block,texture);
        createWallInventory(block,stateHolder);
    }

    public void createTrapdoorBlock(Block block){
        trapdoorBlockWithRenderType((TrapDoorBlock) block,ResourceUtils.getBlockTexture(block),true,"cutout");
    }

    public void createEmptyBlock(Block block){
        simpleBlock(block,new ConfiguredModel(createEmpty(block)));
    }

    public void createChiseledBlock(Block block, ResourceLocation side, ResourceLocation end) {
        simpleBlock(block, new ConfiguredModel(models().cubeColumn(getBlockId(block), side, end)));
    }

    public BlockModelBuilder createCubeMirroredAll(Block block) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString()+"_mirrored")
                .parent(createModelFile("block/cube_mirrored_all"))
                .texture("all", getBlockTexture(location));
    }

    public BlockModelBuilder createEmpty(Block block) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString())
                .texture("particle", getBlockTexture(location));
    }


    public BlockModelBuilder createWallInventory(Block block,BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(block);
        return models().getBuilder(location.toString()+"_inventory")
                .parent(createModelFile("block/wall_inventory"))
                .texture("wall", stateHolder.getMaterial());
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    public BlockStateHolder createHolder(DataBlockState type){
        return new BlockStateHolder(type);
    }

    public BlockStateHolder createHolder(DataBlockState type,ResourceLocation... materials){
        return new BlockStateHolder(type,materials);
    }

    public class BlockStateHolder{

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
