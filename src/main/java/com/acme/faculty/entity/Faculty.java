package com.acme.faculty.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * Repräsentiert eine Fakultät in der Bildungseinrichtung.
 *
 * @author Ahmad Hawarnah
 */
@Builder
@Getter
@Setter
@ToString
public class Faculty {
    /**
     * Eindeutige Kennung für die Fakultät.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Name der Fakultät.
     */
    @NotBlank
    private String name;

    /**
     * Dekan der Fakultät.
     */
    @Valid
    private Dean dean;

    /**
     * Liste der von der Fakultät angebotenen Kurse.
     */
    @NotEmpty
    @UniqueElements
    private List<Course> courses;
}
