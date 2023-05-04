package org.astemir.api.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.render.ISkillsRenderer;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.event.SkyRenderEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = {LevelRenderer.class},priority = 500)
public abstract class MixinLevelRenderer {
    @Shadow
    @Nullable
    private ClientLevel level;
    @Shadow
    private int ticks;

    @Shadow
    protected abstract boolean doesMobEffectBlockSky(Camera p_234311_);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void renderEndSky(PoseStack pPoseStack);

    @Shadow
    @Nullable
    private VertexBuffer skyBuffer;
    @Shadow
    @Final
    private static ResourceLocation SUN_LOCATION;
    @Shadow
    @Final
    private static ResourceLocation MOON_LOCATION;
    @Shadow
    @Nullable
    private VertexBuffer starBuffer;
    @Shadow
    @Nullable
    private VertexBuffer darkBuffer;

    @Shadow
    public abstract boolean shouldShowEntityOutlines();

    @Shadow
    @Nullable
    private RenderTarget entityTarget;


    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    /**
     * @author
     * @reason handle
     */
    @Inject(method = "renderEntity", at = @At("HEAD"), remap = SkillsAPI.REMAP)
    public void onRenderEntity(Entity pEntity, double pCamX, double pCamY, double pCamZ, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, CallbackInfo ci) {
        EntityRenderer renderer = this.entityRenderDispatcher.getRenderer(pEntity);
        if (renderer instanceof ISkillsRenderer iSkillsRenderer) {
            if (pEntity instanceof ICustomRendered) {
                iSkillsRenderer.animate((ICustomRendered) pEntity, pPartialTick);
            }
        }
    }

    /**
     * @author
     * @reason Because
     */
    @Overwrite(remap = SkillsAPI.REMAP)
    public void renderSky(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, boolean isFoggy, Runnable pSkyFogSetup) {
        if (level.effects().renderSky(level, ticks, pPartialTick, pPoseStack, pCamera, pProjectionMatrix, isFoggy, pSkyFogSetup))
            return;
        pSkyFogSetup.run();
        if (!isFoggy) {
            FogType fogtype = pCamera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !this.doesMobEffectBlockSky(pCamera)) {
                if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END) {
                    this.renderEndSky(pPoseStack);
                } else if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                    RenderSystem.disableTexture();
                    Vec3 skyColor = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), pPartialTick);
                    setupFogColor();
                    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                    renderSkybox(pPoseStack,pProjectionMatrix,skyColor);
                    renderSunrise(bufferbuilder,pPoseStack,pPartialTick);
                    RenderSystem.enableTexture();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    pPoseStack.pushPose();
                    SkyRenderEvent event = new SkyRenderEvent(pPoseStack, this.level.getTimeOfDay(pPartialTick), level.getRainLevel(pPartialTick), pPartialTick);
                    MinecraftForge.EVENT_BUS.post(event);
                    RenderSystem.setShaderColor(event.getColor().r, event.getColor().g, event.getColor().b, event.getColor().a);
                    pPoseStack.mulPose(Vector3f.YP.rotationDegrees(event.getYRot()));
                    pPoseStack.mulPose(Vector3f.XP.rotationDegrees(event.getXRot()));
                    Matrix4f matrix4f1 = pPoseStack.last().pose();
                    renderSun(bufferbuilder,matrix4f1,event);
                    renderMoon(bufferbuilder,matrix4f1,event);
                    renderStars(pPoseStack,pProjectionMatrix,pPartialTick,event,pSkyFogSetup);
                    renderDarkness(pPoseStack,pProjectionMatrix,pPartialTick);
                    if (this.level.effects().hasGround()) {
                        RenderSystem.setShaderColor((float)skyColor.x * 0.2F + 0.04F, (float)skyColor.y * 0.2F + 0.04F, (float)skyColor.z * 0.6F + 0.1F, 1.0F);
                    } else {
                        RenderSystem.setShaderColor((float)skyColor.x,(float) skyColor.y, (float)skyColor.z, 1.0F);
                    }
                    RenderSystem.enableTexture();
                    RenderSystem.depthMask(true);
                }
            }
        }
    }

    private void setupFogColor(){
        FogRenderer.levelFogColor();
    }

    private void renderSkybox(PoseStack pPoseStack,Matrix4f pProjectionMatrix,Vec3 skyColor){
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor((float)skyColor.x,(float)skyColor.y, (float)skyColor.z, 1.0F);
        ShaderInstance shaderinstance = RenderSystem.getShader();
        this.skyBuffer.bind();
        this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
        VertexBuffer.unbind();
    }

    private void renderSunrise(BufferBuilder bufferbuilder,PoseStack pPoseStack,float pPartialTick){
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] color = this.level.effects().getSunriseColor(this.level.getTimeOfDay(pPartialTick), pPartialTick);
        if (color != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            float f3 = Mth.sin(this.level.getSunAngle(pPartialTick)) < 0.0F ? 180.0F : 0.0F;
            pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(f3));
            pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            float red = color[0];
            float green = color[1];
            float blue = color[2];
            float alpha = color[3];
            Matrix4f matrix4f = pPoseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(red, green, blue, alpha).endVertex();
            for (int j = 0; j <= 16; ++j) {
                float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f8 = Mth.sin(f7);
                float f9 = Mth.cos(f7);
                bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * alpha).color(red, green, blue, 0.0F).endVertex();
            }
            BufferUploader.drawWithShader(bufferbuilder.end());
            pPoseStack.popPose();
        }
    }

    private void renderDarkness(PoseStack pPoseStack,Matrix4f pProjectionMatrix,float pPartialTick){
        ShaderInstance shaderinstance = RenderSystem.getShader();
        pPoseStack.popPose();
        RenderSystem.disableTexture();
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        double darkness = this.minecraft.player.getEyePosition(pPartialTick).y - this.level.getLevelData().getHorizonHeight(this.level);
        if (darkness < 0.0D) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.0D, 12.0D, 0.0D);
            this.darkBuffer.bind();
            this.darkBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
            VertexBuffer.unbind();
            pPoseStack.popPose();
        }
    }


    private void renderSun(BufferBuilder bufferbuilder,Matrix4f matrix4f1,SkyRenderEvent event) {
        float sunRadius = event.getSunSize();
        float verticalOffset = event.getSunVerticalOffset();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SUN_LOCATION);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -sunRadius, verticalOffset, -sunRadius).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, sunRadius, verticalOffset, -sunRadius).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, sunRadius, verticalOffset, sunRadius).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -sunRadius, verticalOffset, sunRadius).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    private void renderMoon(BufferBuilder bufferbuilder, Matrix4f matrix4f1,SkyRenderEvent event) {
        RenderSystem.setShaderTexture(0, MOON_LOCATION);
        int k = this.level.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float) (l + 0) / 4.0F;
        float f14 = (float) (i1 + 0) / 2.0F;
        float f15 = (float) (l + 1) / 4.0F;
        float f16 = (float) (i1 + 1) / 2.0F;
        float moonRadius = event.getMoonSize();
        float verticalOffset = event.getMoonVerticalOffset();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -moonRadius, verticalOffset, moonRadius).uv(f15, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, moonRadius, verticalOffset, moonRadius).uv(f13, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, moonRadius, verticalOffset, -moonRadius).uv(f13, f14).endVertex();
        bufferbuilder.vertex(matrix4f1, -moonRadius, verticalOffset, -moonRadius).uv(f15, f14).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableTexture();
    }

    private void renderStars(PoseStack pPoseStack,Matrix4f pProjectionMatrix,float pPartialTick,SkyRenderEvent event,Runnable pSkyFogSetup){
        float brightness = this.level.getStarBrightness(pPartialTick) * event.getColor().a;
        if (brightness > 0.0F) {
            RenderSystem.setShaderColor(brightness, brightness, brightness, brightness);
            FogRenderer.setupNoFog();
            this.starBuffer.bind();
            this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            pSkyFogSetup.run();
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

}