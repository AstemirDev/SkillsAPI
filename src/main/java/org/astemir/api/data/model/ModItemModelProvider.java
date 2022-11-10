package org.astemir.api.data.model;


import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.SkillsAPI;
import org.astemir.api.math.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.astemir.api.utils.DataUtils.*;


public class ModItemModelProvider extends ItemModelProvider {


    private List<Pair<Item, ModelType>> itemModels = new ArrayList<>();

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SkillsAPI.API.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Pair<Item, ModelType> pair : itemModels) {
            switch (pair.getValue()){
                case GENERATED -> generatedItem(pair.getKey());
                case SPAWN_EGG -> spawnEgg(pair.getKey());
                case BLOCK_ITEM -> block(pair.getKey());
                case WALL_ITEM -> wall(pair.getKey());
            }
        }
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    public void load(Item item, ModelType type){
        this.itemModels.add(new Pair<>(item,type));
    }

    public void load(Supplier<Item> itemSupplier, ModelType type){
        this.itemModels.add(new Pair<>(itemSupplier.get(),type));
    }

    public ItemModelBuilder block(Item item){
        ResourceLocation location = getItemResourceLocation(item);
        return getBuilder(location.toString())
                .parent(modelFile(SkillsAPI.API.MOD_ID,"block/"+location.getPath()));
    }

    public ItemModelBuilder wall(Item item){
        ResourceLocation location = getItemResourceLocation(item);
        return getBuilder(location.toString())
                .parent(modelFile(SkillsAPI.API.MOD_ID,"block/"+location.getPath()+"_inventory"));
    }


    public ItemModelBuilder spawnEgg(Item item){
        ResourceLocation location = getItemResourceLocation(item);
        return getBuilder(location.toString())
                .parent(modelFile("item/template_spawn_egg"));
    }

    public ItemModelBuilder generatedItem(Item item) {
        ResourceLocation location = getItemResourceLocation(item);
        return getBuilder(location.toString())
                .parent(modelFile("item/generated"))
                .texture("layer0", getItemDefaultTextureLocation(location));
    }


    @Override
    @Deprecated
    public ItemModelBuilder basicItem(Item item) {
        return super.basicItem(item);
    }

    @Override
    @Deprecated
    public ItemModelBuilder basicItem(ResourceLocation item) {
        return super.basicItem(item);
    }

}
