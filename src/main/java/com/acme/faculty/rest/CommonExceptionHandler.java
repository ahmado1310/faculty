package com.acme.faculty.rest;

import com.acme.faculty.service.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Handler für allgemeine Exceptions.
 *
 * @author Ahmad Hawarnah
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());
    }
}
