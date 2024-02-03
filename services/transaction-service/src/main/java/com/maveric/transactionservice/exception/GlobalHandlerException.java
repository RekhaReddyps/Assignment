package com.maveric.transactionservice.exception;

import com.maveric.transactionservice.constant.MessageConstant;
import com.maveric.transactionservice.dto.ErrorDto;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalHandlerException extends Exception {

    @ExceptionHandler(AccountIdMismatchException.class)
    public ResponseEntity<ErrorDto> handleAccountIdMismatchException(AccountIdMismatchException e) {
        ErrorDto error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgsNotValidException(MethodArgumentNotValidException ex) {
        ErrorDto error = getError(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                String.valueOf(HttpStatus.BAD_REQUEST));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleFormatException(HttpMessageNotReadableException e) {
        ErrorDto error = getError(MessageConstant.TYPE_ERROR, String.valueOf(HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> nphandlerFoundException(NoHandlerFoundException e) {
        ErrorDto error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionIdNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTransactionIdNotFoundException(TransactionIdNotFoundException e) {
        ErrorDto error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionAmountException.class)
    public ResponseEntity<ErrorDto> handleTransactionAmountException(TransactionAmountException e) {
        ErrorDto error = getError(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorDto> handleFeignExceptionNotFound(FeignException e, HttpServletResponse response) {
        String message = e.contentUTF8();
        String decode = (String) e.contentUTF8().subSequence(message.lastIndexOf(":\""), message.length()-2);
        ErrorDto error = getError(decode.replace(":\"", ""), String.valueOf(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ErrorDto> handleFeignExceptionBadRequest(FeignException e, HttpServletResponse response) {
        String message = e.contentUTF8();
        String decode = (String) e.contentUTF8().subSequence(message.lastIndexOf(":\""), message.length()-2);
        ErrorDto error = getError(decode.replace(":\"", ""), String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ErrorDto getError(String message , String code){
        ErrorDto error = new ErrorDto();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }
}
