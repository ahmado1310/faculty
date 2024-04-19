package com.acme.faculty.service;

import java.util.UUID;
import lombok.Getter;

/**
 * RuntimeException, falls keine Fakultät gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene id.
     */
    private final UUID id;

    NotFoundException(final UUID id) {
        super(STR."Keine Fakultaet mit der Id \{id} gefunden.");
        this.id = id;
    }
}
