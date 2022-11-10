package org.astemir.api.network.messages;


import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.IActionListener;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ActionControllerMessage {


    private int entityId = -1;

    private BlockPos blockPos;

    private int controllerId;

    private int stateId;

    private int ticks;

    private Type type;

    public ActionControllerMessage(int entityId, int controllerId,int stateId,int ticks) {
        this.type = Type.ENTITY;
        this.entityId = entityId;
        this.controllerId = controllerId;
        this.stateId = stateId;
        this.ticks = ticks;
    }


    public ActionControllerMessage(BlockPos pos, int controllerId, int stateId,int ticks) {
        this.type = Type.BLOCK;
        this.blockPos = pos;
        this.controllerId = controllerId;
        this.stateId = stateId;
        this.ticks = ticks;
    }

    public static void encode(ActionControllerMessage message, FriendlyByteBuf buf) {
        buf.writeEnum(message.type);
        if (message.entityId != -1) {
            buf.writeInt(message.entityId);
        }
        if (message.blockPos != null){
            buf.writeBlockPos(message.blockPos);
        }
        buf.writeInt(message.controllerId);
        buf.writeInt(message.stateId);
        buf.writeInt(message.ticks);
    }

    public static ActionControllerMessage decode(FriendlyByteBuf buf) {
        Type type = buf.readEnum(Type.class);
        switch (type){
            case ENTITY:{
                return new ActionControllerMessage(buf.readInt(), buf.readInt(),buf.readInt(),buf.readInt());
            }
            case BLOCK:{
                return new ActionControllerMessage(buf.readBlockPos(), buf.readInt(),buf.readInt(),buf.readInt());
            }
        }
        return null;
    }


    public static class Handler implements BiConsumer<ActionControllerMessage, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ActionControllerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                if (message.type == Type.ENTITY) {
                    Entity entity = minecraft.level.getEntity(message.entityId);
                    if (entity != null) {
                        if (entity instanceof IActionListener) {
                            ActionController controller = ((IActionListener) entity).getActionStateMachine().getControllers().get(message.controllerId);
                            controller.setActionWithoutSync(controller.getActionById(message.stateId), message.ticks);
                        }
                    }
                }else
                if (message.type == Type.BLOCK){
                    BlockEntity block = minecraft.level.getBlockEntity(message.blockPos);
                    if (block != null){
                        if (block instanceof IActionListener){
                            ActionController controller = ((IActionListener) block).getActionStateMachine().getControllers().get(message.controllerId);
                            controller.setActionWithoutSync(controller.getActionById(message.stateId), message.ticks);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static enum Type{
        ENTITY,BLOCK
    }
}
