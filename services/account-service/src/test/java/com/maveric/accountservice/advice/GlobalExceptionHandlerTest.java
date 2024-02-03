package com.maveric.accountservice.advice;

import com.maveric.accountservice.dto.ErrorDto;
import com.maveric.accountservice.dto.ErrorReponseDto;
import com.maveric.accountservice.exception.AccountIDNotfoundException;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.CustomerIDNotFoundExistsException;
import com.maveric.accountservice.exception.CustomerIdMissmatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handllingException() {
        AccountNotFoundException exception = new AccountNotFoundException("Account not found");
        ResponseEntity<ErrorReponseDto> error = globalExceptionHandler.handllingException(exception);
        assertEquals("404", error.getBody().getCode());
    }

    @Test
    void testHandllingException() {
        AccountIDNotfoundException exception = new AccountIDNotfoundException("Account not found");
        ResponseEntity<ErrorReponseDto> error = globalExceptionHandler.handllingException(exception);
        assertEquals("400", error.getBody().getCode());
    }

    @Test
    void handleCustomerIDNotFoundExistsException() {
        CustomerIDNotFoundExistsException exception = new CustomerIDNotFoundExistsException("Customer not found");
        ErrorDto error = globalExceptionHandler.handleCustomerIDNotFoundExistsException(exception);
        assertEquals("404", error.getCode());
    }

    @Test
    void testHandllingException1() {
        CustomerIdMissmatchException exception = new CustomerIdMissmatchException("Customer ID mismatch");
        ResponseEntity<ErrorReponseDto> error = globalExceptionHandler.handllingException(exception);
        assertEquals("400", error.getBody().getCode());
    }
}