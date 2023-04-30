package com.galactica.model;

// IdGenerator class for generating unique integer IDs
public class IdGenerator {

    // A static integer variable to store the current ID value
    private static int generator = 1;

    // Public static method to get the next unique ID and increment the generator
    public static int get() {
        return generator++;
    }
}
