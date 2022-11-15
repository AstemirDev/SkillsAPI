package org.astemir.api.data.misc;

import net.minecraft.sounds.SoundEvent;

public class DataSoundResource {

    private String path;
    private String subtitle = "";
    private boolean preload = false;

    public DataSoundResource(String path) {
        this.path = path;
    }

    public DataSoundResource subtitle(String subtitle){
        this.subtitle = subtitle;
        return this;
    }

    public DataSoundResource preload(){
        this.preload = preload;
        return this;
    }

    public String getPath() {
        return path;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isPreload() {
        return preload;
    }
}
