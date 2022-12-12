package org.astemir.api.data.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.DeferredRegister;
import org.astemir.api.data.misc.loot.LootProviderBlocks;
import org.astemir.api.data.misc.loot.LootProviderEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SALootTableProvider extends LootTableProvider implements IProvider{

    private List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootProviders =  new ArrayList<>();

    private Map<EntityType, LootProviderEntities.MobLoot> mobsDrops = new HashMap<>();
    private Map<Block, LootProviderBlocks.BlockDrop> blocksDrops = new HashMap<>();

    public SALootTableProvider(DataGenerator p_124437_) {
        super(p_124437_);
    }


    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return lootProviders;
    }

    public LootProviderEntities.MobLoot mobLoot(){
        return new LootProviderEntities.MobLoot();
    }

    public LootProviderBlocks.BlockDrop dropSelf(){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.SELF);
    }

    public LootProviderBlocks.BlockDrop custom(){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.CUSTOM);
    }

    public LootProviderBlocks.BlockDrop dropOther(ItemLike other){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.OTHER_BLOCK).otherDrop(other);
    }

    public LootProviderBlocks.BlockDrop dropSlab(){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.SLAB);
    }

    public LootProviderBlocks.BlockDrop dropDoor(){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.DOOR);
    }

    public LootProviderBlocks.BlockDrop dropSilkTouch(){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.SILK_TOUCH);
    }

    public LootProviderBlocks.BlockDrop dropSilkTouchOther(ItemLike other){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.OTHER_BLOCK_SILK_TOUCH).otherDrop(other);
    }

    public LootProviderBlocks.BlockDrop dropOre(ItemLike drop,int minCount,int maxCount){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.ORE).otherDrop(drop).count(minCount,maxCount);
    }

    public LootProviderBlocks.BlockDrop dropOtherShears(ItemLike other){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.OTHER_SHEARS).otherDrop(other);
    }

    public LootProviderBlocks.BlockDrop leaves(Block sapling){
        return new LootProviderBlocks.BlockDrop(LootProviderBlocks.BlockDropType.LEAVES).otherDrop(sapling);
    }



    public void addEntityDrops(Supplier<EntityType> entityType,LootProviderEntities.MobLoot loot){
        this.mobsDrops.put(entityType.get(),loot);
    }

    public void addBlockDrops(Supplier<Block> block,LootProviderBlocks.BlockDrop drop) {
        this.blocksDrops.put(block.get(), drop);
    }

    public void addEntityDrops(EntityType entityType,LootProviderEntities.MobLoot loot){
        this.mobsDrops.put(entityType,loot);
    }

    public void addBlockDrops(Block block,LootProviderBlocks.BlockDrop drop){
        this.blocksDrops.put(block,drop);
    }

    public void loadEntityDrops(DeferredRegister<EntityType<?>> entities){
        addLootProvider(()->new LootProviderEntities() {
            @Override
            public void addLoot() {
                mobsDrops.forEach((type,loot)->{
                    add(type,loot.build());
                });
            }

            @Override
            public DeferredRegister<EntityType<?>> getRegistry() {
                return entities;
            }
        }, LootContextParamSets.ENTITY);
    }

    public void loadBlock3Drops(DeferredRegister<Block> blocks){
        addLootProvider(()->new LootProviderBlocks() {
            @Override
            public void addLoot() {
                blocksDrops.forEach((block,loot)->{
                    switch (loot.getType()){
                        case SELF ->dropSelf(block);
                        case SILK_TOUCH -> dropWhenSilkTouch(block);
                        case OTHER_BLOCK -> dropOther(block,loot.getOtherDrop());
                        case OTHER_BLOCK_SILK_TOUCH -> otherWhenSilkTouch(block, (Block) loot.getOtherDrop());
                        case ORE -> oreDrops(block,loot.getOtherDrop().asItem(),loot.getCount().getMinValue(),loot.getCount().getMaxValue());
                        case FLOWER_POT -> dropPottedContents(block);
                        case SLAB -> add(block,createSlabItemTable(block));
                        case DOOR -> add(block,createDoorTable(block));
                        case ITEM -> add(block,createSingleItemTable(loot.getOtherDrop(), UniformGenerator.between(loot.getCount().getMinValue(),loot.getCount().getMaxValue())));
                        case CROPS -> cropDrops(block,loot.getOtherDrop().asItem(),((CropsDrop)loot).getSeeds().asItem(),((CropsDrop)loot).getMinAge(),((CropsDrop)loot).getAgeProperty());
                        case LEAVES -> add(block,createLeavesDrops(block,(Block)loot.getOtherDrop(),new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F}));
                        case STEM -> add(block,createStemDrops(block,loot.getOtherDrop().asItem()));
                        case SHEARS -> add(block,createShearsOnlyDrop(block.asItem()));
                        case OTHER_SHEARS -> add(block,createShearsOnlyDrop(loot.getOtherDrop()));
                        case GRASS_BLOCK -> add(block,createSingleItemTableWithSilkTouch(block,loot.getOtherDrop()));
                        case CUSTOM -> add(block,loot.customBuild());
                    }
                });
            }

            @Override
            public DeferredRegister<Block> getRegistry() {
                return blocks;
            }
        }, LootContextParamSets.ENTITY);
    }

    public void addLootProvider(Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> provider,LootContextParamSet paramSet){
        lootProviders.add(Pair.of(provider,paramSet));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
