package org.astemir.api.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.common.entity.EntityProperties;

public abstract class RegisterHelper {

    public static final <T extends Entity> RegistryObject<EntityType<T>> register(DeferredRegister<EntityType<?>> registry,String modId,String id, EntityProperties<T> properties) {
        return registry.register(id,()->properties.build(modId,id));
    }

    public static final RegistryObject<SoundEvent> register(DeferredRegister<SoundEvent> registry,String modId, String id) {
        return registry.register(id, () -> new SoundEvent(new ResourceLocation(modId,id)));
    }
}
