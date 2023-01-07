package org.astemir.api.mixin.client;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.event.HumanoidAnimationEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HumanoidModel.class})
public abstract class MixinHumanoidModel<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart hat;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;
    @Shadow public HumanoidModel.ArmPose leftArmPose;
    @Shadow public HumanoidModel.ArmPose rightArmPose;
    @Shadow public boolean crouching;
    @Shadow public float swimAmount;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",at = @At("HEAD"))
    public void onSetupAnimations(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci){
        HumanoidAnimationEvent<T> event = new HumanoidAnimationEvent<>(head,hat,body,rightArm,leftArm,rightLeg,leftLeg,leftArmPose,rightArmPose,crouching,swimAmount);
        MinecraftForge.EVENT_BUS.post(event);
        leftArmPose = event.getLeftArmPose();
        rightArmPose = event.getRightArmPose();
        swimAmount = event.getSwimAmount();
        crouching = event.isCrouching();
    }
}

