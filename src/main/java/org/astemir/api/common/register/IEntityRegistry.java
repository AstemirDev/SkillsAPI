package org.astemir.api.common.register;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.common.entity.EntityProperties;

public class IEntityRegistry implements IRegistry<EntityType>{

    public static final <T extends Entity> RegistryObject<EntityType<T>> register(DeferredRegister<EntityType<?>> registry, String modId, String id, EntityProperties<T> properties) {
        return registry.register(id,()->properties.build(modId,id));
    }


}
