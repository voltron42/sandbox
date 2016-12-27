package com.voltron42.junitPractice.model;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class MidSizeDog extends Dog {
    @Override
    protected String size() {
        return "mid-size";
    }

    @Override
    public String bark() {
        return "Woof woof!";
    }
}
