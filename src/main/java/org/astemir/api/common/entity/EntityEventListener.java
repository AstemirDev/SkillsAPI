package org.astemir.api.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.action.IActionListener;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.AnimationHandler;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.event.EntityTagEvent;
import org.astemir.api.common.event.EntityTickEvent;
import org.astemir.api.common.entity.ai.ICustomAIEntity;

public class EntityEventListener {

    @SubscribeEvent
    public static void onEntityAttributesLoad(EntityAttributeCreationEvent e){
        for (EntityType type : EntityProperties.getEntitySettings().keySet()) {
            if (!DefaultAttributes.hasSupplier(type)) {
                EntityProperties properties = EntityProperties.getProperties(type);
                if (properties.getAttributes() != null) {
                    e.put(type, (AttributeSupplier) properties.getAttributes().get());
                }
                if (properties.getPlacement() != null) {
                    EntityProperties.SpawnPlacement placement = properties.getPlacement();
                    SpawnPlacements.register(type, placement.getPlacementType(), placement.getHeightmapType(), placement.getPredicate());
                }
            }
        }
    }


    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent e){
        if (!e.getEntity().isRemoved()) {
            if (e.getEntity() instanceof ICustomAIEntity customAIEntity) {
                customAIEntity.getAISystem().update();
            }
            if (e.getEntity() instanceof IActionListener actionListener) {
                for (ActionController controller : actionListener.getActionStateMachine().getControllers()) {
                    controller.update();
                }
            }
        }
        if (e.getEntity() instanceof IAnimatedEntity entity){
            AnimationHandler.INSTANCE.updateAnimations(entity.getAnimationFactory());
        }
    }

    @SubscribeEvent
    public static void onEntityLoad(EntityTagEvent.Load e){
        if (e.getEntity() instanceof IActionListener actionListener){
            actionListener.getActionStateMachine().read(e.getCompoundTag());
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e){
        if (e.getEntity() instanceof ICustomAIEntity customAIEntity){
            customAIEntity.getAISystem().handleHurt(e.getSource(),e.getAmount());
        }
    }

    @SubscribeEvent
    public static void onEntitySave(EntityTagEvent.Save e){
        if (e.getEntity() instanceof IActionListener actionListener){
            actionListener.getActionStateMachine().write(e.getCompoundTag());
        }
    }

    @SubscribeEvent
    public static void onEntityAdded(EntityJoinLevelEvent e){
        if (e.getEntity() instanceof IAnimatedEntity) {
            AnimationFactory factory = ((IAnimatedEntity) e.getEntity()).getAnimationFactory();
            if (e.getLevel().isClientSide()){
                factory.syncClient();
            }else{
                factory.stopAll();
            }
        }
        if (e.getEntity() instanceof IActionListener){
            if (e.getLevel().isClientSide){
                ((IActionListener)e.getEntity()).syncClient();
            }
        }
    }
}
