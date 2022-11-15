package org.astemir.api.data.misc.recipe;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

import java.util.*;

public class RecipePattern {

    private List<String> rows = new ArrayList<>();
    private Map<ItemLike,Character> charMap = new HashMap();

    public RecipePattern(String... lines) {
        this.rows = Arrays.asList(lines);
    }

    public RecipePattern put(String key, ItemLike item){
        charMap.put(item, key.charAt(0));
        return this;
    }

    public void build(ShapedRecipeBuilder builder){
        for (Map.Entry<ItemLike, Character> entry : charMap.entrySet()) {
            builder = builder.define(entry.getValue(),entry.getKey()).unlockedBy(DataRecipeType.getHasName(entry.getKey()), DataRecipeType.has(entry.getKey()));
        }
        for (String row : rows) {
            builder = builder.pattern(row);
        }
    }
}
