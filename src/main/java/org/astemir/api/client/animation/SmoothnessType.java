package org.astemir.api.client.animation;


import net.minecraft.client.Minecraft;

public abstract class SmoothnessType {

    public static SmoothnessType DEFAULT = new SmoothnessType() {
        @Override
        public float deltaCalculate(float partialTicks, float smoothness) {
            return partialTicks/smoothness;
        }
    };

    public static SmoothnessType EXPONENTIAL = new SmoothnessType() {
        @Override
        public float deltaCalculate(float partialTicks, float smoothness) {
            float f1 = 1f-(1f/smoothness);
            float dt = Minecraft.getInstance().getDeltaFrameTime();
            float p = (float) (1f - Math.pow(f1, dt));
            return p;
        }
    };

    public float deltaCalculate(float partialTicks,float smoothness){
        return partialTicks/smoothness;
    }
}
