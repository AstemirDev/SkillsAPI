package org.astemir.api;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.astemir.api.client.TESRModels;
import org.astemir.api.client.render.AdvancedItemRenderer;
import org.astemir.api.common.GlobalTaskHandler;
import org.astemir.api.common.PlayerScreenShaker;
import org.astemir.api.common.WorldEventHandler;
import org.astemir.api.common.entity.EntityRegisterEvents;
import org.astemir.api.common.event.CommandsRegisterEvents;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.common.event.MiscAPIEvents;
import org.astemir.api.network.messages.*;
import org.astemir.api.utils.NetworkUtils;

public abstract class SkillsAPI {

    public static final Logger LOGGER = LogManager.getLogger();

    private SimpleChannel network;

    public final String MOD_ID;

    public static SkillsAPI API;

    public static WorldEventHandler WORLD_EVENTS;

    public static GlobalTaskHandler GLOBAL_TASK_HANDLER;

    public static PlayerScreenShaker SCREEN_SHAKER = new PlayerScreenShaker();

    public SkillsAPI(String modId) {
        API = this;
        MOD_ID = modId;
    }

    protected void defaultInit(){
        WORLD_EVENTS = new WorldEventHandler();
        GLOBAL_TASK_HANDLER = new GlobalTaskHandler();
        EventManager.registerForgeEventInstance(this);
        EventManager.registerFMLEvent(this::clientSetup);
        EventManager.registerFMLEvent(this::commonSetup);
        EventManager.registerFMLEvent(this::enqueueIMC);
        EventManager.registerFMLEvent(this::processIMC);
        EventManager.registerForgeEventClass(MiscAPIEvents.class);
        EventManager.registerForgeEventClass(CommandsRegisterEvents.class);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () ->{
            EventManager.registerFMLEvent(AdvancedItemRenderer::onRegisterReloadListener);
        });
        EventManager.registerFMLEvent(EntityRegisterEvents::onAttributesLoad);
    }

    protected void defaultCommon(){
        network = NetworkUtils.createNetworkChannel(MOD_ID,"api_network_channel");
        network.registerMessage(1, EntityEventMessage.class, EntityEventMessage::encode, EntityEventMessage::decode, new EntityEventMessage.Handler());
        network.registerMessage(2, ActionControllerMessage.class, ActionControllerMessage::encode, ActionControllerMessage::decode, new ActionControllerMessage.Handler());
        network.registerMessage(3, BlockEventMessage.class, BlockEventMessage::encode, BlockEventMessage::decode, new BlockEventMessage.Handler());
        network.registerMessage(4, BlockClickMessage.class, BlockClickMessage::encode, BlockClickMessage::decode, new BlockClickMessage.Handler());
        network.registerMessage(5, ScreenShakeMessage.class, ScreenShakeMessage::encode, ScreenShakeMessage::decode, new ScreenShakeMessage.Handler());
        network.registerMessage(6, AnimationMessage.class,AnimationMessage::encode,AnimationMessage::decode,new AnimationMessage.Handler());
        network.registerMessage(7, ClientAnimationSyncMessage.class,ClientAnimationSyncMessage::encode,ClientAnimationSyncMessage::decode,new ClientAnimationSyncMessage.Handler());
    }

    public void initializeAPI(){
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () ->{
            onUnsafeClientSetup();
            EventManager.registerFMLEvent(TESRModels::onModelRegistryInit);
            EventManager.registerFMLEvent(TESRModels::onModelBakeEvent);
        });
    }


    private void commonSetup(FMLCommonSetupEvent event){
        onCommonSetup(event);
    }

    private void clientSetup(FMLClientSetupEvent event){
        onClientSetup(event);
    }

    private void enqueueIMC(InterModEnqueueEvent event){
        onEnqueueIMC(event);
    }

    private void processIMC(InterModProcessEvent event){
        onProcessIMC(event);
    }

    private void serverStart(ServerStartingEvent event){onServerStarting(event);}


    protected void onClientSetup(FMLClientSetupEvent event){};

    protected void onCommonSetup(FMLCommonSetupEvent event){};

    protected void onServerStarting(ServerStartingEvent event){};

    protected void onEnqueueIMC(InterModEnqueueEvent event){};

    protected void onProcessIMC(InterModProcessEvent event){};

    protected void onUnsafeClientSetup(){};

    public SimpleChannel getAPINetwork() {
        return network;
    }
}
