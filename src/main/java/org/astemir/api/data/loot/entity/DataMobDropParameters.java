package org.astemir.api.data.loot.entity;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import org.astemir.api.data.loot.IDropParameters;

public class DataMobDropParameters implements IDropParameters {

    private UniformInt looting;
    private boolean canBeCooked = false;
    private EntityType needToBeKilledBy;

    public DataMobDropParameters canBeCooked(){
        this.canBeCooked = true;
        return this;
    }

    public DataMobDropParameters looting(int minBonus, int maxBonus){
        this.looting = UniformInt.of(minBonus,maxBonus);
        return this;
    }

    public DataMobDropParameters killerCondition(EntityType killer){
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
