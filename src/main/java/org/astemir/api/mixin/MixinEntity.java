package org.astemir.api.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.common.event.EntityTagEvent;
import org.astemir.api.common.event.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class,priority = 500)
public abstract class MixinEntity extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, net.minecraftforge.common.extensions.IForgeEntity{

    protected MixinEntity(Class<Entity> baseClass, boolean isLazy) {
        super(baseClass, isLazy);
    }

    @Inject(method = "tick",at = @At("HEAD"))
    public void onTick(CallbackInfo ci){
        Entity entity = (Entity)(Object)this;
        EntityTickEvent event = new EntityTickEvent(entity,entity.tickCount);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "load",at = @At("TAIL"))
    public void onLoadData(CompoundTag tag, CallbackInfo ci){
        Entity entity = (Entity)(Object)this;
        EntityTagEvent.Load event = new EntityTagEvent.Load(entity,tag);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "save",at = @At("TAIL"))
    public void onSaveData(CompoundTag tag, CallbackInfoReturnable<Boolean> cir){
        Entity entity = (Entity)(Object)this;
        EntityTagEvent.Save event = new EntityTagEvent.Save(entity,tag);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
