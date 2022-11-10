package org.astemir.api.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.SkillsAPI;
import org.astemir.api.common.entity.EntityProperties;

import java.awt.*;
import java.util.function.Supplier;


public class RegistryUtils {


    public static final RegistryObject<SoundEvent> register(DeferredRegister<SoundEvent> registry, String id) {
        return registry.register(id, () -> new SoundEvent(new ResourceLocation(SkillsAPI.API.MOD_ID,id)));
    }


    public static final <T extends Entity> RegistryObject<EntityType<T>> register(DeferredRegister<EntityType<?>> registry, String id, EntityProperties<T> properties) {
        return registry.register(id,()->properties.build(id));
    }
}
