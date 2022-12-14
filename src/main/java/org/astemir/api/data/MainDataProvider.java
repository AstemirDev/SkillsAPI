package org.astemir.api.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.blockstate.SABlockStateProvider;
import org.astemir.api.data.lang.SALangProvider;
import org.astemir.api.data.loot.SALootTableProvider;
import org.astemir.api.data.model.SAItemModelProvider;
import org.astemir.api.data.recipe.SARecipeProvider;
import org.astemir.api.data.sound.SASoundProvider;
import org.astemir.api.data.tag.SATagProvider;

public class MainDataProvider {

    private DataGenerator generator;
    private ExistingFileHelper existingFileHelper;
    private String modId;

    private SABlockStateProvider blockStateProvider;
    private SAItemModelProvider itemModelProvider;
    private SALangProvider langProvider;
    private SARecipeProvider recipeProvider;
    private SALootTableProvider lootTableProvider;
    private SASoundProvider soundProvider;

    private SATagProvider<Block> blockTagProvider;
    private SATagProvider<Item> itemTagProvider;


    public MainDataProvider(String modId, DataGenerator generator, ExistingFileHelper exFileHelper) {
        this.modId = modId;
        this.generator = generator;
        this.existingFileHelper = exFileHelper;
    }

    public SABlockStateProvider blockStateProvider(){
        if (this.blockStateProvider == null) {
            this.blockStateProvider = new SABlockStateProvider(generator, modId, existingFileHelper);
        }
        return blockStateProvider;
    }

    public SAItemModelProvider itemModelProvider(){
        if (this.itemModelProvider == null) {
            this.itemModelProvider = new SAItemModelProvider(generator, modId, existingFileHelper);
        }
        return itemModelProvider;
    }

    public SALangProvider langProvider(){
        if (this.langProvider == null) {
            this.langProvider = new SALangProvider(modId);
        }
        return langProvider;
    }

    public SARecipeProvider recipeProvider(){
        if (this.recipeProvider == null) {
            this.recipeProvider = new SARecipeProvider(generator);
        }
        return recipeProvider;
    }

    public SALootTableProvider lootTableProvider(){
        if (this.lootTableProvider == null) {
            this.lootTableProvider = new SALootTableProvider(generator);
        }
        return lootTableProvider;
    }

    public SASoundProvider soundProvider(){
        if (this.soundProvider == null) {
            this.soundProvider = new SASoundProvider(generator, modId, existingFileHelper);
        }
        return soundProvider;
    }


    public SATagProvider itemTagProvider(){
        if (this.itemTagProvider == null) {
            this.itemTagProvider = new SATagProvider(generator,Registry.ITEM,modId, existingFileHelper);
        }
        return itemTagProvider;
    }

    public SATagProvider blockTagProvider(){
        if (this.blockTagProvider == null) {
            this.blockTagProvider = new SATagProvider(generator,Registry.BLOCK,modId, existingFileHelper);
        }
        return blockTagProvider;
    }


    private void registerProvider(IProvider provider){
        if (provider != null){
            provider.register(generator);
        }
    }

    public void register(){
        registerProvider(blockStateProvider);
        registerProvider(itemModelProvider);
        registerProvider(langProvider);
        registerProvider(recipeProvider);
        registerProvider(lootTableProvider);
        registerProvider(soundProvider);
        registerProvider(itemTagProvider);
        registerProvider(blockTagProvider);
    }
}
