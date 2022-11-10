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
import org.astemir.api.client.model.ItemModelWrapper;
import org.astemir.api.common.animation.ITESRModel;

import java.util.HashMap;
import java.util.Map;

public class AdvancedItemRenderer extends BlockEntityWithoutLevelRenderer {


    public static Map<Item, ItemModelWrapper> itemModels = new HashMap<>();

    public static AdvancedItemRenderer INSTANCE;

    private BlockEntityRenderDispatcher dispatcher;

    private EntityModelSet models;


    public AdvancedItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher,models);
        this.dispatcher = dispatcher;
        this.models = models;
    }

    public static void addModel(Item item, ItemModelWrapper wrapper){
        itemModels.put(item,wrapper);
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_172555_) {
    }



    @Override
    public void renderByItem(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
        for (Item item : itemModels.keySet()) {
            if (p_108830_.is(item)){
                ItemModelWrapper wrapper = itemModels.get(item);
                wrapper.renderTarget = p_108830_.getItem();
                wrapper.itemStack = p_108830_;
                wrapper.transformType = p_108831_;
                wrapper.multiBufferSource = p_108833_;
                VertexConsumer consumer =  ItemRenderer.getFoilBufferDirect(p_108833_,wrapper.getRenderType(wrapper.getRenderTarget(),wrapper.getTexture((ITESRModel) wrapper.getRenderTarget())), false, p_108830_.hasFoil());
                wrapper.renderItem(p_108830_,p_108831_,p_108832_,consumer,wrapper.getMultiBufferSource(),p_108834_,p_108835_,1,1,1,1);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        INSTANCE = new AdvancedItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(INSTANCE);
    }
}
