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

    private SimpleChannel network;

    public final String MOD_ID;

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

    protected void defaultCommon(){
        network = NetworkUtils.createNetworkChannel(MOD_ID,"api_network_channel");
        network.registerMessage(1, ClientMessageEntityEvent.class, ClientMessageEntityEvent::encode, ClientMessageEntityEvent::decode, new ClientMessageEntityEvent.Handler());
        network.registerMessage(2, ClientMessageActionController.class, ClientMessageActionController::encode, ClientMessageActionController::decode, new ClientMessageActionController.Handler());
        network.registerMessage(3, ClientMessageBlockEvent.class, ClientMessageBlockEvent::encode, ClientMessageBlockEvent::decode, new ClientMessageBlockEvent.Handler());
        network.registerMessage(4, ServerPlayerInteractMessage.class, ServerPlayerInteractMessage::encode, ServerPlayerInteractMessage::decode, new ServerPlayerInteractMessage.Handler());
        network.registerMessage(5, ClientMessagePlayerEffect.class, ClientMessagePlayerEffect::encode, ClientMessagePlayerEffect::decode, new ClientMessagePlayerEffect.Handler());
        network.registerMessage(6, ClientMessageAnimation.class, ClientMessageAnimation::encode, ClientMessageAnimation::decode,new ClientMessageAnimation.Handler());
        network.registerMessage(7, ServerMessageAnimationSync.class, ServerMessageAnimationSync::encode, ServerMessageAnimationSync::decode,new ServerMessageAnimationSync.Handler());
        network.registerMessage(8, ServerMessageActionSync.class, ServerMessageActionSync::encode, ServerMessageActionSync::decode,new ServerMessageActionSync.Handler());
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

    public SimpleChannel getAPINetwork() {
        return network;
    }
}
