package org.realEstate.myRealEstateService.exception;

public class CustomException extends Exception {

    // Constructor that accepts a message
    public CustomException(String message) {
        super(message);  // Pass the message to the parent Exception class
    }
}
