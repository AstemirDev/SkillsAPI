package org.astemir.api.common.entity.utils;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import org.astemir.api.common.entity.ai.goals.GoalLookAtTradingPlayer;
import org.astemir.api.common.entity.ai.goals.GoalTradeWithPlayer;

public class TradeUtils {


    public static <T extends Mob & Merchant & ISpecialTrader> void loadOffers(T mob, CompoundTag tag) {
        if (tag.contains("Offers", 10)) {
            mob.setOffers(new MerchantOffers(tag.getCompound("Offers")));
        }
    }

    public static <T extends Mob & Merchant & ISpecialTrader> void saveOffers(T mob, CompoundTag tag) {
        MerchantOffers offers = mob.getOffers();
        if(!offers.isEmpty())
        {
            tag.put("Offers", offers.createTag());
        }
    }

    public static <T extends Mob & Merchant & ISpecialTrader> void addDefaultTraderGoals(T mob){
        mob.goalSelector.addGoal(1, new GoalTradeWithPlayer<>(mob));
        mob.goalSelector.addGoal(1, new GoalLookAtTradingPlayer<>(mob));
    }

    public static <T extends Mob & Merchant & ISpecialTrader> InteractionResult interactWithTrader(T mob, Player player) {
        if (mob.getTradingPlayer() == null) {
            if (mob.getOffers().isEmpty()) {
                return InteractionResult.sidedSuccess(mob.level.isClientSide);
            } else if (!mob.level.isClientSide) {
                TradeUtils.startTrading(mob, player);
                return InteractionResult.sidedSuccess(mob.level.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    public static <T extends Mob & Merchant & ISpecialTrader> void startTrading(T mob,Player player){
        setTradingPlayer(mob,player);
        mob.openTradingScreen(player, mob.getDisplayName(), 1);
    }

    public static <T extends Mob & Merchant & ISpecialTrader> void setTradingPlayer(T mob, Player player){
        boolean flag = mob.getTradingPlayer() != null && player == null;
        mob.setSpecialTradingPlayer(player);
        if (flag) {
            stopTrading(mob);
        }
    }

    public static <T extends Mob & Merchant & ISpecialTrader> void stopTrading(T mob) {
        mob.setTradingPlayer(null);
    }

    public interface ISpecialTrader {

        //Just set field to player
        @Deprecated
        void setSpecialTradingPlayer(Player player);

        @Deprecated
        void setOffers(MerchantOffers offers);
    }
}
