package org.astemir.example.client.wrapper;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.model.BlockEntityModelWrapper;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.ExampleAPIMod;
import org.astemir.example.client.model.ModelCosmicBeacon;
import org.astemir.example.common.block.BlockEntityCosmicBeacon;

public class ModelWrapperCosmicBeacon extends BlockEntityModelWrapper<BlockEntityCosmicBeacon> {

    public final ResourceLocation TEXTURE = ResourceUtils.texture(ExampleAPIMod.MOD_ID,"block/cosmic_beacon.png");

    private final ModelCosmicBeacon MODEL = new ModelCosmicBeacon();

    @Override
    public AdvancedModel<BlockEntityCosmicBeacon> getModel(BlockEntityCosmicBeacon target) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTexture(BlockEntityCosmicBeacon target) {
        return TEXTURE;
    }
}
