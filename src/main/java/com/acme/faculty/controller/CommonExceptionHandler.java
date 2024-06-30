package com.acme.faculty.controller;

import com.acme.faculty.service.DeanExistsException;
import com.acme.faculty.service.NameExistsException;
import com.acme.faculty.service.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static com.acme.faculty.controller.FacultyWriteController.PROBLEM_PATH;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Handler für allgemeine Exceptions.
 *
 * @author Ahmad Hawarnah
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    /**
     * Behandelt Ausnahmen, die auftreten, wenn eine Ressource nicht gefunden wird.
     *
     * @param ex Die Ausnahme, die aufgetreten ist.
     * @param request Die HTTP-Anfrage.
     * @return Das ProblemDetail-Objekt mit dem entsprechenden Status und Details.
     */
    @ExceptionHandler
    public ProblemDetail onNotFound(final NotFoundException ex, final HttpServletRequest request) {
        log.debug("NotFoundException: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.NOT_FOUND.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    /**
     * Behandelt Ausnahmen, die auftreten, wenn ein Name bereits vorhanden ist.
     *
     * @param ex Die Ausnahme, die aufgetreten ist.
     * @param request Die HTTP-Anfrage.
     * @return Das ProblemDetail-Objekt mit dem entsprechenden Status und Details.
     */
    @ExceptionHandler
    ProblemDetail onNameExists(final NameExistsException ex, final HttpServletRequest request) {
        log.debug("onNameExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.UNPROCESSABLE.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    /**
     * Behandelt Ausnahmen, die auftreten, wenn ein Dekan bereits vorhanden ist.
     *
     * @param ex Die Ausnahme, die aufgetreten ist.
     * @param request Die HTTP-Anfrage.
     * @return Das ProblemDetail-Objekt mit dem entsprechenden Status und Details.
     */
    @ExceptionHandler
    ProblemDetail onDeanExists(final DeanExistsException ex, final HttpServletRequest request) {
        log.debug("onDeanExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.UNPROCESSABLE.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
