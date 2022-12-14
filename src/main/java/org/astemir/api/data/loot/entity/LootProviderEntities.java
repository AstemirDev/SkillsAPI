package org.astemir.api.data.loot.entity;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import org.astemir.api.data.loot.ILootProvider;

import java.util.ArrayList;
import java.util.List;

public class LootProviderEntities extends EntityLoot implements ILootProvider<EntityType<?>> {

    private List<DataMobDrop> mobDrops = new ArrayList<>();
    private DeferredRegister<EntityType<?>> registry;

    public LootProviderEntities(DeferredRegister<EntityType<?>> registry) {
        this.registry = registry;
    }

    @Override
    public void addLoot() {
        for (DataMobDrop mobDrop : mobDrops) {
            add(mobDrop.getType(), mobDrop.build());
        }
    }

    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return getKnown();
    }


    @Override
    public DeferredRegister<EntityType<?>> getRegistry() {
        return registry;
    }
}
