package org.astemir.api.data.recipe;


import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.astemir.api.utils.ResourceUtils;

import java.util.function.Consumer;

public class RecipeHolder {

    private ItemLike result;
    private String customName = "";
    private String group = "";
    private int count = 1;
    private int cookingTime = 200;
    private float experience = 0.1f;

    public RecipeHolder(ItemLike result, int count) {
        this.result = result;
        this.count = count;
    }

    public RecipeHolder(ItemLike result) {
        this(result,1);
    }

    public RecipeHolder name(String name){
        this.customName = name;
        return this;
    }

    public RecipeHolder group(String group){
        this.group = group;
        return this;
    }

    public RecipeHolder experience(float experience){
        this.experience = experience;
        return this;
    }

    public RecipeHolder cookingTime(int time){
        this.cookingTime = time;
        return this;
    }


    public RecipeHolder shaped(Consumer<FinishedRecipe> consumer,RecipePattern pattern){
        ShapedRecipeBuilder newBuilder = new ShapedRecipeBuilder(result,count);
        pattern.build(newBuilder);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }


    public RecipeHolder shapeless(Consumer<FinishedRecipe> consumer,ItemLike... materials){
        ShapelessRecipeBuilder newBuilder = new ShapelessRecipeBuilder(result,count);
        for (ItemLike material : materials) {
            newBuilder = newBuilder.requires(material).unlockedBy(getHasName(material),has(material));
        }
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder stoneCutter(Consumer<FinishedRecipe> consumer,ItemLike material){
        SingleItemRecipeBuilder newBuilder = SingleItemRecipeBuilder.stonecutting(Ingredient.of(material),result,count);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder furnaceSmelting(Consumer<FinishedRecipe> consumer,ItemLike material){
        SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(material),result,experience,cookingTime);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder blastSmelting(Consumer<FinishedRecipe> consumer,ItemLike material){
        SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.blasting(Ingredient.of(material),result,experience,cookingTime);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder smokerCooking(Consumer<FinishedRecipe> consumer,ItemLike material){
        SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.smoking(Ingredient.of(material),result,experience,cookingTime);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder campfireCooking(Consumer<FinishedRecipe> consumer,ItemLike material){
        SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(material),result,experience,cookingTime);
        if (!group.isEmpty()){
            newBuilder = newBuilder.group(group);
        }
        if (customName.isEmpty()) {
            newBuilder.save(consumer);
        }else{
            newBuilder.save(consumer,customName);
        }
        return this;
    }

    public RecipeHolder smithTableUpgrading(Consumer<FinishedRecipe> consumer,ItemLike upgradableItem,ItemLike materialItem){
        UpgradeRecipeBuilder newBuilder = UpgradeRecipeBuilder.smithing(Ingredient.of(upgradableItem),Ingredient.of(materialItem),result.asItem());
        newBuilder = newBuilder.unlocks(getHasName(materialItem),has(materialItem));
        if (customName.isEmpty()) {
            newBuilder.save(consumer, ResourceUtils.getItemId(result.asItem()) + "_smithing");
        }else{
            newBuilder.save(consumer, customName);
        }
        return this;
    }


    public static String getHasName(ItemLike itemLike) {
        return "has_" + ResourceUtils.getItemId(itemLike.asItem());
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        ItemPredicate predicate = ItemPredicate.Builder.item().of(item).withCount(MinMaxBounds.Ints.exactly(1)).build();
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[]{predicate});
    }

}
