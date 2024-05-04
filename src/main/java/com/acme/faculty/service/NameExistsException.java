package com.acme.faculty.service;

import lombok.Getter;

/**
 * Exception, falls der Name bereits existiert.
 *
 * @author <a href="mailto:haah1014@h-ka.de">Ahmad Hawarnah</a>
 */
@Getter
public class NameExistsException extends RuntimeException {
    /**
     * Bereits vorhandener Fakultätsname.
     */
    private final String name;

    NameExistsException(final String name) {
        super("Der Name " + name + " existiert bereits");
        this.name = name;
    }
}
