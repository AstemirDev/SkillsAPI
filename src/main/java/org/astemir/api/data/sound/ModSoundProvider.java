package org.astemir.api.data.sound;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.astemir.api.SkillsAPI;

import java.util.HashMap;
import java.util.Map;

public class ModSoundProvider extends SoundDefinitionsProvider {

    private Map<String,SoundDefinition> soundsToRegister = new HashMap<>();

    public ModSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, SkillsAPI.API.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        for (String name : soundsToRegister.keySet()) {
            add(name,soundsToRegister.get(name));
        }
    }

    public void register(DataGenerator gen) {
        gen.addProvider(true,this);
    }

    public void load(String name,SoundDefinition definition){
        soundsToRegister.put(name,definition);
    }

    public SoundDefinition createDefinition(SoundResource... resources){
        SoundDefinition definition = definition();
        for (SoundResource resource : resources) {
            ResourceLocation loc = SkillsAPI.resource(resource.getPath());
            SoundDefinition.Sound sound = sound(loc);
            if (resource.isPreload()){
                sound = sound.preload();
            }
            definition = definition.with(sound);
            if (!resource.getSubtitle().isEmpty()){
                definition = definition.subtitle(resource.getSubtitle());
            }
        }
        return definition;
    }

}
