package org.astemir.api.data.recipe;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.astemir.api.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class RecipeType {

    public static class StoneCutter extends RecipeType {

        private ItemLike material;

        public StoneCutter(ItemLike material) {
            this.material = material;
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            SingleItemRecipeBuilder newBuilder = SingleItemRecipeBuilder.stonecutting(Ingredient.of(material),holder.getResult(),holder.getCount());
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            newBuilder = newBuilder.unlockedBy(getHasName(material), has(material));
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer,getConversionRecipeName(holder.getResult(),material)+"_stonecutting");
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public static class SmithTable extends RecipeType {

        private ItemLike upgradable;
        private ItemLike material;

        public SmithTable(ItemLike upgradable, ItemLike material) {
            this.upgradable = upgradable;
            this.material = material;
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            UpgradeRecipeBuilder newBuilder = UpgradeRecipeBuilder.smithing(Ingredient.of(upgradable),Ingredient.of(material),holder.getResult().asItem()).unlocks(getHasName(material),has(material));
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer, ResourceUtils.getItemId(holder.getResult().asItem()) + "_smithing");
            }else{
                newBuilder.save(consumer, holder.getCustomName());
            }
        }
    }


    public static class Campfire extends AbstractFurnaceRecipe{


        public Campfire(ItemLike material, int cookingTime, float experience) {
            super(material, cookingTime, experience);
        }

        public Campfire(ItemLike material) {
            super(material);
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(getMaterial()),holder.getResult(),getExperience(),getCookingTime()).unlockedBy(getHasName(getMaterial()),has(getMaterial()));
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public static class SmokerFurnace extends AbstractFurnaceRecipe{


        public SmokerFurnace(ItemLike material, int cookingTime, float experience) {
            super(material, cookingTime, experience);
        }

        public SmokerFurnace(ItemLike material) {
            super(material);
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.smoking(Ingredient.of(getMaterial()),holder.getResult(),getExperience(),getCookingTime()).unlockedBy(getHasName(getMaterial()),has(getMaterial()));
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public static class MetalFurnace extends AbstractFurnaceRecipe{


        public MetalFurnace(ItemLike material, int cookingTime, float experience) {
            super(material, cookingTime, experience);
        }

        public MetalFurnace(ItemLike material) {
            super(material);
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.blasting(Ingredient.of(getMaterial()),holder.getResult(),getExperience(),getCookingTime()).unlockedBy(getHasName(getMaterial()),has(getMaterial()));
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public static class Furnace extends AbstractFurnaceRecipe{


        public Furnace(ItemLike material, int cookingTime, float experience) {
            super(material, cookingTime, experience);
        }

        public Furnace(ItemLike material) {
            super(material);
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            SimpleCookingRecipeBuilder newBuilder = SimpleCookingRecipeBuilder.smelting(Ingredient.of(getMaterial()),holder.getResult(),getExperience(),getCookingTime()).unlockedBy(getHasName(getMaterial()),has(getMaterial()));
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public static class Shaped extends RecipeType {

        private RecipePattern pattern;

        public Shaped(RecipePattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder newBuilder = new ShapedRecipeBuilder(holder.getResult(),holder.getCount());
            pattern.build(newBuilder);
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }


    public static class Shapeless extends RecipeType {

        private ItemLike[] materials;

        public Shapeless(ItemLike... materials) {
            this.materials = materials;
        }

        @Override
        public void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer) {
            ShapelessRecipeBuilder newBuilder = new ShapelessRecipeBuilder(holder.getResult(),holder.getCount());
            List<String> criterionTags = new ArrayList<>();
            for (ItemLike material : materials) {
                String criterionName = getHasName(material);
                newBuilder = newBuilder.requires(material);
                if (!criterionTags.contains(criterionName)){
                    newBuilder = newBuilder.unlockedBy(criterionName,has(material));
                    criterionTags.add(criterionName);
                }
            }
            if (!holder.getGroup().isEmpty()){
                newBuilder = newBuilder.group(holder.getGroup());
            }
            if (holder.getCustomName().isEmpty()) {
                newBuilder.save(consumer);
            }else{
                newBuilder.save(consumer,holder.getCustomName());
            }
        }
    }

    public abstract static class AbstractFurnaceRecipe extends RecipeType {

        private ItemLike material;
        private int cookingTime = 200;
        private float experience = 0.1f;

        public AbstractFurnaceRecipe(ItemLike material,int cookingTime,float experience) {
            this.material = material;
            this.cookingTime = cookingTime;
            this.experience = experience;
        }

        public AbstractFurnaceRecipe(ItemLike material) {
            this.material = material;
        }

        public ItemLike getMaterial() {
            return material;
        }

        public int getCookingTime() {
            return cookingTime;
        }

        public float getExperience() {
            return experience;
        }
    }

    public abstract void build(RecipeHolder holder, Consumer<FinishedRecipe> consumer);


    protected static String getConversionRecipeName(ItemLike p_176518_, ItemLike p_176519_) {
        return ResourceUtils.getItemId(p_176518_.asItem()) + "_from_" + ResourceUtils.getItemId(p_176519_.asItem());
    }

    protected static String getSmeltingRecipeName(ItemLike p_176657_) {
        return ResourceUtils.getItemId(p_176657_.asItem()) + "_from_smelting";
    }

    protected static String getBlastingRecipeName(ItemLike p_176669_) {
        return ResourceUtils.getItemId(p_176669_.asItem()) + "_from_blasting";
    }

    public static String getHasName(ItemLike itemLike) {
        return "has_" + ResourceUtils.getItemId(itemLike.asItem());
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        ItemPredicate predicate = ItemPredicate.Builder.item().of(item).withCount(MinMaxBounds.Ints.exactly(1)).build();
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[]{predicate});
    }
}
