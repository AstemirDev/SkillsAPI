package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.example.SkillsAPIMod;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ScreenShakeMessage {


    private float shakePower;
    private int shakeTicks;


    public ScreenShakeMessage(float shakePower, int shakeTicks) {
        this.shakePower = shakePower;
        this.shakeTicks = shakeTicks;
    }

    public static void encode(ScreenShakeMessage message, FriendlyByteBuf buf) {
        buf.writeFloat(message.shakePower);
        buf.writeInt(message.shakeTicks);
    }

    public static ScreenShakeMessage decode(FriendlyByteBuf buf) {
        ScreenShakeMessage message = new ScreenShakeMessage(buf.readFloat(), buf.readInt());
        return message;
    }


    public static class Handler implements BiConsumer<ScreenShakeMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ScreenShakeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                SkillsAPIMod.INSTANCE.SCREEN_SHAKER.shake(message.shakeTicks,message.shakePower);
            });
            context.setPacketHandled(true);
        }
    }
}
