package com.maveric.userservice.exception;

import com.maveric.userservice.constant.MessageConstant;
import com.maveric.userservice.dto.Error;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
        Error error = getError(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                String.valueOf(HttpStatus.BAD_REQUEST));
        logger.error(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Error> handleEmailDuplication(EmailDuplicateException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        logger.error(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> handleUserNotFound(UserNotFoundException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        logger.error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIdMismatchException.class)
    public ResponseEntity<Error> handleUserIdMismatch(UserIdMismatchException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        logger.error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleFormatException(HttpMessageNotReadableException e) {
        Error error = getError(MessageConstant.GENDER_ERROR, String.valueOf(HttpStatus.BAD_REQUEST));

        logger.error(MessageConstant.GENDER_ERROR);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Error> nphandlerFoundException(NoHandlerFoundException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));

        logger.error(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST.value()));

        logger.error(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Error> missingPathVariableException(MissingPathVariableException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));

        logger.error(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Error> httpServerErrorException(HttpServerErrorException e) {
        Error error = getError(e.getMessage(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        logger.error(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Error> handleFeignExceptionNotFound(FeignException e, HttpServletResponse response) {
        String message = e.contentUTF8();
        String decode = (String) e.contentUTF8().subSequence(message.lastIndexOf(":\""), message.length()-2);
        Error error = getError(decode.replace(":\"", ""), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<Error> handleFeignExceptionBadRequest(FeignException e, HttpServletResponse response) {
        String message = e.contentUTF8();
        String decode = (String) e.contentUTF8().subSequence(message.lastIndexOf(":\""), message.length()-2);
        Error error = getError(decode.replace(":\"", ""), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private Error getError(String message , String code){
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }
}
