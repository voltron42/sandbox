package com.voltron42.junitPractice;

import com.voltron42.junitPractice.model.Kennel;
import com.voltron42.junitPractice.parse.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import sun.misc.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by daniel.johnson on 1/3/2017.
 */
@Category(Regression.class)
public class Regression {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testMain() throws Exception {
        InputStream dogs = Regression.class.getResourceAsStream("/regression.json");
        InputStream expectedStream = Regression.class.getResourceAsStream("/expected.txt");
        String expected = new String(IOUtils.readFully(expectedStream, -1, true));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Parser<Kennel> parser = new Parser.Factory().build(Kennel.class);
        Kennel kennel = parser.parse(dogs);
        kennel.print(new PrintStream(out));
        String actual = new String(out.toByteArray());
        assertEquals(expected, actual);
    }

}
