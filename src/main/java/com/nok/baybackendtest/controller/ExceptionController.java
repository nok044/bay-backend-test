package com.nok.baybackendtest.controller;

import com.nok.baybackendtest.component.CustomHttpException;
import com.nok.baybackendtest.component.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        if(ex instanceof CustomHttpException){
            CustomHttpException exception = (CustomHttpException)ex;
            ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getClass().getSimpleName(), request.getDescription(false));
            return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
        }else {
            ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
