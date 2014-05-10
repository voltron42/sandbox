package com.base.util;

import java.util.Iterator;

public class Range <T extends Number> implements Iterable<T>, Comparable<T>{

    private final T min;
    private final T max;
    private final T step;
    private final Class<T> cls;

    private Range(final T min, final T max, final T step) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.cls = (Class<T>) this.max.getClass();
    }

    public int compareTo(final T arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    private static enum NumberType {
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BIGINT,
        BIGDEC,
        ATOMINT,
        ATOMLONG,
        ;
    }
}
