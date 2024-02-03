package com.maveric.transactionservice.exception;

public class TransactionAmountException extends RuntimeException{
    public TransactionAmountException(String message) {
        super(message);
    }
}
