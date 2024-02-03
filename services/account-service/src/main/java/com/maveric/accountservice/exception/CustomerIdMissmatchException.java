package com.maveric.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomerIdMissmatchException extends RuntimeException{
    public CustomerIdMissmatchException(String message){
        super(message);
    }
}