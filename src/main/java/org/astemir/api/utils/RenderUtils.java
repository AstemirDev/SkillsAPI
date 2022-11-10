package org.astemir.api.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import org.astemir.api.SkillsAPI;

public class RenderUtils {



    public static void fillScreenWithTexture(PoseStack stack, ResourceLocation texture){
        Minecraft minecraft = Minecraft.getInstance();
        int x1 = 0, y1 = 0, x2 = minecraft.getWindow().getGuiScaledWidth(), y2 = minecraft.getWindow().getGuiScaledHeight();
        stack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1,1,1,0.025f);
        RenderSystem.setShaderTexture(0, texture);
        Gui.blit(stack, x1, y1, x1, y1, x2, y2, x2, y2);
        RenderSystem.disableBlend();
        RenderSystem.disableTexture();
        stack.popPose();
    }

    public static Material material(String path) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, SkillsAPI.resource(path));
    }
}