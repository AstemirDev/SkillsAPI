package org.astemir.api.common.event;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.Event;

public class BiomeModifyEvent extends Event {

    private Holder<Biome> biomeHolder;
    private ModifiableBiomeInfo.BiomeInfo.Builder builder;

    public BiomeModifyEvent(Holder<Biome> biomeHolder, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        this.biomeHolder = biomeHolder;
        this.builder = builder;
    }

    public Holder<Biome> getBiomeHolder() {
        return biomeHolder;
    }

    public ModifiableBiomeInfo.BiomeInfo.Builder getBuilder() {
        return builder;
    }
}
