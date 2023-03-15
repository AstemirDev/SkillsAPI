package org.astemir.api.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class LivingTransformEvent<T extends LivingEntity> extends Event {

    private T entityLiving;
    private PoseStack matrixStack;

    public LivingTransformEvent(T entityLiving, PoseStack matrixStack) {
        this.entityLiving = entityLiving;
        this.matrixStack = matrixStack;
    }

    public static class Rotation<T extends LivingEntity> extends LivingTransformEvent<T> {

        private float ageInTicks;
        private float rotationYaw;
        private  float partialTicks;

        public Rotation(T entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
            super(entityLiving, matrixStack);
            this.ageInTicks = ageInTicks;
            this.rotationYaw = rotationYaw;
            this.partialTicks = partialTicks;
        }

        public float getAgeInTicks() {
            return ageInTicks;
        }

        public void setAgeInTicks(float ageInTicks) {
            this.ageInTicks = ageInTicks;
        }

        public float getRotationYaw() {
            return rotationYaw;
        }

        public void setRotationYaw(float rotationYaw) {
            this.rotationYaw = rotationYaw;
        }

        public float getPartialTicks() {
            return partialTicks;
        }

        public void setPartialTicks(float partialTicks) {
            this.partialTicks = partialTicks;
        }
    }

    public static class Scale<T extends LivingEntity> extends LivingTransformEvent<T> {

        private float pPartialTickTime;

        public Scale(T entityLiving, PoseStack matrixStack, float pPartialTickTime) {
            super(entityLiving, matrixStack);
            this.pPartialTickTime = pPartialTickTime;
        }

        public float getpPartialTickTime() {
            return pPartialTickTime;
        }

        public void setpPartialTickTime(float pPartialTickTime) {
            this.pPartialTickTime = pPartialTickTime;
        }
    }

    public T getEntityLiving() {
        return entityLiving;
    }

    public void setEntityLiving(T entityLiving) {
        this.entityLiving = entityLiving;
    }

    public PoseStack getMatrixStack() {
        return matrixStack;
    }

    public void setMatrixStack(PoseStack matrixStack) {
        this.matrixStack = matrixStack;
    }
}
