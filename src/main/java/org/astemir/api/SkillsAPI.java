package org.astemir.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astemir.api.client.TESRModels;
import org.astemir.api.client.render.AdvancedItemRenderer;
import org.astemir.api.client.ClientAPIEvents;
import org.astemir.api.common.GlobalTaskHandler;
import org.astemir.api.common.PlayerScreenShaker;
import org.astemir.api.common.WorldEventHandler;
import org.astemir.api.common.entity.EntityRegisterEvents;
import org.astemir.api.common.event.CommandsRegisterEvents;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.common.event.MiscAPIEvents;
import org.astemir.api.network.messages.*;

public abstract class SkillsAPI {

    public static final Logger LOGGER = LogManager.getLogger();

    private SimpleChannel NETWORK;

    public final String MOD_ID;

    public static SkillsAPI API;

    public static WorldEventHandler WORLD_EVENTS;

    public static GlobalTaskHandler GLOBAL_TASK_HANDLER;

    public static PlayerScreenShaker SCREEN_SHAKER = new PlayerScreenShaker();

    public SkillsAPI(String modId) {
        API = this;
        MOD_ID = modId;
        WORLD_EVENTS = new WorldEventHandler();
        GLOBAL_TASK_HANDLER = new GlobalTaskHandler();
        EventManager.registerForgeEventInstance(this);
        EventManager.registerFMLEvent(this::commonSetup);
        EventManager.registerFMLEvent(this::clientSetup);
        EventManager.registerFMLEvent(this::enqueueIMC);
        EventManager.registerFMLEvent(this::processIMC);
        EventManager.registerFMLEvent(EntityRegisterEvents::onAttributesLoad);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () ->{
            EventManager.registerFMLEvent(AdvancedItemRenderer::onRegisterReloadListener);
            EventManager.registerFMLEvent(TESRModels::onModelRegistryInit);
            EventManager.registerFMLEvent(TESRModels::onModelBakeEvent);
        });
    }

    protected void init(){
        EventManager.registerForgeEventClass(MiscAPIEvents.class);
        EventManager.registerForgeEventClass(CommandsRegisterEvents.class);
    }


    public SimpleChannel createNetworkChannel(String name){
        String networkVersion = "1.3";
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID,name), () -> networkVersion, (v) -> v.equals(networkVersion), (v) -> v.equals(networkVersion));
        return channel;
    }

    private void commonSetup(FMLCommonSetupEvent event){
        NETWORK = createNetworkChannel("api_network_channel");
        NETWORK.registerMessage(1, EntityEventMessage.class, EntityEventMessage::encode, EntityEventMessage::decode, new EntityEventMessage.Handler());
        NETWORK.registerMessage(2, ActionControllerMessage.class, ActionControllerMessage::encode, ActionControllerMessage::decode, new ActionControllerMessage.Handler());
        NETWORK.registerMessage(3, BlockEventMessage.class, BlockEventMessage::encode, BlockEventMessage::decode, new BlockEventMessage.Handler());
        NETWORK.registerMessage(4, BlockClickMessage.class, BlockClickMessage::encode, BlockClickMessage::decode, new BlockClickMessage.Handler());
        NETWORK.registerMessage(5, ScreenShakeMessage.class, ScreenShakeMessage::encode, ScreenShakeMessage::decode, new ScreenShakeMessage.Handler());
        NETWORK.registerMessage(6, AnimationMessage.class,AnimationMessage::encode,AnimationMessage::decode,new AnimationMessage.Handler());
        NETWORK.registerMessage(7, ClientAnimationSyncMessage.class,ClientAnimationSyncMessage::encode,ClientAnimationSyncMessage::decode,new ClientAnimationSyncMessage.Handler());
        onCommonSetup(event);
    }

    private void clientSetup(FMLClientSetupEvent event){
        EventManager.registerForgeEventClass(ClientAPIEvents.class);
        onClientSetup(event);
    }

    private void enqueueIMC(InterModEnqueueEvent event){
        onEnqueueIMC(event);
    }

    private void processIMC(InterModProcessEvent event){
        onProcessIMC(event);
    }

    protected abstract void onClientSetup(FMLClientSetupEvent event);

    protected abstract void onCommonSetup(FMLCommonSetupEvent event);

    protected abstract void onServerStarting(ServerStartingEvent event);

    protected abstract void onEnqueueIMC(InterModEnqueueEvent event);

    protected abstract void onProcessIMC(InterModProcessEvent event);

    public SimpleChannel getAPINetwork() {
        return NETWORK;
    }
}
