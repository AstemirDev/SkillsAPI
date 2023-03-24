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

    public static SmoothnessType SQR = new SmoothnessType() {
        @Override
        public float deltaCalculate(float partialTicks, float smoothness) {
            float f1 = SmoothnessType.DEFAULT.deltaCalculate(partialTicks,smoothness);
            return f1*f1;
        }
    };


    public static SmoothnessType SQR_EXPONENTIAL = new SmoothnessType() {
        @Override
        public float deltaCalculate(float partialTicks, float smoothness) {
            float f1 = SmoothnessType.EXPONENTIAL.deltaCalculate(partialTicks,smoothness);
            return f1*f1;
        }
    };


    public float deltaCalculate(float partialTicks,float smoothness){
        return partialTicks/smoothness;
    }
}
