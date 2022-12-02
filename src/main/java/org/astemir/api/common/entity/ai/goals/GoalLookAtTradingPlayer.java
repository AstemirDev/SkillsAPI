package org.astemir.api.common.entity.ai.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;

public class GoalLookAtTradingPlayer<T extends Mob & Merchant> extends LookAtPlayerGoal {

    private final T mob;

    public GoalLookAtTradingPlayer(T p_25538_) {
        super(p_25538_, Player.class, 8.0F);
        this.mob = p_25538_;
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null){
            return false;
        }
        if (this.mob.getTradingPlayer() != null) {
            this.lookAt = this.mob.getTradingPlayer();
            return true;
        } else {
            return false;
        }
    }
}
