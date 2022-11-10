package org.astemir.example.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.astemir.api.client.render.AdvancedBlockEntityRenderer;
import org.astemir.example.client.wrapper.ModelWrapperCosmicBeacon;
import org.astemir.example.common.block.BlockEntityCosmicBeacon;

public class CosmicBeaconRenderer extends AdvancedBlockEntityRenderer<BlockEntityCosmicBeacon> {

    public CosmicBeaconRenderer(BlockEntityRendererProvider.Context p_173636_) {
        super(p_173636_,new ModelWrapperCosmicBeacon());
    }
}
