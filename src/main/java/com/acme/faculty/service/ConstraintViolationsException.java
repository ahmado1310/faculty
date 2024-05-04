package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import lombok.Getter;

/**
 * Exception, falls es mindestens ein verletztes Constraint gibt.
 *
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {
    /**
     * Die verletzten Constraints.
     */
    private final transient Collection<ConstraintViolation<Faculty>> violations;
    ConstraintViolationsException(
        final Collection<ConstraintViolation<Faculty>> violations
    ) {
        super("Constraints sind verletzt");
        this.violations = violations;
    }
}
