package org.astemir.api.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.eventbus.api.Event;
import org.astemir.api.math.components.Color;

public class SkyRenderEvent extends Event {

    private PoseStack stack;
    private Color color = new Color(1,1,1,1);
    private float sunSize = 30.0F;
    private float moonSize = 20.0F;
    private float sunVerticalOffset = 100.0F;
    private float moonVerticalOffset = -100.0F;
    private float xRot = 0;
    private float yRot = 0;
    private float timeOfDay;
    private float rainLevel;
    private float partialTicks;


    public SkyRenderEvent(PoseStack stack, float timeOfDay, float rainLevel, float partialTicks) {
        this.stack = stack;
        this.color.a = 1.0F-rainLevel;
        this.partialTicks = partialTicks;
        this.rainLevel = rainLevel;
        this.timeOfDay = timeOfDay;
        this.yRot = -90.0F;
        this.xRot = timeOfDay * 360.0F;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getSunSize() {
        return sunSize;
    }

    public void setSunSize(float sunSize) {
        this.sunSize = sunSize;
    }

    public float getMoonSize() {
        return moonSize;
    }

    public void setMoonSize(float moonSize) {
        this.moonSize = moonSize;
    }

    public float getSunVerticalOffset() {
        return sunVerticalOffset;
    }

    public void setSunVerticalOffset(float sunVerticalOffset) {
        this.sunVerticalOffset = sunVerticalOffset;
    }

    public float getMoonVerticalOffset() {
        return moonVerticalOffset;
    }

    public void setMoonVerticalOffset(float moonVerticalOffset) {
        this.moonVerticalOffset = moonVerticalOffset;
    }

    public float getXRot() {
        return xRot;
    }

    public void setXRot(float xRot) {
        this.xRot = xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public void setYRot(float yRot) {
        this.yRot = yRot;
    }

    public PoseStack getStack() {
        return stack;
    }

    public float getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(float timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public void setStack(PoseStack stack) {
        this.stack = stack;
    }

    public float getRainLevel() {
        return rainLevel;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
