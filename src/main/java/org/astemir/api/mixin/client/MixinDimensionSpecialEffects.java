package org.astemir.api.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.event.SkySetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(value={DimensionSpecialEffects.class},priority = 500)
public class MixinDimensionSpecialEffects {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = SkillsAPI.REMAP)
    @Nullable
    public float[] getSunriseColor(float pTimeOfDay, float pPartialTicks) {
        ClientLevel level = Minecraft.getInstance().level;
        SkySetupEvent.ComputeSunriseColor event = new SkySetupEvent.ComputeSunriseColor(pTimeOfDay,level.getRainLevel(pPartialTicks),level.getThunderLevel(pPartialTicks),pPartialTicks);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getSunriseColor();
    }
}
