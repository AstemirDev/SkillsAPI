package org.astemir.api.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBlockClickListener {

    public void onBlockClick(ItemStack stack, Player player, BlockPos pos);
}
