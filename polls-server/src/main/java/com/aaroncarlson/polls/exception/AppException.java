package com.aaroncarlson.polls.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API will throw exceptions if the request is not valid or some unexpected situation occurs.
 * Want to respond with different status codes for different types of exceptions. Define the exception
 * along with the corresponding @ResponseStatus.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

}
