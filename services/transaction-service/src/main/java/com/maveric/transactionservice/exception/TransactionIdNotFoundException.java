package com.maveric.transactionservice.exception;

public class TransactionIdNotFoundException extends Exception{
    public TransactionIdNotFoundException(String message) {
        super(message);
    }
}
