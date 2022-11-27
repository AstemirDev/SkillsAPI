package org.astemir.api.common.gfx;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.network.messages.PlayerEffectMessage;
import org.astemir.example.SkillsAPIMod;

import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerGFXEffectManager {

    public static final PlayerGFXEffectManager INSTANCE = new PlayerGFXEffectManager();

    private CopyOnWriteArrayList<GFXEffect> effects = new CopyOnWriteArrayList<>();

    public static void addGFXEffect(ServerPlayer player, GFXEffect effect,boolean replace){
        SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.PLAYER.with(()->player), new PlayerEffectMessage(effect,replace));
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


    public void addGFXEffect(GFXEffect effect,boolean replace){
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
