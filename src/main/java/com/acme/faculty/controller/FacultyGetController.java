package com.acme.faculty.controller;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.service.FacultyReadService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.acme.faculty.controller.FacultyGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Stellt einen REST-Controller für den Zugriff auf Fakultätsdaten bereit.
 * Dieser Controller bietet Endpunkte für das Abrufen von Fakultätsdaten.
 * <img src="../../../../../../../extras/compose/doc/FacultyGetController.svg" alt="Beschreibung">
 *
 * @author Ahmad Hawarnah
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Faculty API", version = "v0"))
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"java:S1075", "java:S6204", "java:S6856"})
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
    private final UriHelper uriHelper;

    /**
     * Sucht eine Fakultät anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Fakultät.
     * @param version Versionsnummer aus dem Header If-None-Match
     * @param request Das HTTP-Anfrageobjekt.
     * @param authentication Authentication-Objekt für Security
     * @return Ein ResponseEntity mit der gefundenen Fakultät oder dem entsprechenden Statuscode.
     */
    @Operation(summary = "Suche nach ID", tags = "Pfad-Suche")
    @ApiResponse(responseCode = "200", description = "Fakultät gefunden")
    @ApiResponse(responseCode = "404", description = "Fakultät nicht gefunden")
    @Observed(name = "get-by-id")
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    public ResponseEntity<FacultyModel> getById(
        @PathVariable final UUID id,
        @RequestHeader("If-None-Match") final Optional<String> version,
        final HttpServletRequest request,
        final Authentication authentication) {

        final var user = (UserDetails) authentication.getPrincipal();
        log.debug("getById: id={}, version={}, user={}", id, version, user);

        if (user == null) {
            log.error("Trotz Spring Security wurde getById() ohne Benutzerkennung aufgerufen");
            return status(FORBIDDEN).build();
        }

        final var faculty = service.findById(id, user);
        log.debug("getById: {}", faculty);

        final var currentVersion = "\"" + faculty.getVersion() + '"';
        if (version.orElse(null).equals(currentVersion)) {
            return status(NOT_MODIFIED).build();
        }

        final var model = facultyToModel(faculty, request);
        log.debug("getById: model={}", model);
        return ok().eTag(currentVersion).body(model);
    }

    /**
     * Sucht nach Fakultäten anhand der angegebenen Suchkriterien.
     *
     * @param searchCriteria Die Suchkriterien, die verwendet werden sollen.
     * @param request Das HTTP-Anfrageobjekt.
     * @return Eine Sammlung von Fakultäten, die den angegebenen Suchkriterien entsprechen, als CollectionModel.
     */
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Collection mit den Fakultäten")
    @ApiResponse(responseCode = "404", description = "keine Fakultäten gefunden")
    @GetMapping(produces = HAL_JSON_VALUE)
    public CollectionModel<FacultyModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> searchCriteria,
        final HttpServletRequest request) {

        log.debug("get: searchCriteria={}", searchCriteria);
        final var baseUri = uriHelper.getBaseUri(request).toString();

        final var models = service.find(searchCriteria)
            .stream()
            .map(faculty -> {
                final var model = new FacultyModel(faculty);
                model.add(Link.of(baseUri + '/' + faculty.getId()));
                return model;
            })
            .toList();

        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }

    /**
     * Hilfsmethode, um eine Faculty-Instanz in ein FacultyModel zu konvertieren und HATEOAS-Links hinzuzufügen.
     *
     * @param faculty Die zu konvertierende Faculty-Instanz.
     * @param request Das HTTP-Anfrageobjekt.
     * @return Das konvertierte FacultyModel mit HATEOAS-Links.
     */
    private FacultyModel facultyToModel(final Faculty faculty, final HttpServletRequest request) {
        final var model = new FacultyModel(faculty);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + faculty.getId();

        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));

        model.add(selfLink, listLink, addLink, updateLink, removeLink);
        return model;
    }
}
