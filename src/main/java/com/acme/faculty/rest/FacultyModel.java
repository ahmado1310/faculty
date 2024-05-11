package com.acme.faculty.rest;

import com.acme.faculty.entity.Course;
import com.acme.faculty.entity.Dean;
import com.acme.faculty.entity.Faculty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 * <img src="../../../../../asciidoc/KundeModel.svg" alt="Klassendiagramm">
 *
 * @author Ahmad Hawarnah
 */
@JsonPropertyOrder({
    "name", "dean", "courses"
})
@Relation(collectionRelation = "faculties", itemRelation = "faculty")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@ToString(callSuper = true)
class FacultyModel extends RepresentationModel<FacultyModel> {
    private final String name;
    private final Dean dean;
    private final List<Course> courses;

    FacultyModel(final Faculty faculty) {
        name = faculty.getName();
        dean = faculty.getDean();
        courses = faculty.getCourses();
    }
}
