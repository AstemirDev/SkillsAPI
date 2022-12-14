package org.astemir.api.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;
import org.astemir.api.data.IProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SARecipeProvider extends RecipeProvider implements IProvider {

    private List<DataRecipeHolder> recipes = new ArrayList<>();

    public SARecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        for (DataRecipeHolder recipe : recipes) {
            recipe.getRecipeType().build(recipe,p_176532_);
        }
    }
    public void addRecipe(DataRecipeHolder recipe){
        recipes.add(recipe);
    }

    public DataRecipeHolder oneToOne(ItemLike result,int count,ItemLike material) {
        return shapeless(result,count,material);
    }

    public DataRecipeHolder fourBlock(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("##","##").put("#",material));
    }

    public DataRecipeHolder chiseled(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("#","#").put("#",material));
    }

    public DataRecipeHolder nineBlock(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("###","###","###").put("#",material));
    }

    public DataRecipeHolder chest(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("###","# #","###").put("#",material));
    }

    public DataRecipeHolder stairs(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("#  ","## ","###").put("#",material));
    }

    public DataRecipeHolder slab(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("###").put("#",material));
    }

    public DataRecipeHolder trapdoor(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("###","###").put("#",material));
    }

    public DataRecipeHolder boat(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("# #","###").put("#",material));
    }

    public DataRecipeHolder sign(ItemLike result,int count,ItemLike material,ItemLike stick){
        return shaped(result,count,new DataRecipePattern("###","###"," X ").put("#",material).put("X",stick)).group("sign");
    }

    public DataRecipeHolder fence(ItemLike result,int count,ItemLike material,ItemLike stick){
        return shaped(result,count,new DataRecipePattern("W#W","W#W").put("W",material).put("#",stick));
    }

    public DataRecipeHolder fenceGate(ItemLike result,int count,ItemLike material,ItemLike stick){
        return shaped(result,count,new DataRecipePattern("#W#","#W#").put("W",material).put("#",stick));
    }

    public DataRecipeHolder pressurePlate(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("##").put("#",material));
    }

    public DataRecipeHolder wall(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("###","###").put("#",material));
    }

    public DataRecipeHolder door(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("##","##","##").put("#",material));
    }

    public DataRecipeHolder slabToBlock(ItemLike result,int count,ItemLike material){
        return shaped(result,count,new DataRecipePattern("#").put("#",material));
    }

    public DataRecipeHolder stoneCut(ItemLike result,int count,ItemLike material){
        return new DataRecipeHolder(new DataRecipeType.StoneCutter(material),result,count);
    }

    public DataRecipeHolder upgrade(ItemLike result,ItemLike tool,ItemLike material){
        return new DataRecipeHolder(new DataRecipeType.SmithTable(tool,material),result,1);
    }

    public DataRecipeHolder campfire(ItemLike result,int count,ItemLike material,int time,float exp){
        return new DataRecipeHolder(new DataRecipeType.Campfire(material,time,exp),result,count);
    }

    public DataRecipeHolder shaped(ItemLike result, int count, DataRecipePattern pattern){
        return new DataRecipeHolder(new DataRecipeType.Shaped(pattern),result,count);
    }

    public DataRecipeHolder shapeless(ItemLike result,int count,ItemLike... materials){
        return new DataRecipeHolder(new DataRecipeType.Shapeless(materials),result,count);
    }

    public DataRecipeHolder smeltingOrCooking(ItemLike result, int count, ItemLike material,int cookingTime,float experience){
        return new DataRecipeHolder(new DataRecipeType.Furnace(material,cookingTime,experience),result,count);
    }

    public DataRecipeHolder cooking(ItemLike result, int count, ItemLike material,int cookingTime,float experience){
        return new DataRecipeHolder(new DataRecipeType.SmokerFurnace(material,cookingTime,experience),result,count);
    }

    public DataRecipeHolder smelting(ItemLike result, int count, ItemLike material,int cookingTime,float experience){
        return new DataRecipeHolder(new DataRecipeType.MetalFurnace(material,cookingTime,experience),result,count);
    }


    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
