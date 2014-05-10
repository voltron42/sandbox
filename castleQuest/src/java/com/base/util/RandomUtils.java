package com.base.util;

import java.util.Set;

public class RandomUtils {
    public static int getInt(final int max){
        return (int) Math.random()*max;
    }

    public static <T> T getElementFromSet(final Set<T> set){
        int seed = getInt(set.size());
        for(T t : set){
            if(0==seed--){
                return t;
            }
        }
        return null;
    }
}
