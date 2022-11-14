package org.astemir.api.data.tag;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.astemir.api.math.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class APITagProvider<T> extends TagsProvider<T> {

    private List<Pair<T, TagKey<T>>> tags = new ArrayList<>();

    public APITagProvider(DataGenerator p_126546_, Registry p_126547_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126546_, p_126547_, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (Pair<T, TagKey<T>> tagPair : tags) {
            tag(tagPair.getValue()).add(tagPair.getKey());
        }
    }

    public void addTag(T object,TagKey<T> tag){
        tags.add(new Pair<>(object,tag));
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }
}
