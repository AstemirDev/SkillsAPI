package org.astemir.example.client.render.sharkboat;


import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.wrapper.SkillsWrapperEntity;
import org.astemir.example.common.entity.EntityExampleSharkBoat;

public class WrapperExampleSharkBoat extends SkillsWrapperEntity<EntityExampleSharkBoat> {

    private final ModelExampleSharkBoat MODEL = new ModelExampleSharkBoat();

    @Override
    public SkillsModel<EntityExampleSharkBoat, IDisplayArgument> getModel(EntityExampleSharkBoat sharkBoat) {
        return MODEL;
    }
}
