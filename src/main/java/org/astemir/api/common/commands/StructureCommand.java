package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandVariant;
import org.astemir.api.common.world.schematic.WESchematic;
import org.astemir.api.math.vector.Vector3;
import org.astemir.api.utils.FileUtils;
import java.util.Map;

public class StructureCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandBuilder builder = new CommandBuilder("structure");
        CommandArgument name = CommandArgument.greedyString("name");
        CommandArgument blockPos = CommandArgument.blockPos("position");
        CommandArgument rotation = CommandArgument.vector3("rotation");
        CommandArgument skipAir = CommandArgument.bool("skip-air");
        builder.addVariant(new CommandVariant(name,blockPos,rotation,skipAir).execute((p)->{
            ServerLevel level = p.getSource().getLevel();
            try {
                WESchematic schematic = new WESchematic(FileUtils.getResource(name.getString(p)+".schem"));
                for (Map.Entry<BlockPos, BlockState> entry : schematic.blocks(blockPos.getBlockPos(p), Vector3.from(rotation.getVector3(p)),true, skipAir.getBoolean(p)).entrySet()) {
                    level.setBlock(entry.getKey(), entry.getValue(), 19);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }));
        builder.addVariant(new CommandVariant(name,blockPos,rotation).execute((p)->{
            ServerLevel level = p.getSource().getLevel();
            try {
                WESchematic schematic = new WESchematic(FileUtils.getResource(name.getString(p)+".schem"));
                for (Map.Entry<BlockPos, BlockState> entry : schematic.blocks(blockPos.getBlockPos(p), Vector3.from(rotation.getVector3(p)),true, true).entrySet()) {
                    level.setBlock(entry.getKey(), entry.getValue(), 19);
                }
            }catch (Exception e){
            }
            return 0;
        }));
        builder.addVariant(new CommandVariant(name,blockPos).execute((p)->{
            ServerLevel level = p.getSource().getLevel();
            try {
                WESchematic schematic = new WESchematic(FileUtils.getResource(name.getString(p)+".schem"));
                for (Map.Entry<BlockPos, BlockState> entry : schematic.blocks(blockPos.getBlockPos(p), new Vector3(0,0,0),true, true).entrySet()) {
                    level.setBlock(entry.getKey(), entry.getValue(), 19);
                }
            }catch (Exception e){
            }
            return 0;
        }));
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }

}
