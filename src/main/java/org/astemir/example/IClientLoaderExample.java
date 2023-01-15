package org.astemir.example;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.astemir.api.ISafeClientLoader;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.ArmorModels;
import org.astemir.api.client.ModelHelper;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.example.client.render.armor.ModelWrapperTestArmor;
import org.astemir.example.client.render.beacon.RendererExampleCosmicBeacon;
import org.astemir.example.client.render.mace.WrapperExampleMace;
import org.astemir.example.client.render.minotaur.RendererExampleMinotaur;
import org.astemir.example.client.render.sharkboat.RendererExampleSharkBoat;
import org.astemir.example.client.render.sharkboat.WrapperExampleSharkBoat;
import org.astemir.example.common.block.ExampleModBlocks;
import org.astemir.example.common.entity.ExampleModEntities;
import org.astemir.example.common.item.ExampleModItems;

public class IClientLoaderExample implements ISafeClientLoader {

    @Override
    public void load() {
        if (SkillsAPI.isInitializeExampleFeatures()) {
            ModelWrapperTestArmor testArmor = new ModelWrapperTestArmor();
            ArmorModels.addModel(ExampleModItems.TEST_HELMET.get(), testArmor);
            ArmorModels.addModel(ExampleModItems.TEST_CHESTPLATE.get(), testArmor);
            ArmorModels.addModel(ExampleModItems.TEST_LEGGINGS.get(), testArmor);
            ArmorModels.addModel(ExampleModItems.TEST_BOOTS.get(), testArmor);
            AdvancedRendererItem.addModel(ExampleModItems.MACE.get(), new WrapperExampleMace());
            BlockEntityRenderers.register(ExampleModBlocks.COSMIC_BEACON_ENTITY.get(), RendererExampleCosmicBeacon::new);
            EntityRenderers.register(ExampleModEntities.MINOTAUR.get(), RendererExampleMinotaur::new);
            EntityRenderers.register(ExampleModEntities.SHARK_BOAT.get(), (context)-> ModelHelper.entityRenderer(context,new WrapperExampleSharkBoat()));
        }
    }
}
