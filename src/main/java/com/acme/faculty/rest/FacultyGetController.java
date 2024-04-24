package com.acme.faculty.rest;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.service.FacultyReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.acme.faculty.rest.FacultyGetController.REST_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Stellt einen REST-Controller für den Zugriff auf Fakultätsdaten bereit.
 * Dieser Controller bietet Endpunkte für das Abrufen von Fakultätsdaten.
 *
 * @author Ahmad Hawarnah
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Faculty API", version = "v0"))
@RequiredArgsConstructor
@Slf4j
public class FacultyGetController {
    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";

    /**
     * Muster für eine UUID.
     */
    public static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";

    private final FacultyReadService service;

    /**
     * Sucht eine Fakultät anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Fakultät
     * @return Die gefundene Fakultät
     */
    @Operation(summary = "Suche nach ID", tags = "Pfad-Suche")
    @ApiResponse(responseCode = "200", description = "Fakultät gefunden")
    @ApiResponse(responseCode = "404", description = "Fakultät nicht gefunden")
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    Faculty getById(@PathVariable final UUID id) {
        log.debug("getById: id{}", id);
        final var faculty = service.findById(id);
        log.debug("getById: faculty{}", faculty);
        return faculty;
    }

    /**
     * Sucht nach Fakultäten anhand der angegebenen Suchkriterien.
     *
     * @param searchCriteria Die Suchkriterien, die verwendet werden sollen.
     * @return Eine Sammlung von Fakultäten, die den angegebenen Suchkriterien entsprechen.
     */
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Collection mit den Fakultaeten")
    @ApiResponse(responseCode = "404", description = "keine Fakultaeten gefunden")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<Faculty> get(
        @RequestParam @NonNull final MultiValueMap<String, String> searchCriteria
    ) {
        log.debug("get: searchCriteria={}", searchCriteria);
        final var faculties = service.find(searchCriteria);
        log.debug("get {}", faculties);
        return faculties;
    }

}
