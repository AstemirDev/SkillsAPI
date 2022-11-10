package org.astemir.api.math;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WeightedRandom<T> {

    private final List<Pair<Double, T>> table = new ArrayList<>();
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
        table.add(new Pair<>(chance, element));
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
        for (Pair<Double, T> pair : this.table) {
            this.totalWeight += pair.getKey();
        }
    }

    public WeightedRandom canHasNoResult() {
        resultCanBeNull = true;
        return this;
    }

    private double maxCommonElement() {
        double maxChance = 0;
        for (Pair<Double, T> drop : table) {
            if (drop.getKey() > maxChance) {
                maxChance = drop.getKey();
            }
        }
        return maxChance;
    }

    public <M> T random(M m) {
        double num = this.random.nextDouble() * totalWeight;
        for (Pair<Double, T> pair : table) {
            double chance = pair.getKey();
            if (num <= chance) {
                return pair.getValue();
            }
            num -= pair.getKey();
        }
        return null;
    }

    public T random() {
        return random(null);
    }
}
