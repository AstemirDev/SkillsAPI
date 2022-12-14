package org.astemir.api.data.loot.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

public class BlockDrop {

    private BlockDropType type;
    private Block block;
    private Map<String,ItemLike> drops = new HashMap<>();
    private UniformInt count;

    public BlockDrop(Block block, BlockDropType dropType) {
        this.block = block;
        this.type = dropType;
        this.count = UniformInt.of(1,1);
    }

    public BlockDrop otherDrop(String name, ItemLike otherDrop) {
        this.drops.put(name,otherDrop);
        return this;
    }

    public BlockDrop otherDrop(ItemLike otherDrop) {
        this.drops.put("other",otherDrop);
        return this;
    }

    public BlockDrop count(int minCount, int maxCount) {
        this.count = UniformInt.of(minCount,maxCount);
        return this;
    }

    public Block getBlock() {
        return block;
    }

    public UniformInt getCount() {
        return count;
    }

    public ItemLike getDrop(String name) {
        return drops.get(name);
    }

    public BlockDropType getType() {
        return type;
    }

    public LootTable.Builder customBuild(LootProviderBlocks providerBlocks){return null;};

}
