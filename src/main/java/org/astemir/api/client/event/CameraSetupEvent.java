package org.astemir.api.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class CameraSetupEvent extends Event {

    private PoseStack poseStack;
    private float partialTicks;
    private long finishTimeNano;

    public CameraSetupEvent(PoseStack poseStack, float partialTicks, long finishTimeNano) {
        this.poseStack = poseStack;
        this.partialTicks = partialTicks;
        this.finishTimeNano = finishTimeNano;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public long getFinishTimeNano() {
        return finishTimeNano;
    }
}
