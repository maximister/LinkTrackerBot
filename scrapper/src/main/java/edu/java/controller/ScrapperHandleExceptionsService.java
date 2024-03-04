package edu.java.controller;

import edu.java.exceptions.ScrapperException;
import edu.java.model.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperHandleExceptionsService {
    //по идее каждую ошибку обрабатыать не обязательно и такое общее решение закроет большую часть потребностей

    //TODO: ResponseEntityExceptionHandler
    @ExceptionHandler(ScrapperException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperExceptions(ScrapperException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                ex.getDescription(),
                ex.getHttpStatusCode().toString(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            ),
            ex.getHttpStatusCode()
        );
    }
}
