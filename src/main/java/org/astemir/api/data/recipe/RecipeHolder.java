package org.astemir.api.data.recipe;



import net.minecraft.world.level.ItemLike;

public class RecipeHolder {

    private ItemLike result;
    private String customName = "";
    private String group = "";
    private int count = 1;
    private RecipeType recipeType;

    public RecipeHolder(RecipeType recipeType, ItemLike result, int count) {
        this.recipeType = recipeType;
        this.result = result;
        this.count = count;
    }

    public RecipeHolder(RecipeType recipeType, ItemLike result) {
        this(recipeType,result,1);
    }

    public RecipeHolder name(String name){
        this.customName = name;
        return this;
    }

    public RecipeHolder group(String group){
        this.group = group;
        return this;
    }

    public RecipeType getRecipeType() {
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
