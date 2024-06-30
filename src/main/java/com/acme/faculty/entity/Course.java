package com.acme.faculty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Repräsentiert einen Kurs, der von einer Bildungseinrichtung angeboten wird.
 * Diese Klasse enthält den Namen des Kurses.
 *
 * @author Ahmad Hawarnah
 */
@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Course {
    /**
     * Eindeutige Kennung des Kurses.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Name des Kurses.
     */
    @NotBlank
    private String name;
}
