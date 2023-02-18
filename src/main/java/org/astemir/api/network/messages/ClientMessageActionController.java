package org.astemir.api.network.messages;


import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.astemir.api.common.animation.HolderTarget;
import org.astemir.api.common.animation.HolderKey;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.action.IActionListener;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientMessageActionController {



    private HolderKey holderKey;

    private int controllerId;

    private int stateId;

    private int ticks;

    public ClientMessageActionController(HolderKey holderKey, int controllerId,int stateId, int ticks) {
        this.holderKey = holderKey;
        this.controllerId = controllerId;
        this.stateId = stateId;
        this.ticks = ticks;
    }


    public static void encode(ClientMessageActionController message, FriendlyByteBuf buf) {
        message.holderKey.write(buf);
        buf.writeInt(message.controllerId);
        buf.writeInt(message.stateId);
        buf.writeInt(message.ticks);
    }

    public static ClientMessageActionController decode(FriendlyByteBuf buf) {
        return new ClientMessageActionController(HolderKey.read(buf),buf.readInt(),buf.readInt(),buf.readInt());
    }


    public static class Handler implements BiConsumer<ClientMessageActionController, Supplier<NetworkEvent.Context>> {

        @Override
        public void accept(ClientMessageActionController message, Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Minecraft minecraft = Minecraft.getInstance();
                if (message.holderKey.getTarget() == HolderTarget.ENTITY) {
                    Entity entity = minecraft.level.getEntity(message.holderKey.getId());
                    if (entity != null) {
                        if (entity instanceof IActionListener) {
                            ActionController controller = ((IActionListener) entity).getActionStateMachine().getControllers().get(message.controllerId);
                            controller.setActionWithoutSync(controller.getActionById(message.stateId), message.ticks);
                        }
                    }
                }else
                if (message.holderKey.getTarget() == HolderTarget.BLOCK) {
                    BlockEntity block = minecraft.level.getBlockEntity(message.holderKey.getPos());
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
}
