package com.acme.faculty.service;

import lombok.Getter;

/**
 * Exception, falls der Dekan bereits existiert.
 *
 * @author <a href="mailto:haah1014@h-ka.de">Ahmad Hawarnah</a>
 */
@Getter
public class DeanExistsException extends RuntimeException {
    /**
     * Bereits vorhandener Dekan.
     */
    private final String dekan;

    DeanExistsException(final String dekan) {
        super("Der Dekan " + dekan +  "ist ein Dekan von andere Fakultaet");
        this.dekan = dekan;
    }
}
