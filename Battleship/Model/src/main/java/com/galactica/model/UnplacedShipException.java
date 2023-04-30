// Declare the package and class
package com.galactica.model;

// Declare a custom exception class that extends the built-in Exception class
public class UnplacedShipException extends Exception {

    // Declare a constructor that takes a message string as a parameter
    public UnplacedShipException(String message) {
        // Call the constructor of the parent Exception class and pass the message
        // string to it
        super(message);
    }
}
