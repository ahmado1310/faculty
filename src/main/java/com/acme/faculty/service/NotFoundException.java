package com.acme.faculty.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

/**
 * RuntimeException, falls keine Fakultät gefunden wurde.
 */
@SuppressWarnings("StringTemplateMigration")
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene id.
     */
    private final UUID id;
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super("Keine Fakultaet mit der Id " + id +  " gefunden.");
        this.id = id;
        suchkriterien = null;
    }

    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keine entsprechende Fakultät mit diesen Suchkriterien gefunden.");
        this.id = null;
        this.suchkriterien = suchkriterien;
    }
}
