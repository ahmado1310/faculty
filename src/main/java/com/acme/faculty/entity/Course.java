package com.acme.faculty.entity;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Repräsentiert einen Kurs, der von einer Bildungseinrichtung angeboten wird.
 * Diese Klasse enthält den Namen des Kurses.
 *
 * @author Ahmad Hawarnah
 */
@Getter
@Setter
@Builder
@ToString
public class Course {
    /**
     * Eindeutige Kennung des Kurses.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Name des Kurses.
     */
    @NotBlank
    private String name;
}
