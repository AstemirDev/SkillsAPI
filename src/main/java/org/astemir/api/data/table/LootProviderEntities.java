package org.astemir.api.data.table;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;

public abstract class LootProviderEntities extends EntityLoot implements ILootProvider<EntityType<?>>{

    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return getKnown();
    }
}
