package com.nok.baybackendtest.exception;

import com.nok.baybackendtest.component.CustomHttpException;
import org.springframework.http.HttpStatus;

public class MininumSalaryException extends CustomHttpException {

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
