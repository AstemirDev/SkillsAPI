package org.astemir.api.data.tag;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.data.IProvider;
import org.astemir.api.math.collection.Couple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SkillsTagProvider<T> extends TagsProvider<T> implements IProvider {

    private List<Couple<T, TagKey<T>>> tags = new ArrayList<>();

    public SkillsTagProvider(DataGenerator p_126546_, Registry p_126547_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126546_, p_126547_, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (Couple<T, TagKey<T>> tagPair : tags) {
            tag(tagPair.getValue()).add(tagPair.getKey());
        }
    }

    public void addTag(Supplier<T> object, TagKey<T>... tags){
        addTag(object.get(),tags);
    }

    public void addTag(T object,TagKey<T>... tags){
        for (TagKey<T> tag : tags) {
            this.tags.add(new Couple<>(object,tag));
        }
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
