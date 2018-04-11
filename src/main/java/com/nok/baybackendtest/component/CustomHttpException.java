package com.nok.baybackendtest.component;

import org.springframework.http.HttpStatus;

public abstract class CustomHttpException extends RuntimeException {

    abstract public HttpStatus getHttpStatus();
}
