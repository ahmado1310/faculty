package com.acme.faculty.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) für Informationen über einen Dekan.
 *
 * @author <a href="mailto:ahmad.hawarnah@example.com">Ahmad Hawarnah</a>
 * @param name  Der Name des Dekans.
 * @param email Die E-Mail-Adresse des Dekans.
 */
public record DeanDTO(
    @NotBlank
    String name,

    @Email
    String email
) {
}
