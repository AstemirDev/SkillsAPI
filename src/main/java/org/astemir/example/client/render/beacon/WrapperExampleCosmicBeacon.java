package org.astemir.example.client.render.beacon;


import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SunModel;
import org.astemir.api.client.wrapper.MWBlockEntity;
import org.astemir.example.common.block.BlockEntityExampleCosmicBeacon;

public class WrapperExampleCosmicBeacon extends MWBlockEntity<BlockEntityExampleCosmicBeacon> {

    private final ModelExampleCosmicBeacon MODEL = new ModelExampleCosmicBeacon();


    @Override
    public SunModel<BlockEntityExampleCosmicBeacon, IDisplayArgument> getModel(BlockEntityExampleCosmicBeacon cosmicBeacon) {
        return MODEL;
    }
}
