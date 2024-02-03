package com.maveric.transactionservice.exception;

import com.maveric.transactionservice.dto.ErrorDto;
import com.maveric.transactionservice.feignclient.FeignAccountConsumer;
import com.maveric.transactionservice.feignclient.FeignBalanaceConsumer;
import feign.Feign;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExceptionTest {

    @InjectMocks
    GlobalHandlerException globalHandlerException;

    @MockBean
    FeignBalanaceConsumer feignBalanaceConsumer;

    @MockBean
    FeignAccountConsumer feignAccountConsumer;

    @Test
    void accountIdMismatchException() {
        AccountIdMismatchException exception = new AccountIdMismatchException("Account Id not found");
        ResponseEntity<ErrorDto> error = globalHandlerException.handleAccountIdMismatchException(exception);
        assertEquals("404", error.getBody().getCode());
    }

    @Test
    void transactionAmountException() {
        TransactionAmountException exception = new TransactionAmountException("Insufficient balance");
        ResponseEntity<ErrorDto> error = globalHandlerException.handleTransactionAmountException(exception);
        assertEquals("404", error.getBody().getCode());
    }

    @Test
    void transactionIdNotFoundException() {
        TransactionIdNotFoundException exception = new TransactionIdNotFoundException("Transaction Id Not found");
        ResponseEntity<ErrorDto> error = globalHandlerException.handleTransactionIdNotFoundException(exception);
        assertEquals("404", error.getBody().getCode());
    }

    @Test
    void httpMessageNotReadableException() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("error");
        ResponseEntity<ErrorDto> error = globalHandlerException.handleFormatException(exception);
        assertEquals("400 BAD_REQUEST", error.getBody().getCode());
    }

    @Test
    void noHandlerFoundException() {
        NoHandlerFoundException exception = new NoHandlerFoundException("Error", "error", HttpHeaders.EMPTY);
        ResponseEntity<ErrorDto> error = globalHandlerException.nphandlerFoundException(exception);
        assertEquals("404", error.getBody().getCode());
    }
}
