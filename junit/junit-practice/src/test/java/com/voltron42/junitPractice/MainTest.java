package com.voltron42.junitPractice;

import com.voltron42.testCategories.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by daniel.johnson on 12/27/2016.
 */
@Category(IntegrationTest.class)
public class MainTest {

    @Test
    public void testMain() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Main.main(new String[0]);
        String printOut = new String(out.toByteArray());
        assertEquals(printOut, "Hello World!\r\n");
    }
}