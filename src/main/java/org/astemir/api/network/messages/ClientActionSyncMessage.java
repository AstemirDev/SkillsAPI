package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.common.state.IActionListener;
import org.astemir.api.network.AnimationTarget;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ClientActionSyncMessage {

    private UUID uuid;
    private BlockPos pos;
    private ActionControllerMessage.Type type;

    public ClientActionSyncMessage(UUID uuid) {
        this.uuid = uuid;
        this.type = ActionControllerMessage.Type.ENTITY;
    }

    public ClientActionSyncMessage(BlockPos pos) {
        this.pos = pos;
        this.type = ActionControllerMessage.Type.BLOCK;
    }

    public static void encode(ClientActionSyncMessage message, FriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        if (message.type == ActionControllerMessage.Type.ENTITY) {
            buf.writeUUID(message.uuid);
        }else
        if (message.type == ActionControllerMessage.Type.BLOCK) {
            buf.writeBlockPos(message.pos);
        }
    }

    public static ClientActionSyncMessage decode(FriendlyByteBuf buf) {
        ActionControllerMessage.Type type = buf.readEnum(ActionControllerMessage.Type.class);
        if (type == ActionControllerMessage.Type.ENTITY) {
            return new ClientActionSyncMessage(buf.readUUID());
        }else
        if (type == ActionControllerMessage.Type.BLOCK) {
            return new ClientActionSyncMessage(buf.readBlockPos());
        }
        return null;
    }


    public static class Handler implements BiConsumer<ClientActionSyncMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ClientActionSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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


        private static void processMessage(ClientActionSyncMessage message, ServerPlayer playerEntity) {
            if (playerEntity != null) {
                ActionStateMachine machine = null;
                switch (message.type){
                    case ENTITY:{
                        for (Entity entity : playerEntity.level.getEntities(playerEntity,playerEntity.getBoundingBox().inflate(100,100,100))) {
                            if (entity instanceof IActionListener){
                                IActionListener actionListener = (IActionListener)entity;
                                if (((Entity)actionListener).getUUID().equals(message.uuid)){
                                    machine = actionListener.getActionStateMachine();
                                }
                            }
                        }
                        break;
                    }
                    case BLOCK:{
                        BlockEntity blockEntity = playerEntity.level.getBlockEntity(message.pos);
                        if (blockEntity instanceof IActionListener){
                            machine = ((IActionListener) blockEntity).getActionStateMachine();
                        }
                    }
                }
                if (machine != null){
                    for (ActionController controller : machine.getControllers()) {
                        controller.setAction(controller.getActionState(),controller.getTicks());
                    }
                }
            }
        }
    }
}
