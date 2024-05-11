package com.acme.faculty.rest;

import com.acme.faculty.entity.Course;
import com.acme.faculty.entity.Dean;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * Data Transfer Object (DTO) für Fakultätsinformationen.
 *
 * @author <a href="mailto:ahmad.hawarnah@example.com">Ahmad Hawarnah</a>
 * @param name    Der Name der Fakultät. Gültiger name die Fakultäten d.h. mit einem geeigneten Muster.
 * @param dean    Der zugehörige Dekan der Fakultät.
 * @param courses Eine Liste der Kurse, die von der Fakultät angeboten werden.
 */
public record FacultyDTO(
    @NotBlank
    String name,

    @Valid
    @NotNull(groups = OnCreate.class)
    Dean dean,

    @NotEmpty
    @UniqueElements
    List<Course> courses
) {
    /**
     * Marker-Interface für Jakarta Validation: zusätzliche Validierung beim Neuanlegen.
     */
    interface OnCreate { }
}
