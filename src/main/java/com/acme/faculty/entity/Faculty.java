package com.acme.faculty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Repräsentiert eine Fakultät in der Bildungseinrichtung.
 * <img src="../../../../../../../extras/compose/doc/Faculty_Dean_Course_UUID_List.svg" alt="Klassendiagramm">
 *
 * @author Ahmad Hawarnah
 */
@Entity
@Table(name = "faculty")
@NamedEntityGraph(name = Faculty.DEAN_GRAPH, attributeNodes = @NamedAttributeNode("dean"))
@NamedEntityGraph(name = Faculty.DEAN_COURSES_GRAPH, attributeNodes =
    {@NamedAttributeNode("dean"), @NamedAttributeNode("courses")})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder
@Getter
@Setter
@ToString
public class Faculty {
    public static final String DEAN_GRAPH = "Faculty.dean";
    public static final String DEAN_COURSES_GRAPH = "Faculty.deanCourses";

    /**
     * Eindeutige Kennung für die Fakultät.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Versionsnummer für optimistische Synchronisation.
     */
    @Version
    private int version;

    /**
     * Name der Fakultät.
     */
    @NotBlank
    private String name;

    /**
     * Dekan der Fakultät.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @ToString.Exclude
    @Valid
    private Dean dean;

    /**
     * Liste der von der Fakultät angebotenen Kurse.
     */
    // Default: fetch=LAZY
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "faculty_id")
    @OrderColumn(name = "idx", nullable = false)
    @NotEmpty
    @UniqueElements
    @Valid
    private List<Course> courses;
}
