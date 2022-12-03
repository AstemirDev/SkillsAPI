package org.astemir.example.client.render.sharkboat;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.astemir.api.client.render.AdvancedRendererEntity;
import org.astemir.example.common.entity.EntityExampleSharkBoat;

public class RendererExampleSharkBoat extends AdvancedRendererEntity<EntityExampleSharkBoat, WrapperExampleSharkBoat> {

    public RendererExampleSharkBoat(EntityRendererProvider.Context context) {
        super(context,new WrapperExampleSharkBoat());
    }
}
