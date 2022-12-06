package org.astemir.api.data.samples;


import net.minecraft.nbt.Tag;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractDataSample<T> {


    private Map<String,String> translations = new HashMap<>();
    private List<TagKey> tags = new ArrayList<>();
    private T instance;

    public AbstractDataSample(T instance) {
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }

    public List<TagKey> getTags() {
        return tags;
    }

    public void addName(String lang,String name){
        this.translations.put(lang,name);
    }

    public void addTag(TagKey tag){
        if (!tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public Map<String, String> getTranslations() {
        return translations;
    }
}
