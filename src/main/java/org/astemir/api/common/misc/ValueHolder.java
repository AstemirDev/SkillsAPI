package org.astemir.api.common.misc;

public class ValueHolder<T> {

    private T value;

    public ValueHolder(T value) {
        this.value = value;
    }

    public ValueHolder() {
    }

    public T getValue() {
        return value;
    }

    public ValueHolder<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public static ValueHolder empty(){
        return new ValueHolder();
    }
}
