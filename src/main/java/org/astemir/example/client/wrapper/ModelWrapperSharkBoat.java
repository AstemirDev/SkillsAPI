package org.astemir.example.client.wrapper;


import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.EntityModelWrapper;
import org.astemir.example.client.model.ModelSharkBoat;
import org.astemir.example.common.entity.EntitySharkBoat;

public class ModelWrapperSharkBoat extends EntityModelWrapper<EntitySharkBoat> {

    private final ModelSharkBoat MODEL = new ModelSharkBoat();

    @Override
    public AdvancedModel<EntitySharkBoat> getModel(EntitySharkBoat sharkBoat) {
        return MODEL;
    }
}
