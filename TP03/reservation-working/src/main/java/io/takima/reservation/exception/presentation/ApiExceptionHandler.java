package io.takima.reservation.exception.presentation;

import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.exception.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger;

    public ApiExceptionHandler() {
        this.logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    }

    private ResponseEntity<ExceptionDto> makeResponse(Exception e, HttpStatus status) {
        logger.error(e.getMessage(), e);

        var exceptionDto = new ExceptionDto(e);
        return ResponseEntity.status(status).body(exceptionDto);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionDto> handleInvalidInputException(InvalidInputException e, WebRequest r) {
        return makeResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> handleNoSuchElementException(NoSuchElementException e, WebRequest r) {
        return makeResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException e, WebRequest r) {
        return makeResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
