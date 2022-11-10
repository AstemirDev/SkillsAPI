package org.astemir.api.data.table;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;


public abstract class LootProviderBlocks extends BlockLoot implements ILootProvider<Block>{

    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return getKnown();
    }
}
