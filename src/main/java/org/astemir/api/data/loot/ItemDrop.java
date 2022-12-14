package org.astemir.api.data.loot;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class ItemDrop<T extends IDropParameters> {


    private ItemLike item;
    private UniformInt count;
    private T parameters;
    private float chance = 1;


    public ItemDrop(Item item) {
        this.item = item;
        this.count = UniformInt.of(1,1);
    }

    public ItemDrop count(int min,int max) {
        this.count = UniformInt.of(min,max);
        return this;
    }

    public ItemDrop chance(float chance){
        this.chance = chance;
        return this;
    }

    public ItemDrop parameters(T parameters) {
        this.parameters = parameters;
        return this;
    }

    public UniformInt getCount() {
        return count;
    }

    public T getParameters() {
        return parameters;
    }

    public float getChance() {
        return chance;
    }

    public ItemLike getItem() {
        return item;
    }
}

