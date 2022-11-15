package org.astemir.api.data.provider;


import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.misc.DataItemModel;
import org.astemir.api.math.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.astemir.api.utils.ResourceUtils.*;


public class SAItemModelProvider extends ItemModelProvider {


    private List<Pair<Item, DataItemModel>> itemModels = new ArrayList<>();

    private String modId;

    public SAItemModelProvider(DataGenerator generator, String modId, ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        for (Pair<Item, DataItemModel> pair : itemModels) {
            switch (pair.getValue()){
                case GENERATED -> generatedItem(pair.getKey());
                case SPAWN_EGG -> spawnEgg(pair.getKey());
                case BLOCK_ITEM -> block(pair.getKey());
                case WALL_ITEM -> wall(pair.getKey());
            }
        }
    }

    public void addModel(Item item, DataItemModel type){
        this.itemModels.add(new Pair<>(item,type));
    }

    public void addModel(Supplier<Item> itemSupplier, DataItemModel type){
        this.itemModels.add(new Pair<>(itemSupplier.get(),type));
    }

    public ItemModelBuilder block(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile(modId,"block/"+location.getPath()));
    }

    public ItemModelBuilder wall(Item item){
        ResourceLocation location = getItemLocation(item);
        return getBuilder(location.toString())
                .parent(createModelFile(modId,"block/"+location.getPath()+"_inventory"));
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
