package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.gfx.GFXEffect;
import org.astemir.api.common.gfx.GFXEffectType;
import org.astemir.example.SkillsAPIMod;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PlayerEffectMessage {


    private GFXEffect gfxEffect;
    private boolean replace;


    public PlayerEffectMessage(GFXEffect effect,boolean replace) {
        this.gfxEffect = effect;
        this.replace = replace;
    }

    public static void encode(PlayerEffectMessage message, FriendlyByteBuf buf) {
        buf.writeEnum(message.gfxEffect.getEffectType());
        message.gfxEffect.write(buf);
        buf.writeBoolean(message.replace);
    }

    public static PlayerEffectMessage decode(FriendlyByteBuf buf) {
        GFXEffectType effectType = buf.readEnum(GFXEffectType.class);
        GFXEffect effectInstance = GFXEffectType.getEffectInstance(effectType);
        effectInstance.read(buf);
        PlayerEffectMessage message = new PlayerEffectMessage(effectInstance,buf.readBoolean());
        return message;
    }


    public static class Handler implements BiConsumer<PlayerEffectMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(PlayerEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                SkillsAPIMod.INSTANCE.GFX_EFFECT_HANDLER.addGFXEffect(message.gfxEffect,message.replace);
            });
            context.setPacketHandled(true);
        }
    }
}
