package com.acme.faculty.rest;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) für Informationen über einen Kurs.
 *
 * @author <a href="mailto:ahmad.hawarnah@example.com">Ahmad Hawarnah</a>
 * @param name Der Name des Kurses.
 */
public record CourseDTO(
    @NotBlank
    String name
) {
}
