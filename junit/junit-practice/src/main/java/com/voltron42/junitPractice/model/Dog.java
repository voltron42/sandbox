package com.voltron42.junitPractice.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public abstract class Dog {

    private String name;
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "I am a " + size() + " dog. My name is " + name + ". I am " + (age*7) + " in dog years. " + bark();
    }

    protected abstract String size();

    public abstract String bark();
}
