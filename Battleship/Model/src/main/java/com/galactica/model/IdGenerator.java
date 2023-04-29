package com.galactica.model;

public class IdGenerator {

    private static int generator = 1;

    public static int get() {
        return generator++;
    }
}