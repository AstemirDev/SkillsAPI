package org.astemir.example.client.render.sharkboat;


import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SunModel;
import org.astemir.api.client.wrapper.MWEntity;
import org.astemir.example.common.entity.EntityExampleSharkBoat;

public class WrapperExampleSharkBoat extends MWEntity<EntityExampleSharkBoat> {

    private final ModelExampleSharkBoat MODEL = new ModelExampleSharkBoat();

    @Override
    public SunModel<EntityExampleSharkBoat, IDisplayArgument> getModel(EntityExampleSharkBoat sharkBoat) {
        return MODEL;
    }
}
