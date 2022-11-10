package org.astemir.api.common.commands.build;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public class CommandArgument extends CommandPart{

    private ArgumentType type;
    private SuggestionProvider suggestionProvider;

    public CommandArgument(String name,ArgumentType type) {
        super(name);
        this.type = type;
    }

    public static CommandArgument blockPos(String name){
        return new CommandArgument(name, BlockPosArgument.blockPos());
    }

    public static CommandArgument word(String name){
        return new CommandArgument(name, StringArgumentType.word());
    }

    public static CommandArgument integer(String name){
        return new CommandArgument(name, IntegerArgumentType.integer());
    }

    public static CommandArgument integer(String name,int min,int max){
        return new CommandArgument(name, IntegerArgumentType.integer(min,max));
    }

    public static CommandArgument integer(String name,int min){
        return new CommandArgument(name, IntegerArgumentType.integer(min));
    }

    public static CommandArgument entities(String name){
        return new CommandArgument(name, EntityArgument.entities());
    }

    public CommandArgument suggestion(SuggestionProvider suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
        return this;
    }

    public static CommandArgument players(String name){
        return new CommandArgument(name, EntityArgument.players());
    }

    public static CommandArgument create(String name,ArgumentType type){
        return new CommandArgument(name,type);
    }

    public static CommandArgument summonArgument(String name){
        return new CommandArgument(name, EntitySummonArgument.id());
    }

    public int getInt(CommandContext<CommandSourceStack> context){
        return IntegerArgumentType.getInteger(context,getArgumentName());
    }

    public String getString(CommandContext<CommandSourceStack> context){
        return StringArgumentType.getString(context,getArgumentName());
    }

    public ResourceLocation getSummonArgument(CommandContext<CommandSourceStack> context){
        try {
            return EntitySummonArgument.getSummonableEntity(context,getArgumentName());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BlockPos getBlockPos(CommandContext<CommandSourceStack> context){
        try {
            return BlockPosArgument.getLoadedBlockPos(context,getArgumentName());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<? extends Entity> getEntities(CommandContext<CommandSourceStack> context){
        try {
            return EntityArgument.getEntities(context,getArgumentName());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public ArgumentBuilder build(){
        RequiredArgumentBuilder res = Commands.argument(getArgumentName(),type);
        if (suggestionProvider != null){
            res = res.suggests(suggestionProvider);
        }
        return res;
    }
}
