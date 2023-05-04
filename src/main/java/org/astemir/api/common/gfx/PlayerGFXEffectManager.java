package org.astemir.api.common.gfx;

import net.minecraft.server.level.ServerPlayer;
import org.astemir.api.network.messages.ClientMessagePlayerEffect;
import org.astemir.api.network.NetworkUtils;

import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerGFXEffectManager {

    public static final PlayerGFXEffectManager INSTANCE = new PlayerGFXEffectManager();

    private CopyOnWriteArrayList<GFXEffect> effects = new CopyOnWriteArrayList<>();

    public static void addEffect(ServerPlayer player, GFXEffect effect, boolean replace){
        NetworkUtils.sendToPlayer(player,new ClientMessagePlayerEffect(effect,replace));
    }

    public void update() {
        for (GFXEffect effect : effects) {
            if (effect.isRemoved()){
                effects.remove(effect);
            }else{
                effect.update();
            }
        }
    }


    public void addEffect(GFXEffect effect, boolean replace){
        if (replace) {
            for (GFXEffect gfxEffect : effects) {
                if (gfxEffect.getEffectType() == effect.getEffectType()){
                    effects.remove(gfxEffect);
                }
            }
        }
        effects.add(effect);
    }

    public static PlayerGFXEffectManager getInstance() {
        return INSTANCE;
    }

    public CopyOnWriteArrayList<GFXEffect> getEffects() {
        return effects;
    }
}
