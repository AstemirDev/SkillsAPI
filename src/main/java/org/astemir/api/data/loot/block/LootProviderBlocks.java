package org.astemir.api.data.loot.block;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.DeferredRegister;
import org.astemir.api.data.loot.ILootProvider;

import java.util.ArrayList;
import java.util.List;

import static org.astemir.api.data.loot.block.DataBlockDropType.*;


public class LootProviderBlocks extends BlockLoot implements ILootProvider<Block> {


    private List<DataBlockDrop> blocksDrops = new ArrayList<>();
    private DeferredRegister<Block> registry;

    public LootProviderBlocks(DeferredRegister<Block> registry) {
        this.registry = registry;
    }

    @Override
    public void addLoot() {
        for (DataBlockDrop blocksDrop : blocksDrops) {
            Block block = blocksDrop.getBlock();
            switch (blocksDrop.getType()) {
                case SELF -> dropSelf(block);
                case SILK_TOUCH -> dropWhenSilkTouch(block);
                case OTHER_BLOCK -> dropOther(block, blocksDrop.getDrop("other"));
                case OTHER_BLOCK_SILK_TOUCH -> otherWhenSilkTouch(block, (Block) blocksDrop.getDrop("other"));
                case ORE -> oreDrops(block, (Item) blocksDrop.getDrop("other"), blocksDrop.getCount().getMinValue(), blocksDrop.getCount().getMaxValue());
                case FLOWER_POT -> dropPottedContents(block);
                case SLAB -> add(block, createSlabItemTable(block));
                case DOOR -> add(block, createDoorTable(block));
                case ITEM -> add(block, createSingleItemTable(blocksDrop.getDrop("item"), UniformGenerator.between(blocksDrop.getCount().getMinValue(), blocksDrop.getCount().getMaxValue())));
                case CROPS -> cropDrops(block,((DataCropsDrop)blocksDrop).getCrops().asItem(), ((DataCropsDrop) blocksDrop).getSeeds().asItem(), ((DataCropsDrop) blocksDrop).getMinAge(), ((DataCropsDrop) blocksDrop).getAgeProperty());
                case LEAVES -> add(block, createLeavesDrops(block, (Block) blocksDrop.getDrop("other"), new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F}));
                case STEM -> add(block, createStemDrops(block, blocksDrop.getDrop("other").asItem()));
                case SHEARS -> add(block, createShearsOnlyDrop(block.asItem()));
                case OTHER_SHEARS -> add(block, createShearsOnlyDrop(blocksDrop.getDrop("other")));
                case GRASS_BLOCK -> add(block, createSingleItemTableWithSilkTouch(block, blocksDrop.getDrop("other")));
                case EMPTY -> add(block, noDrop());
                case CUSTOM -> add(block, blocksDrop.customBuild());
            }
        }
    }

    public void addDrop(DataBlockDrop drop){
        blocksDrops.add(drop);
    }

    public DataBlockDrop createDropSelf(Block block){
        return new DataBlockDrop(block, DataBlockDropType.SELF);
    }

    public DataBlockDrop createCustom(Block block){
        return new DataBlockDrop(block, DataBlockDropType.CUSTOM);
    }

    public DataBlockDrop createDropOther(Block block, ItemLike other){
        return new DataBlockDrop(block, OTHER_BLOCK).otherDrop("other",other);
    }

    public DataBlockDrop createDropSlab(Block block){
        return new DataBlockDrop(block, DataBlockDropType.SLAB);
    }

    public DataBlockDrop createDropDoor(Block block){
        return new DataBlockDrop(block, DataBlockDropType.DOOR);
    }

    public DataBlockDrop createDropSilkTouch(Block block){
        return new DataBlockDrop(block, SILK_TOUCH);
    }

    public DataBlockDrop createDropSilkTouchOther(Block block, ItemLike other){
        return new DataBlockDrop(block, OTHER_BLOCK_SILK_TOUCH).otherDrop("other",other);
    }

    public DataBlockDrop createDropOre(Block block, ItemLike drop){
        return new DataBlockDrop(block, DataBlockDropType.ORE).otherDrop("other",drop);
    }

    public DataBlockDrop createDropOtherShears(Block block, ItemLike other){
        return new DataBlockDrop(block, DataBlockDropType.OTHER_SHEARS).otherDrop("other",other);
    }

    public DataBlockDrop createDropLeaves(Block block, Block sapling){
        return new DataBlockDrop(block, DataBlockDropType.LEAVES).otherDrop("sapling",sapling);
    }


    protected void oreDrops(Block block,Item drop,int minCount,int maxCount) {
        this.add(block,createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
    }

    protected void cropDrops(Block block, Item drop, Item seeds, int minAge, IntegerProperty property) {
        LootItemCondition.Builder conditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, minAge));
        this.add(block, createCropDrops(block, drop, seeds, conditionBuilder));
    }


    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return getKnown();
    }


    @Override
    public DeferredRegister<Block> getRegistry() {
        return registry;
    }
}
