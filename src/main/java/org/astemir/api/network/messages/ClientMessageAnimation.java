package org.astemir.api.network.messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientMessageAnimation {

    private Action action;
    private HolderKey holderKey;
    private int animationId;
    private int tick = 0;

    public ClientMessageAnimation(HolderKey targetId, Action action, int animationId, int tick) {
        this.holderKey = targetId;
        this.action = action;
        this.animationId = animationId;
        this.tick = tick;
    }


    public static void encode(ClientMessageAnimation message, FriendlyByteBuf buf) {
        message.holderKey.write(buf);
        buf.writeEnum(message.action);
        buf.writeInt(message.animationId);
        buf.writeInt(message.tick);
    }

    public static ClientMessageAnimation decode(FriendlyByteBuf buf) {
        HolderKey key = HolderKey.read(buf);
        return new ClientMessageAnimation(key, buf.readEnum(Action.class), buf.readInt(),buf.readInt());
    }



    public static class Handler implements BiConsumer<ClientMessageAnimation, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ClientMessageAnimation message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                AnimationFactory factory = null;
                switch (message.holderKey.getTarget()){
                    case ENTITY:{
                        if (Minecraft.getInstance().level.getEntity(message.holderKey.getId()) instanceof IAnimated animatedEntity){
                            factory = animatedEntity.getAnimationFactory();
                        }
                        break;
                    }
                    case BLOCK:{
                        BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(message.holderKey.getPos());
                        if (blockEntity instanceof IAnimated){
                            factory = ((IAnimated)blockEntity).getAnimationFactory();
                        }
                    }
                }
                if (factory != null) {
                    switch (message.action) {
                        case START -> {
                            Animation animation = factory.getAnimationList().getAnimation(message.animationId);
                            factory.setAnimation(animation,message.tick);
                            break;
                        }
                        case STOP -> {
                            Animation animation = factory.getAnimationList().getAnimation(message.animationId);
                            factory.removeAnimation(animation);
                            break;
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }

    public enum Action{
        STOP,START
    }
}
