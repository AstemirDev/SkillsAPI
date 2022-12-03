package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.gfx.GFXEffect;
import org.astemir.api.common.gfx.GFXEffectType;
import org.astemir.api.common.gfx.PlayerGFXEffectManager;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientMessagePlayerEffect {


    private GFXEffect gfxEffect;
    private boolean replace;


    public ClientMessagePlayerEffect(GFXEffect effect, boolean replace) {
        this.gfxEffect = effect;
        this.replace = replace;
    }

    public static void encode(ClientMessagePlayerEffect message, FriendlyByteBuf buf) {
        buf.writeEnum(message.gfxEffect.getEffectType());
        message.gfxEffect.write(buf);
        buf.writeBoolean(message.replace);
    }

    public static ClientMessagePlayerEffect decode(FriendlyByteBuf buf) {
        GFXEffectType effectType = buf.readEnum(GFXEffectType.class);
        GFXEffect effectInstance = GFXEffectType.getEffectInstance(effectType);
        effectInstance.read(buf);
        ClientMessagePlayerEffect message = new ClientMessagePlayerEffect(effectInstance,buf.readBoolean());
        return message;
    }


    public static class Handler implements BiConsumer<ClientMessagePlayerEffect, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ClientMessagePlayerEffect message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                PlayerGFXEffectManager.getInstance().addGFXEffect(message.gfxEffect,message.replace);
            });
            context.setPacketHandled(true);
        }
    }
}
