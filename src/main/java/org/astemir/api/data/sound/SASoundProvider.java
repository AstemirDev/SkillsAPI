package org.astemir.api.data.sound;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.data.IProvider;
import org.astemir.api.utils.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

public class SASoundProvider extends SoundDefinitionsProvider implements IProvider {

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

    public void addSound(SoundEvent soundEvent, SoundDefinition definition){
        soundsToRegister.put(soundEvent.getLocation().getPath(),definition);
    }

    public void addSound(RegistryObject<SoundEvent> soundEvent, SoundDefinition definition){
        soundsToRegister.put(soundEvent.get().getLocation().getPath(),definition);
    }


    public DataSound[] sounds(String... paths){
        DataSound[] res = new DataSound[paths.length];
        for (int i = 0; i < paths.length; i++) {
            res[i] = new DataSound(paths[i]);
        }
        return res;
    }

    public SoundDefinition createDefinition(DataSound... resources){
        SoundDefinition definition = definition();
        for (DataSound resource : resources) {
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
