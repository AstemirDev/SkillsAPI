package org.astemir.example;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.astemir.api.ISafeClientLoader;
import org.astemir.api.SkillsForgeMod;
import org.astemir.api.client.ClientManager;
import org.astemir.api.client.registry.TESRModelsRegistry;
import org.astemir.api.client.render.SkillsRendererItem;
import org.astemir.api.common.animation.AnimationEventListener;
import org.astemir.api.common.entity.EntityEventListener;
import org.astemir.api.common.commands.CommandRegister;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.common.event.WorldEventListener;
import org.astemir.api.common.world.SkillsAPIBiomeModifier;
import org.astemir.api.network.messages.*;
import org.astemir.api.network.NetworkUtils;
import org.astemir.example.common.block.ExampleModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.api.common.entity.PlayerEventListener;
import org.astemir.example.common.item.ExampleModItems;
import static org.astemir.example.SkillsAPI.MOD_ID;

@Mod(MOD_ID)
public class SkillsAPI extends SkillsForgeMod {

    public final static String MOD_ID = "skillsapi";

    public final static String PROTOCOL_VERSION = Integer.toString(1);

    public static final SimpleChannel API_NETWORK = NetworkUtils.createNetworkChannel(MOD_ID,"main_channel",PROTOCOL_VERSION);

    public static volatile boolean INITIALIZED = false;

    public static boolean INITIALIZE_EXAMPLE_FEATURES = true;

    public static final boolean REMAP = true;

    public static SkillsAPI INSTANCE;


    public SkillsAPI() {
        INSTANCE = this;
        if (isInitializeExampleFeatures()) {
            ExampleModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ExampleModBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ExampleModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ExampleModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
        SkillsAPIBiomeModifier.register(FMLJavaModLoadingContext.get().getModEventBus());
        EventManager.registerForgeEventClass(WorldEventListener.class);
        EventManager.registerForgeEventClass(AnimationEventListener.class);
        EventManager.registerForgeEventClass(CommandRegister.class);
        EventManager.registerForgeEventClass(EntityEventListener.class);
        EventManager.registerForgeEventClass(PlayerEventListener.class);
        EventManager.registerFMLEvent(EntityEventListener::onEntityAttributesLoad);
    }


    synchronized public static void initialize(){
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
        }
        INITIALIZED = true;
    }

    @Override
    protected void onCommonSetup(FMLCommonSetupEvent event) {
        initialize();
    }

    @Override
    protected void onClientSetup(FMLClientSetupEvent event) {
        EventManager.registerForgeEventClass(ClientManager.class);
    }

    @Override
    protected void onUnsafeClientSetup() {
        EventManager.registerFMLEvent(TESRModelsRegistry::onModelRegistryInit);
        EventManager.registerFMLEvent(TESRModelsRegistry::onModelBakeEvent);
        EventManager.registerFMLEvent(SkillsRendererItem::onRegisterReloadListener);
        if (isInitializeExampleFeatures()) {
            TESRModelsRegistry.addModelReplacement("skillsapi:mace", "skillsapi:mace_in_hand");
        }
    }

    @Override
    public ISafeClientLoader getClientLoader() {
        return new IClientLoaderExample();
    }

    public static boolean isInitializeExampleFeatures() {
        return INITIALIZE_EXAMPLE_FEATURES;
    }
}
