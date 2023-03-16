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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.client.display.DisplayArgumentArmor;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.api.math.vector.Vector3;


public abstract class SkillsWrapperArmor<T extends Item & ICustomRendered> extends HumanoidModel<LivingEntity> implements IModelWrapper<T, DisplayArgumentArmor> {


    public T renderTarget;
    public ItemStack itemStack;
    public MultiBufferSource multiBufferSource;

    public SkillsWrapperArmor() {
        super(HumanoidModel.createMesh(CubeDeformation.NONE,0.0f).getRoot().bake(64,64));
    }


    public void renderWrapper(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float a, RenderCall renderCall,boolean resetBuffer) {
        boolean hasFoil = false;
        if (itemStack != null){
            hasFoil = itemStack.hasFoil();
        }
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(Minecraft.getInstance().renderBuffers().bufferSource(),getRenderType(), false, hasFoil);
        SkillsModel<T, DisplayArgumentArmor> model = getModel(renderTarget);
        model.modelWrapper = this;
        model.renderWithLayers(poseStack,consumer,packedLight, packedOverlay, r, g, b, a,renderCall,resetBuffer);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        renderWrapper(poseStack,vertexConsumer,packedLight,packedOverlay,r,g,b,a,RenderCall.MODEL,false);
    }

    public void setupAngles(LivingEntity entity, T target, ItemStack stack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
        this.renderTarget = target;
        this.itemStack = stack;
        SkillsModel<T, DisplayArgumentArmor> model = getModel(target);
        ModelElement bipedHead = model.getModelElement("bipedHead");
        ModelElement bipedBody = model.getModelElement("bipedBody");
        ModelElement bipedLeftArm = model.getModelElement("bipedLeftArm");
        ModelElement bipedRightArm = model.getModelElement("bipedRightArm");
        ModelElement armorLeftLeg = model.getModelElement("armorLeftLeg");
        ModelElement armorRightLeg = model.getModelElement("armorRightLeg");
        ModelElement armorLeftBoot = model.getModelElement("armorLeftBoot");
        ModelElement armorRightBoot = model.getModelElement("armorRightBoot");
        bipedHead.showModel = false;
        bipedBody.showModel = false;
        bipedLeftArm.showModel = false;
        bipedRightArm.showModel = false;
        armorLeftLeg.showModel = false;
        armorRightLeg.showModel = false;
        armorLeftBoot.showModel = false;
        armorRightBoot.showModel = false;
        if (equipmentSlot == EquipmentSlot.HEAD){
            translateRenderer(bipedHead,original.head,Vector3.zero());
        }else
        if (equipmentSlot == EquipmentSlot.CHEST){
            translateRenderer(bipedBody,original.body,Vector3.zero());
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
        model.setupAnim(renderTarget,new DisplayArgumentArmor(entity,itemStack,equipmentSlot),0,0,0,0,0);
    }

    public void translateRenderer(ModelElement advancedCubeRenderer, ModelPart part, Vector3 position){
        advancedCubeRenderer.rotationPoint = new Vector3(part.x,part.y,part.z);
        advancedCubeRenderer.setRotation(new Vector3(part.xRot,part.yRot,part.zRot));
        advancedCubeRenderer.setPosition(position);
        advancedCubeRenderer.setScale(new Vector3(1,1,1));
        advancedCubeRenderer.showModel = true;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public T getRenderTarget() {
        return renderTarget;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.entityCutoutNoCull(getModel(renderTarget).getTexture(renderTarget));
    }

    @Override
    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }
}
