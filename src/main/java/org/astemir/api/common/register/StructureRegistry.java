package org.astemir.api.common.register;


import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StructureRegistry implements IRegistry<Structure>{

    public static HolderSet<Biome> biomes(ResourceKey<Biome>... biomes){
        List<Holder<Biome>> holders = new ArrayList<>();
        for (ResourceKey<Biome> key : biomes) {
            Optional<Holder<Biome>> optional = ForgeRegistries.BIOMES.getHolder(key);
            if (optional.isPresent()) {
                Holder<Biome> holder = optional.get();
                holders.add(holder);
            }
        }
        return HolderSet.direct(holders);
    }
}
