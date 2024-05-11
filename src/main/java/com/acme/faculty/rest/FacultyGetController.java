package com.acme.faculty.rest;

import com.acme.faculty.service.FacultyReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.acme.faculty.rest.FacultyGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

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
     * @param request Das HTTP-Anfrageobjekt.
     * @return Die gefundene Fakultät mit Atom-Links.
     */
    @Operation(summary = "Suche nach ID", tags = "Pfad-Suche")
    @ApiResponse(responseCode = "200", description = "Fakultät gefunden")
    @ApiResponse(responseCode = "404", description = "Fakultät nicht gefunden")
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    FacultyModel getById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("getById: id={}, Thread={}", id, Thread.currentThread().getName());

        // Geschäftslogik bzw. Anwendungskern
        final var faculty = service.findById(id);

        // HATEOAS
        final var model = new FacultyModel(faculty);

        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + faculty.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        log.debug("getById: {}", model);
        return model;
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
    CollectionModel<FacultyModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> searchCriteria,
        final HttpServletRequest request
    ) {
        log.debug("get: searchCriteria={}", searchCriteria);
        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik bzw. Anwendungskern
        final var models = service.find(searchCriteria)
            .stream()
            .map(faculty -> {
                final var model = new FacultyModel(faculty);
                model.add(Link.of(baseUri + '/' + faculty.getId()));
                return model;
            })
            .collect(Collectors.toList());

        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }
}
