package org.astemir.api.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.astemir.api.data.misc.recipe.DataRecipeHolder;
import org.astemir.api.data.misc.recipe.DataRecipeType;
import org.astemir.api.data.misc.recipe.RecipePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SARecipeProvider extends RecipeProvider {

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

    public DataRecipeHolder fourBlock(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("##","##").put("#",material)),result,count);
    }

    public DataRecipeHolder nineBlock(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("###","###","###").put("#",material)),result,count);
    }

    public DataRecipeHolder chest(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("###","# #","###").put("#",material)),result,count);
    }

    public DataRecipeHolder stairs(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("#  ","## ","###").put("#",material)),result,count);
    }

    public DataRecipeHolder slab(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("#  ","###").put("#",material)),result,count);
    }

    public DataRecipeHolder trapdoor(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("#","###","###").put("#",material)),result,count);
    }

    public DataRecipeHolder sign(ItemLike result,ItemLike material,ItemLike stick,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("###","###"," X ").put("#",material).put("X",stick)),result,count).group("sign");
    }

    public DataRecipeHolder fence(ItemLike result,ItemLike material,ItemLike stick,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("W#W","W#W").put("W",material).put("#",stick)),result,count);
    }

    public DataRecipeHolder fenceGate(ItemLike result,ItemLike material,ItemLike stick,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("#W#","#W#").put("W",material).put("#",stick)),result,count);
    }

    public DataRecipeHolder pressurePlate(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("##").put("#",material)),result,count);
    }

    public DataRecipeHolder wall(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("###","###").put("#",material)),result,count);
    }

    public DataRecipeHolder door(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("##","##","##").put("#",material)),result,count);
    }

    public DataRecipeHolder slabToBlock(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.Shaped(new RecipePattern("#").put("#",material)),result,count);
    }

    public DataRecipeHolder smelt(ItemLike result,ItemLike material,int time,float exp,int count){
        return new DataRecipeHolder(new DataRecipeType.Furnace(material,time,exp),result,count);
    }

    public DataRecipeHolder blast(ItemLike result,ItemLike material,int time,float exp,int count){
        return new DataRecipeHolder(new DataRecipeType.MetalFurnace(material,time,exp),result,count);
    }

    public DataRecipeHolder smoker(ItemLike result,ItemLike material,int time,float exp,int count){
        return new DataRecipeHolder(new DataRecipeType.SmokerFurnace(material,time,exp),result,count);
    }

    public DataRecipeHolder stoneCut(ItemLike result,ItemLike material,int count){
        return new DataRecipeHolder(new DataRecipeType.StoneCutter(material),result,count);
    }

    public DataRecipeHolder campfire(ItemLike result,ItemLike material,int time,float exp,int count){
        return new DataRecipeHolder(new DataRecipeType.Campfire(material,time,exp),result,count);
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
