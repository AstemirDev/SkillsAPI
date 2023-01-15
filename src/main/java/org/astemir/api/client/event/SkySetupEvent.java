package org.astemir.api.client.event;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

public abstract class SkySetupEvent extends Event {

    private float timeOfDay;
    private float rainLevel;
    private float thunderLevel;
    private float partialTick;

    public SkySetupEvent(float timeOfDay, float rainLevel, float thunderLevel, float partialTick) {
        this.timeOfDay = timeOfDay;
        this.rainLevel = rainLevel;
        this.thunderLevel = thunderLevel;
        this.partialTick = partialTick;
    }


    public static class ComputeCloudColor extends SkySetupEvent{

        private Vec3 color;

        public ComputeCloudColor(float timeOfDay, float rainLevel, float thunderLevel, float partialTick, Vec3 color) {
            super(timeOfDay, rainLevel, thunderLevel, partialTick);
            this.color = color;
        }

        public Vec3 getColor() {
            return color;
        }

        public void setColor(Vec3 color) {
            this.color = color;
        }
    }


    public static class ComputeSunriseColor extends SkySetupEvent{

        private float[] sunriseCol = new float[4];

        public ComputeSunriseColor(float timeOfDay, float rainLevel, float thunderLevel, float partialTick) {
            super(timeOfDay, rainLevel, thunderLevel, partialTick);
            float f = 0.4F;
            float f1 = Mth.cos(timeOfDay * ((float) Math.PI * 2F)) - 0.0F;
            float f2 = -0.0F;
            if (!isNull()) {
                float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
                float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float) Math.PI)) * 0.99F;
                f4 *= f4;
                this.sunriseCol[0] = f3 * 0.3F + 0.7F;
                this.sunriseCol[1] = f3 * f3 * 0.7F + 0.2F;
                this.sunriseCol[2] = f3 * f3 * 0.0F + 0.2F;
                this.sunriseCol[3] = f4;
            }else{
                sunriseCol = null;
            }
        }

        public boolean isNull(){
            float f1 = Mth.cos(getTimeOfDay() * ((float) Math.PI * 2F)) - 0.0F;
            if (f1 >= -0.4F && f1 <= 0.4F) {
                return false;
            }
            return true;
        }

        public float[] getSunriseColor() {
            return sunriseCol;
        }

        public void setSunriseColor(float[] sunriseCol) {
            this.sunriseCol = sunriseCol;
        }
    }


    public static abstract class ComputeSkyColor extends SkySetupEvent{

        private Vec3 pos;
        private Vec3 color;

        public ComputeSkyColor(float timeOfDay, float rainLevel, float thunderLevel, float partialTick,Vec3 pos,Vec3 color) {
            super(timeOfDay, rainLevel, thunderLevel, partialTick);
            this.pos = pos;
            this.color = color;
        }


        public static class Pre extends ComputeSkyColor{

            public Pre(float timeOfDay, float rainLevel, float thunderLevel, float partialTick, Vec3 pos, Vec3 color) {
                super(timeOfDay, rainLevel, thunderLevel, partialTick, pos, color);
            }
        }

        public static class Post extends ComputeSkyColor{

            public Post(float timeOfDay, float rainLevel, float thunderLevel, float partialTick, Vec3 pos, Vec3 color) {
                super(timeOfDay, rainLevel, thunderLevel, partialTick, pos, color);
            }
        }

        public Vec3 getPos() {
            return pos;
        }

        public void setPos(Vec3 pos) {
            this.pos = pos;
        }

        public Vec3 getColor() {
            return color;
        }

        public void setColor(Vec3 color) {
            this.color = color;
        }
    }

    public static class ComputeDarkness extends SkySetupEvent{

        private float darkness;

        public ComputeDarkness(float timeOfDay, float rainLevel, float thunderLevel, float partialTick, float darkness) {
            super(timeOfDay, rainLevel, thunderLevel, partialTick);
            this.darkness = darkness;
        }

        public float getDarkness() {
            return darkness;
        }

        public void setDarkness(float darkness) {
            this.darkness = darkness;
        }
    }

    public static class ComputeStarBrightness extends SkySetupEvent{

        private float brightness;

        public ComputeStarBrightness(float timeOfDay, float rainLevel, float thunderLevel, float partialTick, float brightness) {
            super(timeOfDay, rainLevel, thunderLevel, partialTick);
            this.brightness = brightness;
        }

        public float getBrightness() {
            return brightness;
        }

        public void setBrightness(float brightness) {
            this.brightness = brightness;
        }
    }



    public float getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(float timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public float getRainLevel() {
        return rainLevel;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public float getThunderLevel() {
        return thunderLevel;
    }

    public void setThunderLevel(float thunderLevel) {
        this.thunderLevel = thunderLevel;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public void setPartialTick(float partialTick) {
        this.partialTick = partialTick;
    }
}
