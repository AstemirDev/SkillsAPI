package org.astemir.example.client.render.beacon;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.astemir.api.client.render.AdvancedRendererBlockEntity;
import org.astemir.example.common.block.BlockEntityExampleCosmicBeacon;

public class RendererExampleCosmicBeacon extends AdvancedRendererBlockEntity<BlockEntityExampleCosmicBeacon> {

    public RendererExampleCosmicBeacon(BlockEntityRendererProvider.Context p_173636_) {
        super(p_173636_,new WrapperExampleCosmicBeacon());
    }
}
