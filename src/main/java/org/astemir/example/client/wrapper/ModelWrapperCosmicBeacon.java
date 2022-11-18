package org.astemir.example.client.wrapper;


import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.BlockEntityModelWrapper;
import org.astemir.example.client.model.ModelCosmicBeacon;
import org.astemir.example.common.block.BlockEntityCosmicBeacon;

public class ModelWrapperCosmicBeacon extends BlockEntityModelWrapper<BlockEntityCosmicBeacon> {

    private final ModelCosmicBeacon MODEL = new ModelCosmicBeacon();


    @Override
    public AdvancedModel<BlockEntityCosmicBeacon> getModel(BlockEntityCosmicBeacon cosmicBeacon) {
        return MODEL;
    }
}
