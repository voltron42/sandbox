package com.voltron42.junitPractice.model;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class BigDog extends Dog {
    @Override
    protected String size() {
        return "big";
    }

    @Override
    public String bark() {
        return "Bow wow!";
    }
}
