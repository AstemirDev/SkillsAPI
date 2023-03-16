package org.astemir.example.client.render.beacon;


import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.wrapper.SkillsWrapperBlockEntity;
import org.astemir.example.common.block.BlockEntityExampleCosmicBeacon;

public class WrapperExampleCosmicBeacon extends SkillsWrapperBlockEntity<BlockEntityExampleCosmicBeacon> {

    private final ModelExampleCosmicBeacon MODEL = new ModelExampleCosmicBeacon();


    @Override
    public SkillsModel<BlockEntityExampleCosmicBeacon, IDisplayArgument> getModel(BlockEntityExampleCosmicBeacon cosmicBeacon) {
        return MODEL;
    }
}
