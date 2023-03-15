package org.astemir.api.client.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;


public class HumanoidModelEvent<T extends LivingEntity> extends Event {

    private T entity;
    private ModelPart head;
    private ModelPart hat;
    private ModelPart body;
    private ModelPart rightArm;
    private ModelPart leftArm;
    private ModelPart rightLeg;
    private ModelPart leftLeg;
    private HumanoidModel.ArmPose leftArmPose;
    private HumanoidModel.ArmPose rightArmPose;
    private boolean crouching;
    private float swimAmount;

    public HumanoidModelEvent(T entity, ModelPart head, ModelPart hat, ModelPart body, ModelPart rightArm, ModelPart leftArm, ModelPart rightLeg, ModelPart leftLeg, HumanoidModel.ArmPose leftArmPose, HumanoidModel.ArmPose rightArmPose, boolean crouching, float swimAmount) {
        this.entity = entity;
        this.head = head;
        this.hat = hat;
        this.body = body;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.leftArmPose = leftArmPose;
        this.rightArmPose = rightArmPose;
        this.crouching = crouching;
        this.swimAmount = swimAmount;
    }

    public T getEntity() {
        return entity;
    }

    public ModelPart getHead() {
        return head;
    }

    public ModelPart getHat() {
        return hat;
    }

    public ModelPart getBody() {
        return body;
    }

    public ModelPart getRightArm() {
        return rightArm;
    }

    public ModelPart getLeftArm() {
        return leftArm;
    }

    public ModelPart getRightLeg() {
        return rightLeg;
    }

    public ModelPart getLeftLeg() {
        return leftLeg;
    }

    public HumanoidModel.ArmPose getLeftArmPose() {
        return leftArmPose;
    }

    public void setLeftArmPose(HumanoidModel.ArmPose leftArmPose) {
        this.leftArmPose = leftArmPose;
    }

    public HumanoidModel.ArmPose getRightArmPose() {
        return rightArmPose;
    }

    public void setRightArmPose(HumanoidModel.ArmPose rightArmPose) {
        this.rightArmPose = rightArmPose;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public float getSwimAmount() {
        return swimAmount;
    }

    public void setSwimAmount(float swimAmount) {
        this.swimAmount = swimAmount;
    }
}
