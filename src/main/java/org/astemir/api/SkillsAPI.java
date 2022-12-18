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
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.api.common.event.EventEntityRegister;
import org.astemir.api.common.event.EventCommandRegister;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.common.event.EventMisc;
import org.astemir.api.network.messages.*;
import org.astemir.api.utils.NetworkUtils;

public abstract class SkillsAPI {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleChannel API_NETWORK = NetworkUtils.createNetworkChannel("skillsapi","api_network_channel");

    public final String MOD_ID;

    public static volatile boolean INITIALIZED = false;

    public SkillsAPI(String modId) {
        MOD_ID = modId;
    }

    protected void defaultInit(){
        initializeAPI();
        EventManager.registerForgeEventClass(EventMisc.class);
        EventManager.registerForgeEventClass(EventCommandRegister.class);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> () ->{
            EventManager.registerFMLEvent(AdvancedRendererItem::onRegisterReloadListener);
        });
        EventManager.registerFMLEvent(EventEntityRegister::onAttributesLoad);
    }



    public static synchronized void initializeNetwork(){
        if (!INITIALIZED) {
            int id = 0;
            API_NETWORK.registerMessage(id++, ClientMessageEntityEvent.class, ClientMessageEntityEvent::encode, ClientMessageEntityEvent::decode, new ClientMessageEntityEvent.Handler());
            API_NETWORK.registerMessage(id++, ClientMessageActionController.class, ClientMessageActionController::encode, ClientMessageActionController::decode, new ClientMessageActionController.Handler());
            API_NETWORK.registerMessage(id++, ClientMessageWorldPosEvent.class, ClientMessageWorldPosEvent::encode, ClientMessageWorldPosEvent::decode, new ClientMessageWorldPosEvent.Handler());
            API_NETWORK.registerMessage(id++, ServerPlayerInteractMessage.class, ServerPlayerInteractMessage::encode, ServerPlayerInteractMessage::decode, new ServerPlayerInteractMessage.Handler());
            API_NETWORK.registerMessage(id++, ClientMessagePlayerEffect.class, ClientMessagePlayerEffect::encode, ClientMessagePlayerEffect::decode, new ClientMessagePlayerEffect.Handler());
            API_NETWORK.registerMessage(id++, ClientMessageAnimation.class, ClientMessageAnimation::encode, ClientMessageAnimation::decode, new ClientMessageAnimation.Handler());
            API_NETWORK.registerMessage(id++, ServerMessageAnimationSync.class, ServerMessageAnimationSync::encode, ServerMessageAnimationSync::decode, new ServerMessageAnimationSync.Handler());
            API_NETWORK.registerMessage(id++, ServerMessageActionSync.class, ServerMessageActionSync::encode, ServerMessageActionSync::decode, new ServerMessageActionSync.Handler());
            API_NETWORK.registerMessage(id++, ServerMessageWorldPosEvent.class, ServerMessageWorldPosEvent::encode, ServerMessageWorldPosEvent::decode, new ServerMessageWorldPosEvent.Handler());
            API_NETWORK.registerMessage(id++, ServerMessageEntityEvent.class, ServerMessageEntityEvent::encode, ServerMessageEntityEvent::decode, new ServerMessageEntityEvent.Handler());
            INITIALIZED = true;
        }
    }

    public void initializeAPI(){
        EventManager.registerForgeEventInstance(this);
        EventManager.registerFMLEvent(this::clientSetup);
        EventManager.registerFMLEvent(this::commonSetup);
        EventManager.registerFMLEvent(this::enqueueIMC);
        EventManager.registerFMLEvent(this::processIMC);
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

    private void serverStart(ServerStartingEvent event){ onServerStarting(event);}


    protected void onClientSetup(FMLClientSetupEvent event){};

    protected void onCommonSetup(FMLCommonSetupEvent event){};

    protected void onServerStarting(ServerStartingEvent event){};

    protected void onEnqueueIMC(InterModEnqueueEvent event){};

    protected void onProcessIMC(InterModProcessEvent event){};

    protected void onUnsafeClientSetup(){};
}
