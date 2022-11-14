package org.astemir.example;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import org.astemir.api.client.render.AdvancedItemRenderer;
import org.astemir.api.client.TESRModels;
import org.astemir.api.client.render.ArmorModels;
import org.astemir.api.common.event.EventManager;
import org.astemir.example.client.renderer.CosmicBeaconRenderer;
import org.astemir.example.client.renderer.MinotaurRenderer;
import org.astemir.example.client.renderer.SharkBoatRenderer;
import org.astemir.example.client.wrapper.ModelWrapperMace;
import org.astemir.example.client.wrapper.ModelWrapperTestArmor;
import org.astemir.example.common.block.ModBlocks;
import org.astemir.example.common.entity.ModEntities;
import org.astemir.example.common.item.ModItems;
import static org.astemir.example.ExampleAPIMod.MOD_ID;


@Mod(MOD_ID)
public class ExampleAPIMod extends SkillsAPI {

    public final static String MOD_ID = "skillsapi";

    public static boolean INITIALIZE_EXAMPLE_FEATURES = true;

    public ExampleAPIMod() {
        super(MOD_ID);
        defaultInit();
        if (INITIALIZE_EXAMPLE_FEATURES) {
            ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModBlocks.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        defaultCommon();
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        EventManager.registerForgeEventClass(ClientStateHandler.class);
        if (INITIALIZE_EXAMPLE_FEATURES) {
            ModelWrapperTestArmor testArmor = new ModelWrapperTestArmor();
            ArmorModels.addModel(ModItems.TEST_HELMET.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_CHESTPLATE.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_LEGGINGS.get(), testArmor);
            ArmorModels.addModel(ModItems.TEST_BOOTS.get(), testArmor);
            AdvancedItemRenderer.addModel(ModItems.MACE.get(), new ModelWrapperMace());
            BlockEntityRenderers.register(ModBlocks.COSMIC_BEACON_ENTITY.get(), CosmicBeaconRenderer::new);
            EntityRenderers.register(ModEntities.MINOTAUR.get(), MinotaurRenderer::new);
            EntityRenderers.register(ModEntities.SHARK_BOAT.get(), SharkBoatRenderer::new);
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
