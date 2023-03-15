package org.astemir.api.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.astemir.api.math.collection.Couple;
import java.util.HashMap;
import java.util.Map;

public class ToolActionResult {


    public static final Map<Couple<Block, Block>,ToolAction> actions = new HashMap<>();

    public static void addVariant(ToolAction action,Block from,Block to){
        actions.put(new Couple<>(from,to),action);
    }

    public static SoundEvent getActionSound(ToolAction action){
        if (action == ToolActions.AXE_STRIP){
            return SoundEvents.AXE_STRIP;
        }else
        if (action == ToolActions.HOE_TILL){
            return SoundEvents.HOE_TILL;
        }else
        if (action == ToolActions.SHOVEL_FLATTEN){
            return SoundEvents.SHOVEL_FLATTEN;
        }
        return null;
    }

    public static Block getVariant(ToolAction action,Block block){
        for (Map.Entry<Couple<Block, Block>, ToolAction> entry : actions.entrySet()) {
            if (entry.getValue() == action){
                if (entry.getKey().getKey() == block){
                    return entry.getKey().getValue();
                }
            }
        }
        return null;
    }

    public static boolean canBeProcessed(ToolAction action,Block block){
        return getVariant(action,block) != null;
    }
}
