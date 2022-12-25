package org.astemir.api.data.loot.entity;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import org.astemir.api.data.loot.IDropParameters;

public class MobDropParameters implements IDropParameters {

    private UniformInt looting;
    private boolean canBeCooked = false;
    private EntityType needToBeKilledBy;

    public MobDropParameters canBeCooked(){
        this.canBeCooked = true;
        return this;
    }

    public MobDropParameters looting(int minBonus, int maxBonus){
        this.looting = UniformInt.of(minBonus,maxBonus);
        return this;
    }

    public MobDropParameters killerCondition(EntityType killer){
        this.needToBeKilledBy = killer;
        return this;
    }

    public boolean isCanBeCooked() {
        return canBeCooked;
    }

    public EntityType getNeedToBeKilledBy() {
        return needToBeKilledBy;
    }

    public UniformInt getLooting() {
        return looting;
    }
}
