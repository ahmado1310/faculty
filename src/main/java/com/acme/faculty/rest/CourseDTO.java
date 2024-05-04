package com.acme.faculty.rest;

/**
 * Data Transfer Object (DTO) für Informationen über einen Kurs.
 *
 * @author <a href="mailto:ahmad.hawarnah@example.com">Ahmad Hawarnah</a>
 * @param name Der Name des Kurses.
 */
public record CourseDTO(
    String name
) {
}
