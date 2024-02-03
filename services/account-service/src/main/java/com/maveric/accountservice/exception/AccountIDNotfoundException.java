package com.maveric.accountservice.exception;

public class AccountIDNotfoundException extends RuntimeException {
    public AccountIDNotfoundException(String message)
    {
        super(message);
    }
}
