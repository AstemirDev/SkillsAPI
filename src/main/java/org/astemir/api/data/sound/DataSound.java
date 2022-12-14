package org.astemir.api.data.sound;

public class DataSound {

    private String path;
    private String subtitle = "";
    private boolean preload = false;

    public DataSound(String path) {
        this.path = path;
    }

    public DataSound subtitle(String subtitle){
        this.subtitle = subtitle;
        return this;
    }

    public DataSound preload(){
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
