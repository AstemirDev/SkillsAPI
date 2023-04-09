package org.astemir.api.lib;

import net.minecraftforge.fml.ModList;

public abstract class ModLib {

    private String modId;

    private boolean enabled = false;

    public ModLib(String modId) {
        this.modId = modId;
    }

    public void load(){
        enabled = ModList.get().isLoaded(modId);
    }

    public String getModId() {
        return modId;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
