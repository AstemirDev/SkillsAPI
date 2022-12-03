package org.astemir.example.client.render.sharkboat;


import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.AbstractModelWrapperEntity;
import org.astemir.example.common.entity.EntityExampleSharkBoat;

public class WrapperExampleSharkBoat extends AbstractModelWrapperEntity<EntityExampleSharkBoat> {

    private final ModelExampleSharkBoat MODEL = new ModelExampleSharkBoat();

    @Override
    public AdvancedModel<EntityExampleSharkBoat> getModel(EntityExampleSharkBoat sharkBoat) {
        return MODEL;
    }
}
