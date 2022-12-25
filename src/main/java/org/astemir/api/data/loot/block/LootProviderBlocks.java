package org.astemir.api.data.loot.block;

import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
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
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.astemir.api.data.loot.block.BlockDropType.*;


public class LootProviderBlocks extends BlockLoot implements ILootProvider<Block> {


    private List<BlockDrop> blocksDrops = new ArrayList<>();
    private DeferredRegister<Block> registry;

    public LootProviderBlocks(DeferredRegister<Block> registry) {
        this.registry = registry;
    }

    @Override
    public void addLoot() {
        for (BlockDrop blocksDrop : blocksDrops) {
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
                case CROPS -> cropDrops(block,((CropsDrop)blocksDrop).getCrops().asItem(), ((CropsDrop) blocksDrop).getSeeds().asItem(), ((CropsDrop) blocksDrop).getMinAge(), ((CropsDrop) blocksDrop).getAgeProperty());
                case LEAVES -> add(block, createLeavesDrops(block, (Block) blocksDrop.getDrop("other"), new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F}));
                case STEM -> add(block, createStemDrops(block, blocksDrop.getDrop("other").asItem()));
                case SHEARS -> add(block, createShearsOnlyDrop(block.asItem()));
                case OTHER_SHEARS -> add(block, createShearsOnlyDrop(blocksDrop.getDrop("other")));
                case GRASS_BLOCK -> add(block, createSingleItemTableWithSilkTouch(block, blocksDrop.getDrop("other")));
                case EMPTY -> add(block, noDrop());
                case CUSTOM -> add(block, blocksDrop.customBuild(this));
            }
        }
    }

    public void addDrop(BlockDrop drop){
        blocksDrops.add(drop);
    }

    public BlockDrop createDropSelf(Block block){
        return new BlockDrop(block, BlockDropType.SELF);
    }

    public BlockDrop createCustom(Block block){
        return new BlockDrop(block, BlockDropType.CUSTOM);
    }

    public BlockDrop createDropOther(Block block, ItemLike other){
        return new BlockDrop(block, OTHER_BLOCK).otherDrop("other",other);
    }

    public BlockDrop createDropSlab(Block block){
        return new BlockDrop(block, BlockDropType.SLAB);
    }

    public BlockDrop createDropDoor(Block block){
        return new BlockDrop(block, BlockDropType.DOOR);
    }

    public BlockDrop createDropSilkTouch(Block block){
        return new BlockDrop(block, SILK_TOUCH);
    }

    public BlockDrop createDropSilkTouchOther(Block block, ItemLike other){
        return new BlockDrop(block, OTHER_BLOCK_SILK_TOUCH).otherDrop("other",other);
    }

    public BlockDrop createDropOre(Block block, ItemLike drop){
        return new BlockDrop(block, BlockDropType.ORE).otherDrop("other",drop);
    }

    public BlockDrop createDropOtherShears(Block block, ItemLike other){
        return new BlockDrop(block, BlockDropType.OTHER_SHEARS).otherDrop("other",other);
    }

    public BlockDrop createDropLeaves(Block block, Block sapling){
        return new BlockDrop(block, BlockDropType.LEAVES).otherDrop("other",sapling);
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
