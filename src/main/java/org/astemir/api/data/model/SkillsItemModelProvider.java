package org.astemir.api.data.model;


import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.IProvider;
import org.astemir.api.math.collection.Couple;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.astemir.api.utils.ResourceUtils.*;


public class SkillsItemModelProvider extends ItemModelProvider implements IProvider {


    private List<Couple<Item, ItemModel>> itemModels = new ArrayList<>();

    private String modId;

    public SkillsItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        for (Couple<Item, ItemModel> pair : itemModels) {
            switch (pair.getValue()){
                case GENERATED -> generatedItem(pair.getKey());
                case HANDHELD -> handheld(pair.getKey());
                case SPAWN_EGG -> spawnEgg(pair.getKey());
                case BLOCK_ITEM,FENCE_GATE_ITEM -> block(pair.getKey());
                case TRAPDOOR_ITEM -> trapdoor(pair.getKey());
                case TALL_GRASS -> tallGrass(pair.getKey());
                case GRASS -> grass(pair.getKey());
                case WALL_ITEM, FENCE_ITEM,BUTTON_ITEM -> blockInventory(pair.getKey());
            }
        }
    }

    public void addModel(Item item, ItemModel type){
        this.itemModels.add(new Couple<>(item,type));
    }

    public void addModel(Supplier<Item> itemSupplier, ItemModel type){
        this.itemModels.add(new Couple<>(itemSupplier.get(),type));
    }

    public ItemModelBuilder block(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile(modId,"block/"+location.getPath()));
    }

    public ItemModelBuilder blockInventory(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile(modId,"block/"+location.getPath()+"_inventory"));
    }

    public ItemModelBuilder trapdoor(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile(modId,"block/"+location.getPath()+"_bottom"));
    }

    public ItemModelBuilder tallGrass(Item item){
        ResourceLocation location = getItemLocation(item);
        ResourceLocation blockTexture = getBlockTexture(((BlockItem)item).getBlock());
        return getBuilder(location.toString())
                .parent(createModelFile("item/generated"))
                .texture("layer0",blockTexture+"_top");
    }

    public ItemModelBuilder grass(Item item){
        ResourceLocation location = getItemLocation(item);
        ResourceLocation blockTexture = getBlockTexture(((BlockItem)item).getBlock());
        return getBuilder(location.toString())
                .parent(createModelFile("item/generated"))
                .texture("layer0",blockTexture);
    }



    public ItemModelBuilder spawnEgg(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile("item/template_spawn_egg"));
    }

    public ItemModelBuilder generatedItem(Item item) {
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile("item/generated"))
                .texture("layer0", getItemTexture(location));
    }

    public ItemModelBuilder handheld(Item item) {
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile("item/handheld"))
                .texture("layer0", getItemTexture(location));
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

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
