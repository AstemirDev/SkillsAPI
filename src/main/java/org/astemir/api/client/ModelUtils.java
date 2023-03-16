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
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.render.SkillsRendererBlockEntity;
import org.astemir.api.client.render.SkillsRendererEntity;
import org.astemir.api.client.render.SkillsRendererLivingEntity;
import org.astemir.api.client.wrapper.SkillsWrapperBlockEntity;
import org.astemir.api.client.wrapper.SkillsWrapperEntity;
import org.astemir.api.client.wrapper.SkillsWrapperItem;
import org.astemir.api.common.misc.ICustomRendered;

public class ModelUtils {

    public static <T extends BlockEntity & ICustomRendered,M extends SkillsWrapperBlockEntity<T>> SkillsRendererBlockEntity<T> blockEntityRenderer(BlockEntityRendererProvider.Context context, M wrapper){
        return new SkillsRendererBlockEntity<>(context, wrapper);
    }

    public static <T extends Entity & ICustomRendered,M extends SkillsWrapperEntity<T>> SkillsRendererEntity<T,M> entityRenderer(EntityRendererProvider.Context context, M wrapper){
        return new SkillsRendererEntity<T, M>(context, wrapper) {
            @Override
            public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
                super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
            }
        };
    }

    public static <T extends LivingEntity & ICustomRendered,M extends SkillsWrapperEntity<T>> SkillsRendererLivingEntity<T,M> livingRenderer(EntityRendererProvider.Context context, M wrapper){
        return new SkillsRendererLivingEntity<>(context, wrapper);
    }

    public static <T extends Item & ICustomRendered> SkillsWrapperItem createItemModel(ResourceLocation modelPath, ResourceLocation texturePath){
        return new SkillsWrapperItem<T>() {

            public SkillsModel<T, DisplayArgumentItem> model = new SkillsModel<>(modelPath) {
                @Override
                public ResourceLocation getTexture(T target) {
                    return texturePath;
                }
            };

            @Override
            public SkillsModel<T, DisplayArgumentItem> getModel(T target) {
                return model;
            }
        };
    }
}
