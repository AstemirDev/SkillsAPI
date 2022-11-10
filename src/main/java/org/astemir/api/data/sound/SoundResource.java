package org.astemir.api.data.sound;

public class SoundResource{

    private String path;
    private String subtitle = "";
    private boolean preload = false;

    public SoundResource(String path) {
        this.path = path;
    }

    public SoundResource subtitle(String subtitle){
        this.subtitle = subtitle;
        return this;
    }

    public SoundResource preload(){
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
