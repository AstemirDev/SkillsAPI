package org.astemir.api.common.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.wrapper.AbstractModelWrapperArmor;
import org.astemir.api.client.registry.ArmorModels;
import org.astemir.api.common.misc.ICustomRendered;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SkillsArmorItem extends ArmorItem implements ICustomRendered {


    public SkillsArmorItem(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
        super(p_40386_, p_40387_, p_40388_);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "textures/models/armor/leather_layer_1.png";
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                AbstractModelWrapperArmor wrapper = ArmorModels.getModel(itemStack.getItem());
                wrapper.setupAngles(livingEntity,itemStack.getItem(),itemStack,equipmentSlot,original);
                return wrapper;
            }
            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                AbstractModelWrapperArmor wrapper = ArmorModels.getModel(itemStack.getItem());
                wrapper.setupAngles(livingEntity,itemStack.getItem(),itemStack,equipmentSlot,original);
                return wrapper;
            }
        });
    }
}
