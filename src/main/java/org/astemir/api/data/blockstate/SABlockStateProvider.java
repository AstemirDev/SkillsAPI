package org.astemir.api.data.blockstate;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.IProvider;
import org.astemir.api.data.model.DataBlockModelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


import static org.astemir.api.utils.ResourceUtils.*;


public class SABlockStateProvider extends BlockStateProvider implements IProvider {

    private List<DataBlockStateHolder> blockStates = new ArrayList<>();

    public SABlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper) {
        super(gen, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (DataBlockStateHolder blockStateHolder : blockStates) {
            Block block = blockStateHolder.getBlock();
            switch (blockStateHolder.getType()){
                case EMPTY -> createEmptyBlock(blockStateHolder);
                case CROSS -> simpleBlock(block, DataBlockModelUtils.createCrossBlock(this,blockStateHolder));
                case LOG -> logBlock((RotatedPillarBlock) block);
                case DOOR -> createDoorBlock(blockStateHolder);
                case LEAVES -> simpleBlock(block, DataBlockModelUtils.createLeavesBlock(this,blockStateHolder));
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
                case STAIRS -> stairsBlock((StairBlock) blockStateHolder.getBlock(),blockStateHolder.getMaterial("texture"));
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block,blockStateHolder.getMaterial("texture"));
                case CUSTOM -> blockStateHolder.customBuild();
            }
        }
    }

    public void createMirroredBlock(DataBlockStateHolder stateHolder){
        simpleBlock(stateHolder.getBlock(),new ConfiguredModel(cubeAll(stateHolder.getBlock())),new ConfiguredModel(DataBlockModelUtils.createCubeMirroredAll(this,stateHolder)),new ConfiguredModel(cubeAll(stateHolder.getBlock()),0,180,false),new ConfiguredModel(DataBlockModelUtils.createCubeMirroredAll(this,stateHolder),0,180,false));
    }

    public void createDoorBlock(DataBlockStateHolder stateHolder){
        doorBlockWithRenderType((DoorBlock) stateHolder.getBlock(),stateHolder.getMaterial("bottom"),stateHolder.getMaterial("top"),"cutout");
    }

    public void createButtonBlock(DataBlockStateHolder stateHolder){
        buttonBlock((ButtonBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        DataBlockModelUtils.createButtonInventory(this,stateHolder);
    }

    public void createFenceBlock(DataBlockStateHolder stateHolder){
        fenceBlock((FenceBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        DataBlockModelUtils.createFenceInventory(this,stateHolder);
    }

    public void createWallBlock(DataBlockStateHolder stateHolder) {
        wallBlock((WallBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"));
        DataBlockModelUtils.createWallInventory(this,stateHolder);
    }

    public void createTrapdoorBlock(DataBlockStateHolder stateHolder){
        trapdoorBlockWithRenderType((TrapDoorBlock) stateHolder.getBlock(),stateHolder.getMaterial("texture"),true,"cutout");
    }

    public void createEmptyBlock(DataBlockStateHolder stateHolder){
        simpleBlock(stateHolder.getBlock(),new ConfiguredModel(DataBlockModelUtils.createEmpty(this,stateHolder)));
    }

    public void createChiseledBlock(DataBlockStateHolder stateHolder) {
        simpleBlock(stateHolder.getBlock(), new ConfiguredModel(models().cubeColumn(getBlockId(stateHolder.getBlock()), stateHolder.getMaterial("side"), stateHolder.getMaterial("end"))));
    }


    public DataBlockStateHolder addState(DataBlockStateHolder customHolder){
        this.blockStates.add(customHolder);
        return customHolder;
    }

    public DataBlockStateHolder addState(Block block, DataBlockState type){
        DataBlockStateHolder holder = new DataBlockStateHolder(block,type);
        ResourceLocation texture = getBlockTexture(block);
        holder.material("texture",texture);
        holder.material("particle",texture);
        holder.material("side",texture);
        holder.material("front",texture);
        holder.material("top",new ResourceLocation(texture.getNamespace(),texture.getPath()+"_top"));
        holder.material("bottom",new ResourceLocation(texture.getNamespace(),texture.getPath()+"_bottom"));
        holder.material("double",texture);
        this.blockStates.add(holder);
        return holder;
    }

    public DataBlockStateHolder addState(Supplier<Block> block, DataBlockState type){
        return addState(block.get(),type);
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    public DataBlockStateHolder createHolder(Block block, DataBlockState type){
        return new DataBlockStateHolder(block,type);
    }
}
