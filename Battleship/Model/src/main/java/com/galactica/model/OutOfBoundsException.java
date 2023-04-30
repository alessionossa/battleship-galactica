package com.galactica.model;

// OutOfBoundsException is a custom exception class that extends the built-in Exception class
public class OutOfBoundsException extends Exception {

    // Constructor for the OutOfBoundsException, accepting a message as a parameter
    public OutOfBoundsException(String message) {
        // Pass the message to the base class (Exception) constructor
        super(message);
    }
}
