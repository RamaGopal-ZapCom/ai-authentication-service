package com.zapcom.exceptions;

public class AccountAlreadyActivated extends RuntimeException {
    public AccountAlreadyActivated(String accountAlreadyActivated) {
        super(accountAlreadyActivated);
    }
}
