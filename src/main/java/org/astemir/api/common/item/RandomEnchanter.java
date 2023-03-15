package org.astemir.api.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.astemir.api.math.collection.Couple;
import org.astemir.api.math.random.WeightedRandom;

import java.util.HashMap;
import java.util.Map;


public class RandomEnchanter {


    public static EnchantmentMap newEnchantmentMap(){
        return new EnchantmentMap();
    }


    public static RandomEnchantment enchantment(Enchantment enchantment){
        return new RandomEnchantment(enchantment);
    }

    public static class EnchantmentMap{

        private Map<RandomEnchantment,Double> storedEnchantments = new HashMap<>();

        public EnchantmentMap add(RandomEnchantment enchantment,double chance){
            this.storedEnchantments.put(enchantment,chance);
            return this;
        }

        public WeightedRandom randomList(){
            WeightedRandom<RandomEnchantment> mutable = new WeightedRandom<>();
            for (Map.Entry<RandomEnchantment, Double> entry : storedEnchantments.entrySet()) {
                mutable = mutable.add(entry.getValue(),entry.getKey());
            }
            mutable.build();
            return mutable;
        }

        public ItemStack enchant(ItemStack stack,int enchantmentCount){
            WeightedRandom<RandomEnchantment> weightedRandom = randomList();
            for (int i = 0;i<enchantmentCount;i++){
                RandomEnchantment randomEnchantment = weightedRandom.randomWithRemove();
                if (randomEnchantment != null) {
                    if (randomEnchantment.canEnchant(stack)) {
                        Integer level = randomEnchantment.getLevels().random();
                        if (level == null) {
                            level = 1;
                        }
                        stack.enchant(randomEnchantment.getEnchantment(), level);
                    }
                }
            }
            return stack;
        }
    }

    public static class RandomEnchantment{

        private Enchantment enchantment = Enchantments.SHARPNESS;
        private Enchantment[] conflictsWith = new Enchantment[]{};
        private WeightedRandom<Integer> weightedRandom = new WeightedRandom<>();

        public RandomEnchantment(Enchantment enchantment) {
            this.enchantment = enchantment;
        }


        public RandomEnchantment conflicts(Enchantment... enchantments){
            this.conflictsWith = enchantments;
            return this;
        }

        public RandomEnchantment levels(Couple<Double,Integer>... levels) {
            this.weightedRandom = new WeightedRandom<>();
            for (Couple<Double, Integer> couple : levels) {
                weightedRandom.add(couple.getKey(),couple.getValue());
            }
            weightedRandom.build();
            return this;
        }

        public boolean canEnchant(ItemStack itemStack){
            for (Enchantment conflictEnchantment : getConflictsWith()) {
                if (itemStack.getEnchantmentLevel(conflictEnchantment) > 0){
                    return false;
                }
            }
            return true;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public Enchantment[] getConflictsWith() {
            return conflictsWith;
        }


        public WeightedRandom<Integer> getLevels() {
            return weightedRandom;
        }
    }

}
