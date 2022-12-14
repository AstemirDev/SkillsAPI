package org.astemir.api.common.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface ITamable {


    public boolean canAttackTarget(LivingEntity target,LivingEntity owner);

    public boolean isOrderedToSit();

    public boolean isTamed();

    public Player getOwner();
}
