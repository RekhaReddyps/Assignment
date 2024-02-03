package com.maveric.transactionservice.exception;

public class AccountIdMismatchException extends RuntimeException{
    public AccountIdMismatchException(String message) {
        super(message);
    }
}
