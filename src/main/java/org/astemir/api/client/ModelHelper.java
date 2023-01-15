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
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.render.AdvancedRendererBlockEntity;
import org.astemir.api.client.render.AdvancedRendererEntity;
import org.astemir.api.client.render.AdvancedRendererLivingEntity;
import org.astemir.api.client.wrapper.AbstractModelWrapperBlockEntity;
import org.astemir.api.client.wrapper.AbstractModelWrapperEntity;
import org.astemir.api.client.wrapper.AbstractModelWrapperItem;
import org.astemir.api.common.animation.ISARendered;

public class ModelHelper {

    public static <T extends BlockEntity & ISARendered,M extends AbstractModelWrapperBlockEntity<T>> AdvancedRendererBlockEntity<T> blockEntityRenderer(BlockEntityRendererProvider.Context context, M wrapper){
        return new AdvancedRendererBlockEntity<>(context, wrapper);
    }

    public static <T extends Entity & ISARendered,M extends AbstractModelWrapperEntity<T>> AdvancedRendererEntity<T,M> entityRenderer(EntityRendererProvider.Context context, M wrapper){
        return new AdvancedRendererEntity<T, M>(context, wrapper) {
            @Override
            public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
                super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
            }
        };
    }

    public static <T extends LivingEntity & ISARendered,M extends AbstractModelWrapperEntity<T>> AdvancedRendererLivingEntity<T,M> livingRenderer(EntityRendererProvider.Context context, M wrapper){
        return new AdvancedRendererLivingEntity<>(context, wrapper);
    }

    public static <T extends Item & ISARendered> AbstractModelWrapperItem createItemModel(ResourceLocation modelPath, ResourceLocation texturePath){
        return new AbstractModelWrapperItem<T>() {

            public AdvancedModel<T> model = new AdvancedModel<>(modelPath) {
                @Override
                public ResourceLocation getTexture(T target) {
                    return texturePath;
                }
            };

            @Override
            public AdvancedModel<T> getModel(T target) {
                return model;
            }

        };
    }
}
