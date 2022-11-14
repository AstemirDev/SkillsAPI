package org.astemir.api.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;

public class APIRecipeProvider extends RecipeProvider {

    public APIRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
    }


    public void bricks(Block result, ItemLike material, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result).define('#', material).pattern("##").pattern("##").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void bricksFour(Block result, ItemLike material, Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result,4).define('#', material).pattern("##").pattern("##").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void stairs(Block result,ItemLike material,Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result, 4).define('#', material).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void slab(Block result,ItemLike material,Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result, 6).define('#', material).pattern("###").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void trapdoor(Block result,ItemLike material,Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result, 2).define('#', material).pattern("###").pattern("###").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void sign(Block result,ItemLike material,Consumer<FinishedRecipe> consumer){
        ShapedRecipeBuilder.shaped(result, 3).group("sign").define('#', material).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ").unlockedBy("has_"+getHasName(material), has(material)).unlockedBy("has_stick",has(Items.STICK)).save(consumer);
    }

    public void fence(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result, 3).define('W', material).define('#', Items.STICK).pattern("W#W").pattern("W#W").unlockedBy("has_"+getHasName(material), has(material)).unlockedBy("has_stick",has(Items.STICK)).save(consumer);
    }

    public void fenceGateBuilder(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result).define('#', Items.STICK).define('W', material).pattern("#W#").pattern("#W#").unlockedBy("has_"+getHasName(material), has(material)).unlockedBy("has_stick",has(Items.STICK)).save(consumer);
    }

    public void pressurePlate(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        pressurePlate(consumer,result,material);
    }

    public void wall(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        wall(consumer,result,material);
    }

    public void door(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result, 3).define('#', material).pattern("##").pattern("##").pattern("##").unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }

    public void slabToBlock(Block result,ItemLike material,Consumer<FinishedRecipe> consumer) {
        chiseled(consumer,result,material);
    }

    public void smelt(ItemLike result,ItemLike material,Consumer<FinishedRecipe> consumer,float exp,int time){
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material), result, exp, time).unlockedBy("has_"+getHasName(material), has(material)).save(consumer);
    }



    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
