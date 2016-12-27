package com.voltron42.junitPractice.model;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class SmallDog extends Dog {

    @Override
    protected String size() {
        return "small";
    }

    @Override
    public String bark() {
        return "Yip yip!";
    }
}
