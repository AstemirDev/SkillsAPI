package org.astemir.api.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.event.GlobalCameraSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {Camera.class},priority = 500)
public abstract class MixinCamera {

    @Shadow private boolean initialized;
    @Shadow private BlockGetter level;
    @Shadow private Entity entity;
    @Shadow private boolean detached;

    @Shadow protected abstract void setRotation(float pYRot, float pXRot);

    @Shadow protected abstract void setPosition(double pX, double pY, double pZ);

    @Shadow private float eyeHeightOld;
    @Shadow private float eyeHeight;
    @Shadow private float yRot;
    @Shadow private float xRot;

    @Shadow protected abstract void move(double pDistanceOffset, double pVerticalOffset, double pHorizontalOffset);

    @Shadow protected abstract double getMaxZoom(double pStartingDistance);

    /**
     * @author Astemir
     * @reason No customization
     */
    @Overwrite(remap = SkillsAPI.REMAP)
    public void setup(BlockGetter pLevel, Entity pEntity, boolean pDetached, boolean pThirdPersonReverse, float pPartialTick) {
        this.initialized = true;
        this.level = pLevel;
        this.entity = pEntity;
        this.detached = pDetached;
        GlobalCameraSetupEvent event = new GlobalCameraSetupEvent(pEntity,pPartialTick,detached,pThirdPersonReverse);
        event.setRotation(pEntity.getViewYRot(pPartialTick), pEntity.getViewXRot(pPartialTick));
        event.setPosition(Mth.lerp((double)pPartialTick, pEntity.xo, pEntity.getX()), Mth.lerp((double)pPartialTick, pEntity.yo, pEntity.getY()) + (double)Mth.lerp(pPartialTick, this.eyeHeightOld, this.eyeHeight), Mth.lerp((double)pPartialTick, pEntity.zo, pEntity.getZ()));
        MinecraftForge.EVENT_BUS.post(event);
        setRotation(event.getRotation().x,event.getRotation().y);
        setPosition(event.getPosition().x,event.getPosition().y,event.getPosition().z);
        move(event.getOffset().x,event.getOffset().y,event.getOffset().z);
        if (event.isVanillaBehavior()){
            if (pDetached) {
                if (pThirdPersonReverse) {
                    this.setRotation(this.yRot + 180.0F, -this.xRot);
                }
                this.move(-this.getMaxZoom(4.0D), 0.0D, 0.0D);
            } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).isSleeping()) {
                Direction direction = ((LivingEntity)pEntity).getBedOrientation();
                this.setRotation(direction != null ? direction.toYRot() - 180.0F : 0.0F, 0.0F);
                this.move(0.0D, 0.3D, 0.0D);
            }
        }
    }

}
