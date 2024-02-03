package com.maveric.userservice.exception;

import com.maveric.userservice.dto.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserExceptionTest {

    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    void emailDuplicateException() {
        EmailDuplicateException exception = new EmailDuplicateException("User not found with email");
        ResponseEntity<Error> error = globalExceptionHandler.handleEmailDuplication(exception);
        assertEquals("400", error.getBody().getCode());
    }

    @Test
    void userNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User Not found");
        ResponseEntity<Error> error = globalExceptionHandler.handleUserNotFound(exception);
        assertEquals("404", error.getBody().getCode());
    }

    @Test
    void genderNotFound() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Gender should be either MALE/FEMALE");
        ResponseEntity<Error> error = globalExceptionHandler.handleFormatException(exception);
        assertEquals("400 BAD_REQUEST", error.getBody().getCode());
    }

    @Test
    void noHandlerFoundException() {
        NoHandlerFoundException exception = new NoHandlerFoundException("POST", "localhost:3000", HttpHeaders.EMPTY);
        ResponseEntity<Error> error = globalExceptionHandler.nphandlerFoundException(exception);
        assertEquals("404", error.getBody().getCode());
    }
}
