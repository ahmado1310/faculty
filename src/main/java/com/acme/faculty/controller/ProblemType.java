package com.acme.faculty.controller;

/**
 * Enum für ProblemDetail.type.
 *
 * @author Ahmad Hawarnah
 */
enum ProblemType {

    NOT_FOUND("Not_Found"),
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),

    /**
     * Fehler, wenn z.B. Emailadresse bereits existiert.
     */
    UNPROCESSABLE("unprocessable");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
