package edu.java.bot.controller;

import edu.java.bot.exceptions.BotException;
import edu.java.bot.model.controllerDto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BotHandleExceptionService {
    //TODO: ResponseEntityExceptionHandler
    @ExceptionHandler(BotException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperExceptions(BotException ex) {
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
