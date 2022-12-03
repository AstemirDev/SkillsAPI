package org.astemir.api.network.messages;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.common.state.IActionListener;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ServerMessageActionSync {

    private UUID uuid;
    private BlockPos pos;
    private ClientMessageActionController.Type type;

    public ServerMessageActionSync(UUID uuid) {
        this.uuid = uuid;
        this.type = ClientMessageActionController.Type.ENTITY;
    }

    public ServerMessageActionSync(BlockPos pos) {
        this.pos = pos;
        this.type = ClientMessageActionController.Type.BLOCK;
    }

    public static void encode(ServerMessageActionSync message, FriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        if (message.type == ClientMessageActionController.Type.ENTITY) {
            buf.writeUUID(message.uuid);
        }else
        if (message.type == ClientMessageActionController.Type.BLOCK) {
            buf.writeBlockPos(message.pos);
        }
    }

    public static ServerMessageActionSync decode(FriendlyByteBuf buf) {
        ClientMessageActionController.Type type = buf.readEnum(ClientMessageActionController.Type.class);
        if (type == ClientMessageActionController.Type.ENTITY) {
            return new ServerMessageActionSync(buf.readUUID());
        }else
        if (type == ClientMessageActionController.Type.BLOCK) {
            return new ServerMessageActionSync(buf.readBlockPos());
        }
        return null;
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
                        controller.sendUpdatePacket(playerEntity);
                    }
                }
            }
        }
    }
}
