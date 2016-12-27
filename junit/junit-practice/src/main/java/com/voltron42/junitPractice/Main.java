package com.voltron42.junitPractice;

import com.voltron42.junitPractice.model.Kennel;
import com.voltron42.junitPractice.parse.Parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        InputStream stream = Main.class.getResourceAsStream("/dogs.json");
        Parser<Kennel> parser = new Parser.Factory().build(Kennel.class);
        Kennel kennel = parser.parse(stream);
        kennel.print(System.out);
    }
}
