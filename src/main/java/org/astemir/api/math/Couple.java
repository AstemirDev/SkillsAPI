package org.astemir.api.math;


import java.util.Objects;

public class Couple<K, V> {

    private final K key;

    private final V value;

    public Couple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Couple) {
            Couple pair = (Couple) o;
            if (!Objects.equals(key, pair.key)) return false;
            return Objects.equals(value, pair.value);
        }
        return false;
    }

    public static <K,V> Couple<K,V> create(K key,V value){
        return new Couple<K,V>(key,value);
    }
}

