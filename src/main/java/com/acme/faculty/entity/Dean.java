package com.acme.faculty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
 * Repräsentiert einen Dekan an einer Bildungseinrichtung.
 * Diese Klasse enthält den Namen und die E-Mail-Adresse des Dekans.
 *
 * @author Ahmad Hawarnah
 */
@Entity
@Table(name = "dean")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Dean {
    /**
     * Die eindeutige Kennung des Dekans.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Name des Dekans.
     */
    @NotBlank
    private String name;

    /**
     * Die E-Mail-Adresse des Dekans.
     */
    @Email
    private String email;
}
