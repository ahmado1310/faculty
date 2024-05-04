package com.acme.faculty.rest;

import com.acme.faculty.entity.Course;
import com.acme.faculty.entity.Dean;
import java.util.List;

/**
 * Data Transfer Object (DTO) für Fakultätsinformationen.
 *
 * @author <a href="mailto:ahmad.hawarnah@example.com">Ahmad Hawarnah</a>
 * @param name    Der Name der Fakultät.
 * @param dean    Der zugehörige Dekan der Fakultät.
 * @param courses Eine Liste der Kurse, die von der Fakultät angeboten werden.
 */
public record FacultyDTO(
    String name,
    Dean dean,
    List<Course> courses
) {
}
