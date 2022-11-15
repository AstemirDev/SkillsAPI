package org.astemir.api.data.misc.loot;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;

public interface ILootProvider<T> {

    void addLoot();

    default Iterable<T> getKnown(){
        Iterable<T> types = new HashSet<>();
        for (RegistryObject<T> entry : getRegistry().getEntries()) {
            ((HashSet)types).add(entry.get());
        }
        return types;
    }

    DeferredRegister<T> getRegistry();
}
