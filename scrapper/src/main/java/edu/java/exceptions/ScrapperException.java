package edu.java.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ScrapperException extends Exception {
    protected final String description;
    protected final HttpStatus httpStatusCode;

    public ScrapperException(String message, String description, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.description = description;
    }
}
