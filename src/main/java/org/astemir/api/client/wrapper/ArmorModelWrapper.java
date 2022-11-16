package org.astemir.api.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.misc.AdvancedCubeRenderer;
import org.astemir.api.client.misc.RenderCall;
import org.astemir.api.common.animation.ITESRModel;
import org.astemir.api.math.Vector3;

public abstract class ArmorModelWrapper<T extends Item & ITESRModel> extends HumanoidModel<LivingEntity> implements IModelWrapper<T> {


    public T renderTarget;
    public ItemStack itemStack;
    public MultiBufferSource multiBufferSource;

    public ArmorModelWrapper() {
        super(HumanoidModel.createMesh(CubeDeformation.NONE,0.0f).getRoot().bake(64,64));
    }


    public void renderWrapper(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_, RenderCall renderCall,boolean resetBuffer) {
        boolean hasFoil = false;
        if (itemStack != null){
            hasFoil = itemStack.hasFoil();
        }
        VertexConsumer consumer =  ItemRenderer.getFoilBufferDirect(Minecraft.getInstance().renderBuffers().bufferSource(),getRenderType(getRenderTarget(),getTexture(getRenderTarget())), false, hasFoil);
        AdvancedModel<T> model = getModel(renderTarget);
        model.modelWrapper = this;
        model.renderModel(p_103111_,consumer,p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_,renderCall,resetBuffer);
    }

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        renderWrapper(p_103111_,p_103112_,p_103113_,p_103114_,p_103115_,p_103116_,p_103117_,p_103118_,RenderCall.MODEL,false);
    }

    public void setupAngles(LivingEntity entity, T target, ItemStack stack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
        this.renderTarget = target;
        this.itemStack = stack;
        AdvancedModel<T> model = getModel(target);
        AdvancedCubeRenderer bipedHead = model.getModelRenderer("bipedHead");
        AdvancedCubeRenderer bipedBody = model.getModelRenderer("bipedBody");
        AdvancedCubeRenderer bipedLeftArm = model.getModelRenderer("bipedLeftArm");
        AdvancedCubeRenderer bipedRightArm = model.getModelRenderer("bipedRightArm");
        AdvancedCubeRenderer armorLeftLeg = model.getModelRenderer("armorLeftLeg");
        AdvancedCubeRenderer armorRightLeg = model.getModelRenderer("armorRightLeg");
        AdvancedCubeRenderer armorLeftBoot = model.getModelRenderer("armorLeftBoot");
        AdvancedCubeRenderer armorRightBoot = model.getModelRenderer("armorRightBoot");
        bipedHead.showModel = false;
        bipedBody.showModel = false;
        bipedLeftArm.showModel = false;
        bipedRightArm.showModel = false;
        armorLeftLeg.showModel = false;
        armorRightLeg.showModel = false;
        armorLeftBoot.showModel = false;
        armorRightBoot.showModel = false;
        if (equipmentSlot == EquipmentSlot.HEAD){
            translateRenderer(bipedHead,original.head,Vector3.ZERO);
        }else
        if (equipmentSlot == EquipmentSlot.CHEST){
            translateRenderer(bipedBody,original.body,Vector3.ZERO);
            translateRenderer(bipedRightArm,original.rightArm,new Vector3(1.25f,0,0));
            translateRenderer(bipedLeftArm,original.leftArm,new Vector3(-1.25f,0,0));
        }else
        if (equipmentSlot == EquipmentSlot.LEGS){
            translateRenderer(armorLeftLeg,original.leftLeg,new Vector3(-2.5f,12,0));
            translateRenderer(armorRightLeg,original.rightLeg,new Vector3(2.5f,12,0));
        }else
        if (equipmentSlot == EquipmentSlot.FEET){
            translateRenderer(armorLeftBoot,original.leftLeg,new Vector3(-2.5f,12,0));
            translateRenderer(armorRightBoot,original.rightLeg,new Vector3(2.5f,12,0));
        }
        if (original.young) {
            float headScale = 1.5F / 2;
            float bodyScale = 1F / 2;
            float headOffset = 16;
            float bodyOffset = 24;
            bipedHead.setScale(new Vector3(headScale,headScale,headScale));
            bipedBody.setScale(new Vector3(bodyScale, bodyScale, bodyScale));
            bipedLeftArm.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            bipedRightArm.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            bipedHead.setPosition(new Vector3(0,-headOffset,0));
            bipedBody.setPosition(new Vector3(0,-bodyOffset,0));
            bipedLeftArm.setPosition(new Vector3(-6.5f, -22,0));
            bipedRightArm.setPosition(new Vector3(6.5f, -22,0));
            armorLeftLeg.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            armorRightLeg.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            armorRightLeg.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            armorRightBoot.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
            armorLeftBoot.setScale(new Vector3(bodyScale,bodyScale,bodyScale));
        }
    }

    public void translateRenderer(AdvancedCubeRenderer advancedCubeRenderer,ModelPart part,Vector3 position){
        advancedCubeRenderer.rotationPoint = new Vector3(part.x,part.y,part.z);
        advancedCubeRenderer.setRotation(new Vector3(part.xRot,part.yRot,part.zRot));
        advancedCubeRenderer.setPosition(position);
        advancedCubeRenderer.setScale(new Vector3(1,1,1));
        advancedCubeRenderer.showModel = true;
    }

    @Override
    public void setupAnim(LivingEntity p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
    }

    public RenderType getRenderType(T entity, ResourceLocation texture){
        return RenderType.entityCutoutNoCull(texture);
    }

    public abstract AdvancedModel<T> getModel(T target);

    public abstract ResourceLocation getTexture(T target);

    public T getRenderTarget() {
        return renderTarget;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }
}
