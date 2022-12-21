package org.astemir.example;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.astemir.api.ISafeClientsideBullshitLoader;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.ArmorModels;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.example.client.render.armor.ModelWrapperTestArmor;
import org.astemir.example.client.render.beacon.RendererExampleCosmicBeacon;
import org.astemir.example.client.render.mace.WrapperExampleMace;
import org.astemir.example.client.render.minotaur.RendererExampleMinotaur;
import org.astemir.example.client.render.sharkboat.RendererExampleSharkBoat;
import org.astemir.example.common.block.ModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.example.common.item.ModItems;

public class IClientLoaderExample implements ISafeClientsideBullshitLoader {

    @Override
    public void load() {
        if (SkillsAPI.isInitializeExampleFeatures()) {
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
}
