package org.astemir.api.network.messages;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.HolderKey;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.action.ActionStateMachine;
import org.astemir.api.common.action.IActionListener;

import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageActionSync {


    private HolderKey holderKey;

    public ServerMessageActionSync(HolderKey key) {
        this.holderKey = key;
    }

    public static void encode(ServerMessageActionSync message, FriendlyByteBuf buf) {
        message.holderKey.write(buf);
    }

    public static ServerMessageActionSync decode(FriendlyByteBuf buf) {
        return new ServerMessageActionSync(HolderKey.read(buf));
    }


    public static class Handler implements BiConsumer<ServerMessageActionSync, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ServerMessageActionSync message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            LogicalSide side = context.getDirection().getReceptionSide();
            context.setPacketHandled(true);

            if(!side.isServer()) {
                return;
            }

            final ServerPlayer playerEntity = context.getSender();
            if(playerEntity == null) {
                return;
            }

            context.enqueueWork(() -> processMessage(message, playerEntity));
        }


        private static void processMessage(ServerMessageActionSync message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                ActionStateMachine machine = null;
                switch (message.holderKey.getTarget()){
                    case ENTITY:{
                        for (Entity entity : playerEntity.level.getEntities(playerEntity,playerEntity.getBoundingBox().inflate(100,100,100))) {
                            if (entity instanceof IActionListener){
                                IActionListener actionListener = (IActionListener)entity;
                                if (((Entity)actionListener).getUUID().equals(message.holderKey.getId())){
                                    machine = actionListener.getActionStateMachine();
                                }
                            }
                        }
                        break;
                    }
                    case BLOCK:{
                        BlockEntity blockEntity = playerEntity.level.getBlockEntity(message.holderKey.getPos());
                        if (blockEntity instanceof IActionListener){
                            machine = ((IActionListener) blockEntity).getActionStateMachine();
                        }
                    }
                }
                if (machine != null){
                    for (ActionController controller : machine.getControllers()) {
                        controller.sendUpdatePacket(playerEntity);
                    }
                }
            }
        }
    }
}
