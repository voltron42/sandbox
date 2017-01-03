package com.voltron42.junitPractice;

import com.voltron42.testCategories.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;

/**
 * Created by daniel.johnson on 1/3/2017.
 */
@Category(UnitTest.class)
public class HelloWorldTest {

    @Test
    public void testToString() throws Exception {
        HelloWorld hw = new HelloWorld();
        assertEquals(hw.toString(), "Hello World!");
    }
}