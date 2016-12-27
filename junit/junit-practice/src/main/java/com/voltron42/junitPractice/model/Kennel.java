package com.voltron42.junitPractice.model;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class Kennel {

    private List<Dog> dogs;

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    public void print(PrintStream out) {
        for (Dog dog : dogs) {
            out.println(dog);
        }
    }
}
