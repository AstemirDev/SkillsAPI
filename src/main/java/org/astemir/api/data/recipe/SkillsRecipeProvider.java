package org.astemir.api.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import org.astemir.api.data.IProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SkillsRecipeProvider extends RecipeProvider implements IProvider {

    private List<RecipeHolder> recipes = new ArrayList<>();

    public SkillsRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        for (RecipeHolder recipe : recipes) {
            recipe.getRecipeType().build(recipe,p_176532_);
        }
    }
    public void addRecipe(RecipeHolder recipe){
        recipes.add(recipe);
    }

    public RecipeHolder oneToOne(ItemLike result, int count, ItemLike material) {
        return shapeless(result,count,material);
    }

    public RecipeHolder fourBlock(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("##","##").put("#",material));
    }

    public RecipeHolder chiseled(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("#","#").put("#",material));
    }

    public RecipeHolder nineBlock(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("###","###","###").put("#",material));
    }

    public RecipeHolder chest(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("###","# #","###").put("#",material));
    }

    public RecipeHolder stairs(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("#  ","## ","###").put("#",material));
    }

    public RecipeHolder slab(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("###").put("#",material));
    }

    public RecipeHolder trapdoor(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("###","###").put("#",material));
    }

    public RecipeHolder boat(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("# #","###").put("#",material));
    }

    public RecipeHolder sign(ItemLike result, int count, ItemLike material, ItemLike stick){
        return shaped(result,count,new RecipePattern("###","###"," X ").put("#",material).put("X",stick)).group("sign");
    }

    public RecipeHolder fence(ItemLike result, int count, ItemLike material, ItemLike stick){
        return shaped(result,count,new RecipePattern("W#W","W#W").put("W",material).put("#",stick));
    }

    public RecipeHolder fenceGate(ItemLike result, int count, ItemLike material, ItemLike stick){
        return shaped(result,count,new RecipePattern("#W#","#W#").put("W",material).put("#",stick));
    }

    public RecipeHolder pressurePlate(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("##").put("#",material));
    }

    public RecipeHolder wall(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("###","###").put("#",material));
    }

    public RecipeHolder door(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("##","##","##").put("#",material));
    }

    public RecipeHolder slabToBlock(ItemLike result, int count, ItemLike material){
        return shaped(result,count,new RecipePattern("#").put("#",material));
    }

    public RecipeHolder stoneCut(ItemLike result, int count, ItemLike material){
        return new RecipeHolder(new RecipeType.StoneCutter(material),result,count);
    }

    public RecipeHolder upgrade(ItemLike result, ItemLike tool, ItemLike material){
        return new RecipeHolder(new RecipeType.SmithTable(tool,material),result,1);
    }

    public RecipeHolder campfire(ItemLike result, int count, ItemLike material, int time, float exp){
        return new RecipeHolder(new RecipeType.Campfire(material,time,exp),result,count);
    }

    public RecipeHolder shaped(ItemLike result, int count, RecipePattern pattern){
        return new RecipeHolder(new RecipeType.Shaped(pattern),result,count);
    }

    public RecipeHolder shapeless(ItemLike result, int count, ItemLike... materials){
        return new RecipeHolder(new RecipeType.Shapeless(materials),result,count);
    }

    public RecipeHolder smeltingOrCooking(ItemLike result, int count, ItemLike material, int cookingTime, float experience){
        return new RecipeHolder(new RecipeType.Furnace(material,cookingTime,experience),result,count);
    }

    public RecipeHolder cooking(ItemLike result, int count, ItemLike material, int cookingTime, float experience){
        return new RecipeHolder(new RecipeType.SmokerFurnace(material,cookingTime,experience),result,count);
    }

    public RecipeHolder smelting(ItemLike result, int count, ItemLike material, int cookingTime, float experience){
        return new RecipeHolder(new RecipeType.MetalFurnace(material,cookingTime,experience),result,count);
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
