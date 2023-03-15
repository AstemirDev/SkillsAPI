package org.astemir.api.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.common.event.BiomeModifyEvent;
import org.astemir.example.SkillsAPI;

public class SkillsAPIBiomeModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(SkillsAPI.MOD_ID, "skills_api_biome_modifier"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SkillsAPI.MOD_ID);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            BiomeModifyEvent event = new BiomeModifyEvent(biome,builder);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public static void register(IEventBus eventBus){
        DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SkillsAPI.MOD_ID);
        biomeModifiers.register(eventBus);
        biomeModifiers.register("skills_api_biome_modifier", SkillsAPIBiomeModifier::makeCodec);
    }

    public Codec<? extends BiomeModifier> codec() {
        return (Codec)SERIALIZER.get();
    }

    public static Codec<SkillsAPIBiomeModifier> makeCodec() {
        return Codec.unit(SkillsAPIBiomeModifier::new);
    }

}