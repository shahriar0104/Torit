package com.example.anik.ssparen;

/**
 * Created by Anik on 8/19/2017.
 */

public class retriveTest {

    String day;
    String values;
    String flat;
    public retriveTest() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public retriveTest(String values, String day,String flat) {
        this.values = values;
        this.day = day;
        this.flat=flat;
    }

    public String getValues() {
        return values;
    }
    public String getDay() {
        return day;
    }

    public String getFlat() {
        return flat;
    }
}
