package org.astemir.api.common.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ISoundRegistry implements IRegistry<SoundEvent>{

    public static final RegistryObject<SoundEvent> register(DeferredRegister<SoundEvent> registry, String modId, String id) {
        return registry.register(id, () -> new SoundEvent(new ResourceLocation(modId,id)));
    }
}
