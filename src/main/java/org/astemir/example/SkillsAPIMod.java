package org.astemir.example;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.ClientStateHandler;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.api.client.TESRModels;
import org.astemir.api.client.ArmorModels;
import org.astemir.api.common.event.EventManager;
import org.astemir.api.network.messages.*;
import org.astemir.example.client.render.mace.WrapperExampleMace;
import org.astemir.example.client.render.minotaur.RendererExampleMinotaur;
import org.astemir.example.client.render.beacon.RendererExampleCosmicBeacon;
import org.astemir.example.client.render.sharkboat.RendererExampleSharkBoat;
import org.astemir.example.client.render.armor.ModelWrapperTestArmor;
import org.astemir.example.common.block.ModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.example.common.item.ModItems;
import static org.astemir.example.SkillsAPIMod.MOD_ID;


@Mod(MOD_ID)
public class SkillsAPIMod extends SkillsAPI {

    public final static String MOD_ID = "skillsapi";

    public static SkillsAPIMod INSTANCE;

    public static boolean INITIALIZE_EXAMPLE_FEATURES = false;

    static{
        initializeNetwork();
    }

    public SkillsAPIMod() {
        super(MOD_ID);
        INSTANCE = this;
        defaultInit();
        if (INITIALIZE_EXAMPLE_FEATURES) {
            ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ExampleModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
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
        if (INITIALIZE_EXAMPLE_FEATURES) {
            TESRModels.addModelReplacement("skillsapi:mace", "skillsapi:mace_in_hand");
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Override
    protected void onEnqueueIMC(InterModEnqueueEvent event) {

    }

    @Override
    protected void onProcessIMC(InterModProcessEvent event) {

    }
}
