package org.astemir.api.client.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import org.astemir.api.io.ResourceUtils;
import org.astemir.api.math.components.Color;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    public static void renderCube(float x,float y,float z,float width,float height,float depth,Color color){
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(x, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, 0.5D, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, 0.5D, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y+height, z).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y+height, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x+width, y, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(x, y, z+depth).color(color.r, color.g, color.b, color.a).endVertex();
        tesselator.end();
    }
    public static void renderEntityInInventory(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        float f = (float)Math.atan((double)(pMouseX / 40.0F));
        float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
        renderEntityInInventoryRaw(pPosX, pPosY, pScale, f, f1, pLivingEntity);
    }

    public static void renderEntityInInventoryRaw(int pPosX, int pPosY, int pScale, float angleXComponent, float angleYComponent, LivingEntity pLivingEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)pPosX, (double)pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)pScale, (float)pScale, (float)pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        float f2 = pLivingEntity.yBodyRot;
        float f3 = pLivingEntity.getYRot();
        float f4 = pLivingEntity.getXRot();
        float f5 = pLivingEntity.yHeadRotO;
        float f6 = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;
        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pLivingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        pLivingEntity.yBodyRot = f2;
        pLivingEntity.setYRot(f3);
        pLivingEntity.setXRot(f4);
        pLivingEntity.yHeadRotO = f5;
        pLivingEntity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public static void fillColor(PoseStack poseStack, int x, int y, int width, int height, Color color){
        Matrix4f matrix4f = poseStack.last().pose();
        int pMinX = x;
        int pMinY = y;
        int pMaxX = x+width;
        int pMaxY = y+height;
        if (pMinX < pMaxX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }
        if (pMinY < pMaxY) {
            int j = pMinY;
            pMinY = pMaxY;
            pMaxY = j;
        }
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrix4f, (float)pMinX, (float)pMaxY, 0.0F).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)pMaxY, 0.0F).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMaxX, (float)pMinY, 0.0F).color(color.r, color.g, color.b, color.a).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pMinX, (float)pMinY, 0.0F).color(color.r, color.g, color.b, color.a).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }


    public static void fillScreenWithTexture(PoseStack stack, ResourceLocation texture,float r,float g,float b,float a){
        Minecraft minecraft = Minecraft.getInstance();
        int x1 = 0, y1 = 0, x2 = minecraft.getWindow().getGuiScaledWidth(), y2 = minecraft.getWindow().getGuiScaledHeight();
        stack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(r,g,b,a);
        RenderSystem.setShaderTexture(0, texture);
        Gui.blit(stack, x1, y1, x1, y1, x2, y2, x2, y2);
        RenderSystem.disableBlend();
        RenderSystem.disableTexture();
        stack.popPose();
    }

    public static Material material(String modId,String path) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, ResourceUtils.loadResource(modId,path));
    }
}