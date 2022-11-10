package org.astemir.example.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.model.ItemModelWrapper;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.client.model.ModelMace;
import org.astemir.example.common.item.MaceItem;

public class ModelWrapperMace extends ItemModelWrapper<MaceItem> {


    public final ResourceLocation TEXTURE = ResourceUtils.texture("item/mace_in_hand.png");
    private final ModelMace MODEL = new ModelMace();

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }

    @Override
    public AdvancedModel<MaceItem> getModel(MaceItem target) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTexture(MaceItem target) {
        return TEXTURE;
    }
}
