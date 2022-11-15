package org.astemir.api.data.misc.recipe;



import net.minecraft.world.level.ItemLike;

import javax.xml.crypto.Data;

public class DataRecipeHolder {

    private ItemLike result;
    private String customName = "";
    private String group = "";
    private int count = 1;
    private DataRecipeType recipeType;

    public DataRecipeHolder(DataRecipeType recipeType,ItemLike result, int count) {
        this.recipeType = recipeType;
        this.result = result;
        this.count = count;
    }

    public DataRecipeHolder(DataRecipeType recipeType,ItemLike result) {
        this(recipeType,result,1);
    }

    public DataRecipeHolder name(String name){
        this.customName = name;
        return this;
    }

    public DataRecipeHolder group(String group){
        this.group = group;
        return this;
    }

    public DataRecipeType getRecipeType() {
        return recipeType;
    }

    public ItemLike getResult() {
        return result;
    }

    public String getCustomName() {
        return customName;
    }

    public int getCount() {
        return count;
    }

    public String getGroup() {
        return group;
    }
}
