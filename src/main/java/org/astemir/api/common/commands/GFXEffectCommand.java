package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandPart;
import org.astemir.api.common.commands.build.CommandVariant;
import org.astemir.api.common.gfx.*;
import org.astemir.api.math.Color;

import java.util.Collection;

public class GFXEffectCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandArgument targets = CommandArgument.players("targets");
        CommandArgument power = CommandArgument.doubleArg("power");
        CommandArgument speed = CommandArgument.doubleArg("speed");
        CommandArgument ticks = CommandArgument.integer("ticks");
        CommandArgument color = CommandArgument.vector3("color");
        CommandArgument replaceMode = CommandArgument.bool("replace");
        CommandBuilder builder = new CommandBuilder("gfx")
                .variants(new CommandVariant(new CommandPart("shakescreen"),targets,power,ticks,replaceMode).execute((p)->{
                    GFXScreenShake effect = new GFXScreenShake(power.getDouble(p),ticks.getInt(p));
                    boolean replace = replaceMode.getBoolean(p);
                    sendEffect(targets.getEntities(p),effect,replace);
                    return 0;
                }),new CommandVariant(new CommandPart("blackout"),targets,color,speed,replaceMode).execute((p)->{
                    Vec3 vec = color.getVector3(p);
                    GFXBlackOut effect = new GFXBlackOut(new Color((float)vec.x,(float)vec.y,(float)vec.z,1),speed.getDouble(p));
                    boolean replace = replaceMode.getBoolean(p);
                    sendEffect(targets.getEntities(p),effect,replace);
                    return 0;
                }),new CommandVariant(new CommandPart("blackin"),targets,color,speed,replaceMode).execute((p)->{
                    Vec3 vec = color.getVector3(p);
                    GFXBlackIn effect = new GFXBlackIn(new Color((float)vec.x,(float)vec.y,(float)vec.z,1),speed.getDouble(p));
                    boolean replace = replaceMode.getBoolean(p);
                    sendEffect(targets.getEntities(p),effect,replace);
                    return 0;
                }),new CommandVariant(new CommandPart("blackinout"),targets,color,speed,replaceMode).execute((p)->{
                    Vec3 vec = color.getVector3(p);
                    GFXBlackInOut effect = new GFXBlackInOut(new Color((float)vec.x,(float)vec.y,(float)vec.z,1),speed.getDouble(p));
                    boolean replace = replaceMode.getBoolean(p);
                    sendEffect(targets.getEntities(p),effect,replace);
                    return 0;
                }));
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }

    public static void sendEffect(Collection<? extends Entity> entities,GFXEffect effect,boolean replace){
        for (Entity entity : entities) {
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) entity;
                PlayerGFXEffectManager.addGFXEffect(player, effect, replace);
            }
        }
    }
}