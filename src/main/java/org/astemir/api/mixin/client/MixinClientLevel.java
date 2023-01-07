package org.astemir.api.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.event.SkySetupEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin({ClientLevel.class})
public abstract class MixinClientLevel extends Level {


    @Shadow @Final private Minecraft minecraft;
    @Shadow private int skyFlashTime;

    protected MixinClientLevel(WritableLevelData pLevelData, ResourceKey<Level> pDimension, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
        super(pLevelData, pDimension, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

    /**
     * @author Astemir
     * @reason Event handling
     */
    @Overwrite(remap = false)
    public float getSkyDarken(float pPartialTick) {
        float f = this.getTimeOfDay(pPartialTick);
        float f1 = 1.0F - (Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        float f2 = getRainLevel(pPartialTick);
        float f3 = getThunderLevel(pPartialTick);
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 *= 1.0F - f2 * 5.0F / 16.0F;
        f1 *= 1.0F - f3 * 5.0F / 16.0F;
        float darkness = f1 * 0.8F + 0.2F;
        SkySetupEvent.ComputeDarkness event = new SkySetupEvent.ComputeDarkness(f,f2,f3,pPartialTick,darkness);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDarkness();
    }

    /**
     * @author Astemir
     * @reason Shadowing
     */
    @Overwrite(remap = false)
    public Vec3 getSkyColor(Vec3 pPos, float pPartialTick) {
        float f = this.getTimeOfDay(pPartialTick);
        float f5 = this.getRainLevel(pPartialTick);
        float f9 = this.getThunderLevel(pPartialTick);
        Vec3 vec3 = pPos.subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
        BiomeManager biomemanager = this.getBiomeManager();
        Vec3 vec31 = CubicSampler.gaussianSampleVec3(vec3, (p_194161_, p_194162_, p_194163_) -> {
            return Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_194161_, p_194162_, p_194163_).value().getSkyColor());
        });
        SkySetupEvent.ComputeSkyColor.Pre preEvent = new SkySetupEvent.ComputeSkyColor.Pre(f,f5,f9,pPartialTick,vec3,vec31);
        MinecraftForge.EVENT_BUS.post(preEvent);
        vec31 = preEvent.getColor();
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = (float)vec31.x * f1;
        float f3 = (float)vec31.y * f1;
        float f4 = (float)vec31.z * f1;
        if (f5 > 0.0F) {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.75F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        if (f9 > 0.0F) {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.75F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        if (!this.minecraft.options.hideLightningFlash().get() && this.skyFlashTime > 0) {
            float f11 = (float)this.skyFlashTime - pPartialTick;
            if (f11 > 1.0F) {
                f11 = 1.0F;
            }

            f11 *= 0.45F;
            f2 = f2 * (1.0F - f11) + 0.8F * f11;
            f3 = f3 * (1.0F - f11) + 0.8F * f11;
            f4 = f4 * (1.0F - f11) + 1.0F * f11;
        }

        SkySetupEvent.ComputeSkyColor.Post postEvent = new SkySetupEvent.ComputeSkyColor.Post(f,f5,f9,pPartialTick,vec3,new Vec3((double)f2, (double)f3, (double)f4));
        MinecraftForge.EVENT_BUS.post(postEvent);
        return postEvent.getColor();
    }

    /**
     * @author Astemir
     * @reason Shadowing
     */
    @Overwrite(remap = false)
    public Vec3 getCloudColor(float pPartialTick) {
        float f = this.getTimeOfDay(pPartialTick);
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = this.getRainLevel(pPartialTick);
        if (f5 > 0.0F) {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.95F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        f2 *= f1 * 0.9F + 0.1F;
        f3 *= f1 * 0.9F + 0.1F;
        f4 *= f1 * 0.85F + 0.15F;
        float f9 = this.getThunderLevel(pPartialTick);
        if (f9 > 0.0F) {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.95F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }
        SkySetupEvent.ComputeCloudColor event = new SkySetupEvent.ComputeCloudColor(f,f5,f9,pPartialTick,new Vec3((double)f2, (double)f3, (double)f4));
        return event.getColor();
    }

    /**
     * @author Astemir
     * @reason Shadowing
     */
    @Overwrite(remap = false)
    public float getStarBrightness(float pPartialTick) {
        float f = this.getTimeOfDay(pPartialTick);
        float f2 = this.getRainLevel(pPartialTick);
        float f3 = this.getThunderLevel(pPartialTick);

        float f1 = 1.0F - (Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        SkySetupEvent.ComputeStarBrightness event = new SkySetupEvent.ComputeStarBrightness(f,f2,f3,pPartialTick,f1 * f1 * 0.5F);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getBrightness();
    }

}
