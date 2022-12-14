package org.astemir.api.data.lang;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.astemir.api.data.IProvider;

import java.util.HashMap;
import java.util.Map;

public class SALangProvider implements IProvider {

    private Map<String, DataLangDictionary> langs = new HashMap<>();

    private String modId;

    public SALangProvider(String modId) {
        this.modId = modId;
    }

    public DataLangDictionary addLang(String lang){
        DataLangDictionary res = new DataLangDictionary();
        langs.put(lang,res);
        return res;
    }

    public DataLangDictionary getOrAddLang(String lang){
        if (langs.containsKey(lang)){
            return langs.get(lang);
        }else{
            return addLang(lang);
        }
    }

    public void register(DataGenerator gen){
        langs.forEach((lang,data)->{
            LanguageProvider provider = new LanguageProvider(gen, modId,lang) {
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
