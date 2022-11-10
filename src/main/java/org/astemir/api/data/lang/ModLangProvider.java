package org.astemir.api.data.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.astemir.api.SkillsAPI;

import java.util.HashMap;
import java.util.Map;

public class ModLangProvider {

    private Map<String, LanguageDictionary> langs = new HashMap<>();

    public LanguageDictionary addLang(String lang){
        LanguageDictionary res = new LanguageDictionary();
        langs.put(lang,res);
        return res;
    }

    public void register(DataGenerator gen){
        langs.forEach((lang,data)->{
            LanguageProvider provider = new LanguageProvider(gen, SkillsAPI.API.MOD_ID,lang) {
                @Override
                protected void addTranslations() {
                    data.getTranslations().forEach((key,translation)->{
                        add(key,translation);
                    });
                }
            };
            gen.addProvider(true,provider);
        });
    }
}
