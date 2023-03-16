package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.client.wrapper.SkillsWrapperItem;

import java.util.HashMap;
import java.util.Map;

public class SkillsRendererItem extends BlockEntityWithoutLevelRenderer {


    public static Map<Item, SkillsWrapperItem> itemModels = new HashMap<>();

    public static SkillsRendererItem INSTANCE;

    private BlockEntityRenderDispatcher dispatcher;

    private EntityModelSet models;


    public SkillsRendererItem(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher,models);
        this.dispatcher = dispatcher;
        this.models = models;
    }

    public static void addModel(Item item, SkillsWrapperItem wrapper){
        itemModels.put(item,wrapper);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
    }



    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        for (Item item : itemModels.keySet()) {
            if (itemStack.is(item)){
                SkillsWrapperItem wrapper = itemModels.get(item);
                wrapper.renderTarget = itemStack.getItem();
                wrapper.itemStack = itemStack;
                wrapper.transformType = transformType;
                wrapper.multiBufferSource = bufferSource;
                VertexConsumer consumer =  ItemRenderer.getFoilBufferDirect(bufferSource,wrapper.getRenderType(), false, itemStack.hasFoil());
                wrapper.renderItem(itemStack,transformType,poseStack,consumer,wrapper.getMultiBufferSource(),packedLight,packedOverlay,1,1,1,1);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        INSTANCE = new SkillsRendererItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(INSTANCE);
    }
}
