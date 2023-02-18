package org.astemir.api.data.blockstate;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.IProvider;
import org.astemir.api.data.model.BlockModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


import static org.astemir.api.data.model.BlockModelUtils.createTintedCross;
import static org.astemir.api.utils.ResourceUtils.*;


public class SkillsBlockStateProvider extends BlockStateProvider implements IProvider {

    private List<BlockStateHolder> blockStates = new ArrayList<>();

    public SkillsBlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper) {
        super(gen, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockStateHolder blockStateHolder : blockStates) {
            Block block = blockStateHolder.getBlock();
            switch (blockStateHolder.getType()){
                case EMPTY -> createEmptyBlock(blockStateHolder);
                case CROSS -> simpleBlock(block, BlockModelUtils.createCrossBlock(this,blockStateHolder));
                case LOG -> logBlock((RotatedPillarBlock) block);
                case DOOR -> createDoorBlock(blockStateHolder);
                case LEAVES -> simpleBlock(block, BlockModelUtils.createLeavesBlock(this,blockStateHolder));
                case DEFAULT -> simpleBlock(block);
                case TRAPDOOR -> createTrapdoorBlock(blockStateHolder);
                case MIRRORED -> createMirroredBlock(blockStateHolder);
                case HORIZONTAL -> horizontalBlock(block,blockStateHolder.getMaterial("side"),blockStateHolder.getMaterial("front"),blockStateHolder.getMaterial("top"));
                case CHISELED -> createChiseledBlock(blockStateHolder);
                case COLUMN -> axisBlock((RotatedPillarBlock) block,blockStateHolder.getMaterial("side"),blockStateHolder.getMaterial("end"));
                case SLAB -> slabBlock((SlabBlock) block,blockStateHolder.getMaterial("double"),blockStateHolder.getMaterial("texture"));
                case BUTTON -> createButtonBlock(blockStateHolder);
                case FENCE -> createFenceBlock(blockStateHolder);
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block,blockStateHolder.getMaterial("texture"));
                case WALL -> createWallBlock(blockStateHolder);
                case TALL_GRASS -> createDoublePlant(blockStateHolder);
                case GRASS -> createGrass(blockStateHolder);
                case STAIRS -> stairsBlock((StairBlock) blockStateHolder.getBlock(),blockStateHolder.getMaterial("texture"));
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block,blockStateHolder.getMaterial("texture"));
                case CUSTOM -> blockStateHolder.customBuild();
            }
        }
    }

    public void createMirroredBlock(BlockStateHolder stateHolder){
        simpleBlock(stateHolder.getBlock(),new ConfiguredModel(cubeAll(stateHolder.getBlock())),new ConfiguredModel(BlockModelUtils.createCubeMirroredAll(this,stateHolder)),new ConfiguredModel(cubeAll(stateHolder.getBlock()),0,180,false),new ConfiguredModel(BlockModelUtils.createCubeMirroredAll(this,stateHolder),0,180,false));
    }

    public void createDoorBlock(BlockStateHolder stateHolder){
        doorBlockWithRenderType((DoorBlock) stateHolder.getBlock(),stateHolder.getMaterial("bottom"),stateHolder.getMaterial("top"),"cutout");
    }

    public void createButtonBlock(BlockStateHolder stateHolder){
        buttonBlock((ButtonBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        BlockModelUtils.createButtonInventory(this,stateHolder);
    }

    public void createFenceBlock(BlockStateHolder stateHolder){
        fenceBlock((FenceBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        BlockModelUtils.createFenceInventory(this,stateHolder);
    }

    public void createWallBlock(BlockStateHolder stateHolder) {
        wallBlock((WallBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        BlockModelUtils.createWallInventory(this,stateHolder);
    }

    public void createTrapdoorBlock(BlockStateHolder stateHolder){
        trapdoorBlockWithRenderType((TrapDoorBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"),true,"cutout");
    }

    public void createEmptyBlock(BlockStateHolder stateHolder){
        simpleBlock(stateHolder.getBlock(),new ConfiguredModel(BlockModelUtils.createEmpty(this,stateHolder)));
    }

    public void createChiseledBlock(BlockStateHolder stateHolder) {
        simpleBlock(stateHolder.getBlock(), new ConfiguredModel(models().cubeColumn(getBlockId(stateHolder.getBlock()), stateHolder.getMaterial("side"), stateHolder.getMaterial("end"))));
    }

    public void createDoublePlant(BlockStateHolder stateHolder){
        getVariantBuilder(stateHolder.getBlock()).
                partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).addModels(new ConfiguredModel(createTintedCross(this,stateHolder,"bottom"))).
                partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).addModels(new ConfiguredModel(createTintedCross(this,stateHolder,"top")));
    }

    public void createGrass(BlockStateHolder stateHolder){
        simpleBlock(stateHolder.getBlock(),createTintedCross(this, stateHolder));
    }



    public BlockStateHolder addState(BlockStateHolder customHolder){
        this.blockStates.add(customHolder);
        return customHolder;
    }


    public BlockStateHolder addState(Block block, BlockStateType type){
        BlockStateHolder holder = new BlockStateHolder(block,type);
        holder.loadDefaultTexturePaths();
        this.blockStates.add(holder);
        return holder;
    }

    public BlockStateHolder addState(Supplier<Block> block, BlockStateType type){
        return addState(block.get(),type);
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    public BlockStateHolder createHolder(Block block, BlockStateType type){
        return new BlockStateHolder(block,type);
    }
}
