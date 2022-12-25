package org.astemir.api.common.register;


import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

public interface IRegistry<T> {

    static <B> DeferredRegister<B> create(String id,IForgeRegistry<B> reg){
        return DeferredRegister.create(reg,id);
    }

    static <B> void register(DeferredRegister<B> register){
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
