package org.astemir.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.awt.*;

public class BlocksUtils {

    public static final float STRENGTH_LEAVES = 0.2f;
    public static final float STRENGTH_SAND = 0.5f;
    public static final float STRENGTH_DIRT = 0.6f;
    public static final float STRENGTH_SANDSTONE = 0.8f;
    public static final float STRENGTH_STONE = 1.5f;
    public static final float STRENGTH_COBBLESTONE = 2f;
    public static final float STRENGTH_LOG = 2f;
    public static final float STRENGTH_DEEPSLATE = 4.5f;
    public static final float STRENGTH_OBSIDIAN = 50f;

    public static Block block(Material material, MaterialColor color, SoundType soundType, float strength){
        return new Block(BlockBehaviour.Properties.of(material, color).sound(soundType).strength(strength));
    }

    public static Block block(Material material,MaterialColor color,SoundType soundType,float strength,float explosionResistance){
        return new Block(BlockBehaviour.Properties.of(material, color).sound(soundType).strength(strength, explosionResistance));
    }

    public static Block block(Material material,MaterialColor color,SoundType soundType,float strength,boolean requiresCorrectTool){
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(material, color).sound(soundType).strength(strength);
        if (requiresCorrectTool){
            properties = properties.requiresCorrectToolForDrops();
        }
        return new Block(properties);
    }

    public static Block block(Material material,MaterialColor color,SoundType soundType,float strength,float explosionResistance,boolean requiresCorrectTool){
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(material, color).sound(soundType).strength(strength,explosionResistance);
        if (requiresCorrectTool){
            properties = properties.requiresCorrectToolForDrops();
        }
        return new Block(properties);
    }

    public static Block sand(MaterialColor color, Color dustColor){
        return sand(color,dustColor,SoundType.SAND);
    }

    public static Block sand(MaterialColor color,Color dustColor,SoundType soundType){
        return new SandBlock(dustColor.hashCode(), BlockBehaviour.Properties.of(Material.SAND, color).strength(STRENGTH_SAND).sound(soundType));
    }

    public static Block planks(MaterialColor color){
        return planks(color,SoundType.WOOD);
    }

    public static Block planks(MaterialColor color,SoundType soundType){
        return new Block(BlockBehaviour.Properties.of(Material.WOOD, color).strength(STRENGTH_COBBLESTONE, 3.0F).sound(soundType));
    }

    public static Block stone(MaterialColor color){
        return stone(color,SoundType.STONE);
    }

    public static Block stone(MaterialColor color,SoundType soundType){
        return new Block(BlockBehaviour.Properties.of(Material.STONE, color).sound(soundType).requiresCorrectToolForDrops().strength(STRENGTH_STONE, 6.0F));
    }

    public static Block horizontal(MaterialColor color){
        return horizontal(color,SoundType.STONE);
    }

    public static GlazedTerracottaBlock horizontal(MaterialColor color, SoundType soundType){
        return new GlazedTerracottaBlock(BlockBehaviour.Properties.of(Material.STONE, color).sound(soundType).requiresCorrectToolForDrops().strength(STRENGTH_STONE, 6.0F));
    }

    public static RotatedPillarBlock log(MaterialColor sideColor, MaterialColor topColor, SoundType soundType) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? sideColor : topColor).strength(STRENGTH_LOG).sound(soundType));
    }

    public static StairBlock stairs(Block block) {
        return new StairBlock(()->block.defaultBlockState(),BlockBehaviour.Properties.copy(block));
    }

    public static SlabBlock slab(Block block) {
        return new SlabBlock(BlockBehaviour.Properties.copy(block));
    }

    public static WallBlock wall(Block block) {
        return new WallBlock(BlockBehaviour.Properties.copy(block));
    }

    public static PressurePlateBlock pressurePlate(Material material,MaterialColor color,SoundType type) {
        return new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,BlockBehaviour.Properties.of(material,color).noOcclusion().strength(0.5f).sound(type));
    }

    public static StoneButtonBlock stoneButton(Material material,MaterialColor color,SoundType type) {
        return new StoneButtonBlock(BlockBehaviour.Properties.of(material,color).noOcclusion().strength(0.5f).sound(type));
    }

    public static WoodButtonBlock woodButton(Material material,MaterialColor color,SoundType type) {
        return new WoodButtonBlock(BlockBehaviour.Properties.of(material,color).noOcclusion().strength(0.5f).sound(type));
    }

    public static RotatedPillarBlock log(MaterialColor sideColor, MaterialColor topColor) {
        return log(sideColor,topColor,SoundType.WOOD);
    }

    public static TallGrassBlock tallGrass(){
        return new TallGrassBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ));
    }

    public static FlowerBlock flower(MobEffect effect, int effectDuration){
        return new FlowerBlock(effect, effectDuration, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    }

    public static LeavesBlock leaves(SoundType soundType) {
        return new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(STRENGTH_LEAVES).randomTicks().sound(soundType).noOcclusion().isValidSpawn(BlocksUtils::ocelotOrParrot).isSuffocating((a,b,c)->false).isViewBlocking((a,b,c)->false));
    }

    public static Boolean ocelotOrParrot(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
}
