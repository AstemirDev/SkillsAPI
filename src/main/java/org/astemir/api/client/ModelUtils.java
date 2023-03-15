package org.astemir.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.display.DisplayArgumentItem;
import org.astemir.api.client.model.SunModel;
import org.astemir.api.client.render.SunRendererBlockEntity;
import org.astemir.api.client.render.SunRendererEntity;
import org.astemir.api.client.render.SunRendererLivingEntity;
import org.astemir.api.client.wrapper.MWBlockEntity;
import org.astemir.api.client.wrapper.MWEntity;
import org.astemir.api.client.wrapper.MWItem;
import org.astemir.api.common.misc.ICustomRendered;

public class ModelUtils {

    public static <T extends BlockEntity & ICustomRendered,M extends MWBlockEntity<T>> SunRendererBlockEntity<T> blockEntityRenderer(BlockEntityRendererProvider.Context context, M wrapper){
        return new SunRendererBlockEntity<>(context, wrapper);
    }

    public static <T extends Entity & ICustomRendered,M extends MWEntity<T>> SunRendererEntity<T,M> entityRenderer(EntityRendererProvider.Context context, M wrapper){
        return new SunRendererEntity<T, M>(context, wrapper) {
            @Override
            public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
                super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
            }
        };
    }

    public static <T extends LivingEntity & ICustomRendered,M extends MWEntity<T>> SunRendererLivingEntity<T,M> livingRenderer(EntityRendererProvider.Context context, M wrapper){
        return new SunRendererLivingEntity<>(context, wrapper);
    }

    public static <T extends Item & ICustomRendered> MWItem createItemModel(ResourceLocation modelPath, ResourceLocation texturePath){
        return new MWItem<T>() {

            public SunModel<T, DisplayArgumentItem> model = new SunModel<>(modelPath) {
                @Override
                public ResourceLocation getTexture(T target) {
                    return texturePath;
                }
            };

            @Override
            public SunModel<T, DisplayArgumentItem> getModel(T target) {
                return model;
            }
        };
    }
}
