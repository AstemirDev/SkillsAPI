package org.astemir.api.math;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WeightedRandom<T> {

    private final List<Couple<Double, T>> table = new ArrayList<>();
    private final Random random;
    private double totalWeight;
    private boolean resultCanBeNull = false;

    public WeightedRandom() {
        random = new Random();
    }

    public WeightedRandom(Random random) {
        this.random = random;
    }

    public WeightedRandom add(double chance, T element) {
        table.add(new Couple<>(chance, element));
        return this;
    }

    public T getNullElement() {
        return null;
    }


    public WeightedRandom build() {
        if (resultCanBeNull) {
            double maxChance = maxCommonElement();
            add(100 - maxChance, getNullElement());
        }
        calculateTotalWeight();
        return this;
    }

    public void calculateTotalWeight() {
        this.totalWeight = 0;
        for (Couple<Double, T> pair : this.table) {
            this.totalWeight += pair.getKey();
        }
    }

    public WeightedRandom canHasNoResult() {
        resultCanBeNull = true;
        return this;
    }

    private double maxCommonElement() {
        double maxChance = 0;
        for (Couple<Double, T> drop : table) {
            if (drop.getKey() > maxChance) {
                maxChance = drop.getKey();
            }
        }
        return maxChance;
    }

    public <M extends IRandomImplifications<T>> T random(M m) {
        double num = this.random.nextDouble() * totalWeight;
        for (Couple<Double, T> pair : table) {
            double chance = pair.getKey();
            if (num <= chance) {
                if (m != null){
                    m.imply(pair.getValue());
                }
                return pair.getValue();
            }
            num -= pair.getKey();
        }
        return null;
    }

    public <M extends IRandomImplifications<T>> T randomWithRemove(M m) {
        double num = this.random.nextDouble() * totalWeight;
        for (Couple<Double, T> pair : table) {
            double chance = pair.getKey();
            if (num <= chance) {
                T value = pair.getValue();
                if (m != null){
                    m.imply(value);
                }
                table.remove(pair);
                return value;
            }
            num -= pair.getKey();
        }
        return null;
    }


    public T random() {
        return random(null);
    }

    public T randomWithRemove() {
        return randomWithRemove(null);
    }


    public List<Couple<Double, T>> getTable() {
        return table;
    }
}
