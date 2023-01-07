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
