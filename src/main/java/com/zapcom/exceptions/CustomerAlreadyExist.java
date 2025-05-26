package com.zapcom.exceptions;

public class CustomerAlreadyExist extends RuntimeException {
    public CustomerAlreadyExist(String message) {
        super(message);
    }
}
