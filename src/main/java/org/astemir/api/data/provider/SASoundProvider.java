package org.astemir.api.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.astemir.api.data.misc.DataSoundResource;
import org.astemir.api.utils.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

public class SASoundProvider extends SoundDefinitionsProvider {

    private Map<String,SoundDefinition> soundsToRegister = new HashMap<>();
    private String modId;

    public SASoundProvider(DataGenerator generator, String modId, ExistingFileHelper helper) {
        super(generator,modId , helper);
        this.modId = modId;
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

    public void addSound(String name,SoundDefinition definition){
        soundsToRegister.put(name,definition);
    }

    public SoundDefinition createDefinition(DataSoundResource... resources){
        SoundDefinition definition = definition();
        for (DataSoundResource resource : resources) {
            ResourceLocation loc = ResourceUtils.loadResource(modId,resource.getPath());
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