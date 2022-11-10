package org.astemir.example.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.astemir.api.client.render.AdvancedEntityRenderer;
import org.astemir.example.client.wrapper.ModelWrapperSharkBoat;
import org.astemir.example.common.entity.EntitySharkBoat;

public class SharkBoatRenderer extends AdvancedEntityRenderer<EntitySharkBoat, ModelWrapperSharkBoat> {

    public SharkBoatRenderer(EntityRendererProvider.Context context) {
        super(context,new ModelWrapperSharkBoat());
    }
}
