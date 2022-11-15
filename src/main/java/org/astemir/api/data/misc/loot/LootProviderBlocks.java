package org.astemir.api.data.misc.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;


public abstract class LootProviderBlocks extends BlockLoot implements ILootProvider<Block>{

    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return getKnown();
    }


    protected void oreDrops(Block block,Item drop,int minCount,int maxCount) {
        this.add(block,createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
    }


    protected void cropDrops(Block block, Item drop, Item seeds, int minAge, IntegerProperty property) {
        LootItemCondition.Builder conditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, minAge));
        this.add(block, createCropDrops(block, drop, seeds, conditionBuilder));
    }

    public static class CropsDrop extends BlockDrop{

        private ItemLike seeds;
        private int minAge = 1;
        private IntegerProperty ageProperty;

        public CropsDrop(ItemLike seeds,int minAge,IntegerProperty ageProperty) {
            super(BlockDropType.CROPS);
            this.minAge = minAge;
            this.seeds = seeds;
            this.ageProperty = ageProperty;
        }

        public int getMinAge() {
            return minAge;
        }

        public ItemLike getSeeds() {
            return seeds;
        }

        public IntegerProperty getAgeProperty() {
            return ageProperty;
        }
    }

    public static class BlockDrop{

        private BlockDropType type;
        private ItemLike otherDrop;
        private UniformInt count;

        public BlockDrop(BlockDropType dropType) {
            this.type = dropType;
            this.count = UniformInt.of(1,1);
        }

        public BlockDrop otherDrop(ItemLike otherDrop) {
            this.otherDrop = otherDrop;
            return this;
        }

        public BlockDrop count(int minCount,int maxCount) {
            this.count = UniformInt.of(minCount,maxCount);
            return this;
        }

        public UniformInt getCount() {
            return count;
        }

        public BlockDropType getType() {
            return type;
        }

        public ItemLike getOtherDrop() {
            return otherDrop;
        }
    }


    public enum BlockDropType{
        SELF,SILK_TOUCH,SHEARS,ITEM,OTHER_SHEARS,OTHER_BLOCK,OTHER_BLOCK_SILK_TOUCH,ORE,FLOWER_POT,SLAB,DOOR,CROPS,LEAVES,STEM,
    }
}
