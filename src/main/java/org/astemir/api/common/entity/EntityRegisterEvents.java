package org.astemir.api.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityRegisterEvents {

    @SubscribeEvent
    public static void onAttributesLoad(EntityAttributeCreationEvent e){
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
}
