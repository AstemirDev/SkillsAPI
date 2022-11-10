package org.astemir.api.common.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

public class EventManager {


    public static <T extends Event> void registerFMLEvent(Consumer<T> event){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event);
    }

    public static <T extends GenericEvent<? extends F>, F> void registerFMLGenericEvent(Class<F> className, Consumer<T> event){
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(className,event);
    }

    public static void registerForgeEventClass(Class<?> className){
        MinecraftForge.EVENT_BUS.register(className);
    }

    public static void registerForgeEventInstance(Object event){
        MinecraftForge.EVENT_BUS.register(event);
    }
}
