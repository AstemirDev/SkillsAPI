package org.astemir.api;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.astemir.api.client.ArmorModels;
import org.astemir.api.client.ClientStateHandler;
import org.astemir.api.client.TESRModels;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.api.common.event.EventEntityRegister;
import org.astemir.api.common.event.EventCommandRegister;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.common.event.EventMisc;
import org.astemir.api.network.messages.*;
import org.astemir.api.utils.NetworkUtils;
import org.astemir.example.client.render.armor.ModelWrapperTestArmor;
import org.astemir.example.client.render.beacon.RendererExampleCosmicBeacon;
import org.astemir.example.client.render.mace.WrapperExampleMace;
import org.astemir.example.client.render.minotaur.RendererExampleMinotaur;
import org.astemir.example.client.render.sharkboat.RendererExampleSharkBoat;
import org.astemir.example.common.block.ModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.example.common.item.ModItems;
import static org.astemir.api.SkillsAPI.MOD_ID;

@Mod(MOD_ID)
public class SkillsAPI extends ForgeMod {

    public final static String MOD_ID = "skillsapi";

    public static final SimpleChannel API_NETWORK = NetworkUtils.createNetworkChannel(MOD_ID,"api_network_channel");

    public static SkillsAPI INSTANCE;

    public static volatile boolean INITIALIZED = false;

    public static boolean INITIALIZE_EXAMPLE_FEATURES = true;


    static{
        initializeNetwork();
    }


    public SkillsAPI() {
        INSTANCE = this;
        if (INITIALIZE_EXAMPLE_FEATURES) {
            ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ExampleModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
        EventManager.registerForgeEventClass(EventMisc.class);
        EventManager.registerForgeEventClass(EventCommandRegister.class);
        EventManager.registerFMLEvent(EventEntityRegister::onAttributesLoad);
    }


    private static synchronized void initializeNetwork(){
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

    @Override
    protected void onClientSetup(FMLClientSetupEvent event) {
        EventManager.registerForgeEventClass(ClientStateHandler.class);
        if (INITIALIZE_EXAMPLE_FEATURES) {
            ModelWrapperTestArmor testArmor = new ModelWrapperTestArmor();
            ArmorModels.addModel(ModItems.TEST_HELMET.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_CHESTPLATE.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_LEGGINGS.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_BOOTS.get(), testArmor);
            AdvancedRendererItem.addModel(ModItems.MACE.get(), new WrapperExampleMace());
            BlockEntityRenderers.register(ModBlocks.COSMIC_BEACON_ENTITY.get(), RendererExampleCosmicBeacon::new);
            EntityRenderers.register(ExampleModEntities.MINOTAUR.get(), RendererExampleMinotaur::new);
            EntityRenderers.register(ExampleModEntities.SHARK_BOAT.get(), RendererExampleSharkBoat::new);
        }
    }

    @Override
    protected void onUnsafeClientSetup() {
        EventManager.registerFMLEvent(TESRModels::onModelRegistryInit);
        EventManager.registerFMLEvent(TESRModels::onModelBakeEvent);
        EventManager.registerFMLEvent(AdvancedRendererItem::onRegisterReloadListener);
        if (INITIALIZE_EXAMPLE_FEATURES) {
            TESRModels.addModelReplacement("skillsapi:mace", "skillsapi:mace_in_hand");
        }
    }
}
