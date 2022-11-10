package org.astemir.api.data.table;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider {

    private List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootProviders =  new ArrayList<>();

    public ModLootTableProvider(DataGenerator p_124437_) {
        super(p_124437_);
    }


    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return lootProviders;
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
