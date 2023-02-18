package org.astemir.example.client.render.beacon;


import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.AbstractModelWrapperBlockEntity;
import org.astemir.example.common.block.BlockEntityExampleCosmicBeacon;

public class WrapperExampleCosmicBeacon extends AbstractModelWrapperBlockEntity<BlockEntityExampleCosmicBeacon> {

    private final ModelExampleCosmicBeacon MODEL = new ModelExampleCosmicBeacon();


    @Override
    public AdvancedModel<BlockEntityExampleCosmicBeacon,Object> getModel(BlockEntityExampleCosmicBeacon cosmicBeacon) {
        return MODEL;
    }
}
