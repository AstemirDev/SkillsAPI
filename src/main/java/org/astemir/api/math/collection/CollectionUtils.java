package org.astemir.api.math.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

    public static <T> List<T> list(){
        return new ArrayList<>();
    }

    public static <T> T[] array(int size){
        return (T[]) new Object[size];
    }

    public static <T> T[] array(T... elements){
        T[] result = array(elements.length);
        for (int i = 0; i < elements.length; i++) {
            result[i] = elements[i];
        }
        return result;
    }

    public static <T> T[] addToArray(T[] array,T element){
        T[] newArray = array(array.length+1);
        newArray[array.length] = element;
        return newArray;
    }

    public static <T> List<T> toList(T... elements){
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <T> T[] toArray(List<T> list){
        return (T[]) list.toArray(new Object[list.size()]);
    }

    public static <T> T[] toArray(Set<T> set){
        T[] result = array(set.size());
        int i = 0;
        for (T t : set) {
            result[i] = t;
            i++;
        }
        return result;
    }
}
