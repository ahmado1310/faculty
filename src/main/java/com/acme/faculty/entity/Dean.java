package com.acme.faculty.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Repräsentiert einen Dekan an einer Bildungseinrichtung.
 * Diese Klasse enthält den Namen und die E-Mail-Adresse des Dekans.
 *
 * @author Ahmad Hawarnah
 */
@Getter
@Setter
@Builder
@ToString
public class Dean {
    /**
     * Die eindeutige Kennung des Dekans.
     */
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
