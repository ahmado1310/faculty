package com.acme.faculty.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Repräsentiert einen Dekan an einer Bildungseinrichtung.
 *
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
