package com.acme.faculty.rest;

import com.acme.faculty.service.ConstraintViolationsException;
import com.acme.faculty.service.DeanExistsException;
import com.acme.faculty.service.FacultyWriteService;
import com.acme.faculty.service.NameExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static com.acme.faculty.rest.FacultyGetController.ID_PATTERN;
import static com.acme.faculty.rest.FacultyGetController.REST_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Controller zur Behandlung von POST- und PUT-Anfragen für Fakultäten.
 *
 * @author Ahmad Hawarnah
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class FacultyWriteController {
    private static final String PROBLEM_PATH = "/problem/";
    private final FacultyWriteService service;
    private final FacultyMapper mapper;

    /**
     * Fügt eine neue Fakultät hinzu.
     *
     * @param facultyDTO Die Daten der neuen Fakultät als DTO.
     * @param request    Die HTTP-Anfrage für die Generierung der Antwort-URI.
     * @return ResponseEntity mit dem URI der neu erstellten Fakultät im 'Location'-Header.
     * @throws URISyntaxException falls die URI nicht korrekt aufgebaut werden kann.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Eine neue Fakultaet anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Fakultaet neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Validierungsfehler")
    ResponseEntity<Void> post(@RequestBody final FacultyDTO facultyDTO, final HttpServletRequest request)
        throws URISyntaxException {
        log.info("Post Faculty: {}", facultyDTO);
        final var facultyInput = mapper.toFaculty(facultyDTO);
        final var faculty = service.create(facultyInput);
        log.info("Post Faculty: {}", faculty);
        final URI location = new URI(request.getRequestURL().toString() + faculty.getId());
        return created(location).build();
    }

    /**
     * Aktualisiert eine Fakultät anhand ihrer ID.
     *
     * @param id         Die ID der zu aktualisierenden Fakultät.
     * @param facultyDTO Die neuen Daten der Fakultät als DTO.
     */
    @Operation(summary = "Eine Fakultaet mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Fakultaet nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungueltige Werte, Name vorhanden oder Dekan ist vorhanden")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    void put(@PathVariable final UUID id, @RequestBody final FacultyDTO facultyDTO) {
        log.debug("put: id = {}, {}", id, facultyDTO);
        final var facultyInput = mapper.toFaculty(facultyDTO);
        service.update(id, facultyInput);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final ConstraintViolationsException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()
                .replace("creat.faculty", "")
                .replace("update.faculty", ""));
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onNameExists(final NameExistsException ex, final HttpServletRequest request) {
        log.debug("onNameExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.UNPROCESSABLE.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onDeanExists(final DeanExistsException ex, final HttpServletRequest request) {
        log.debug("onDeanExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.UNPROCESSABLE.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
