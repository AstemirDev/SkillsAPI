package org.astemir.example;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.astemir.api.IClientLoader;
import org.astemir.api.client.registry.ArmorModelsRegistry;
import org.astemir.api.client.ModelUtils;
import org.astemir.api.client.render.SkillsRendererItem;
import org.astemir.api.lib.shimmer.ShimmerLib;
import org.astemir.example.client.render.armor.ModelWrapperTestArmor;
import org.astemir.example.client.render.beacon.RendererExampleCosmicBeacon;
import org.astemir.example.client.render.mace.WrapperExampleMace;
import org.astemir.example.client.render.minotaur.RendererExampleMinotaur;
import org.astemir.example.client.render.sharkboat.WrapperExampleSharkBoat;
import org.astemir.example.common.block.ExampleModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.example.common.item.ExampleModItems;

public class IClientLoaderExample implements IClientLoader {

    @Override
    public void load() {
        if (SkillsAPI.isInitializeExampleFeatures()) {
            ModelWrapperTestArmor testArmor = new ModelWrapperTestArmor();
            ArmorModelsRegistry.addModel(ExampleModItems.TEST_HELMET.get(), testArmor);
            ArmorModelsRegistry.addModel(ExampleModItems.TEST_CHESTPLATE.get(), testArmor);
            ArmorModelsRegistry.addModel(ExampleModItems.TEST_LEGGINGS.get(), testArmor);
            ArmorModelsRegistry.addModel(ExampleModItems.TEST_BOOTS.get(), testArmor);
            SkillsRendererItem.addModel(ExampleModItems.MACE.get(), new WrapperExampleMace());
            BlockEntityRenderers.register(ExampleModBlocks.COSMIC_BEACON_ENTITY.get(), RendererExampleCosmicBeacon::new);
            EntityRenderers.register(ExampleModEntities.MINOTAUR.get(), RendererExampleMinotaur::new);
            EntityRenderers.register(ExampleModEntities.SHARK_BOAT.get(), (context)-> ModelUtils.entityRenderer(context,new WrapperExampleSharkBoat()));
        }
    }
}
