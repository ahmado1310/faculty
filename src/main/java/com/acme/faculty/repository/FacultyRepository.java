package com.acme.faculty.repository;

import com.acme.faculty.entity.Faculty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Collection;

import static com.acme.faculty.entity.Faculty.DEAN_COURSES_GRAPH;

/**
 * Repository für den DB-Zugriff bei Fakultäten.
 *
 * @author Ahmad Hawarnah
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, UUID>, JpaSpecificationExecutor<Faculty> {

    @EntityGraph(DEAN_COURSES_GRAPH)
    @NonNull
    @Override
    List<Faculty> findAll();

    @EntityGraph(DEAN_COURSES_GRAPH)
    @NonNull
    @Override
    List<Faculty> findAll(@NonNull Specification<Faculty> spec);

    @EntityGraph(DEAN_COURSES_GRAPH)
    @NonNull
    @Override
    Optional<Faculty> findById(@NonNull UUID id);

    /**
     * Faculty einschließlich Dean und Courses anhand der ID suchen.
     *
     * @param id Faculty ID
     * @return Gefundene Faculty
     */
    @Query("""
        SELECT DISTINCT f
        FROM #{#entityName} f
        WHERE f.id = :id
    """)
    @EntityGraph(DEAN_COURSES_GRAPH)
    @NonNull
    Optional<Faculty> findByIdFetchDeanAndCourses(UUID id);

    /**
     * Faculty anhand des Namens suchen.
     *
     * @param name Der (Teil-)Name der gesuchten Faculties
     * @return Die gefundenen Faculties oder eine leere Collection
     */
    @Query("""
        SELECT f
        FROM #{#entityName} f
        WHERE lower(f.name) LIKE concat('%', lower(:name), '%')
        ORDER BY f.name
    """)
    @EntityGraph(DEAN_COURSES_GRAPH)
    List<Faculty> findByName(CharSequence name);

    /**
     * Findet Fakultäten anhand der Zugehörigkeit eines Dekans.
     *
     * @param dean Suchbegriff für den Namen des Dekans.
     * @return Sammlung von Fakultäten, in denen der gesuchte Dekan zugeordnet ist.
     */
    @Query("""
        SELECT f
        FROM #{#entityName} f
        JOIN f.dean d
        WHERE lower(d.name) LIKE concat('%', lower(:dean), '%')
    """)
    @EntityGraph(DEAN_COURSES_GRAPH)
    Collection<Faculty> findByDean(CharSequence dean);

    /**
     * Findet Fakultäten anhand des Namens eines Kurses.
     *
     * @param course Suchbegriff für den Namen des Kurses.
     * @return Eine Sammlung von Fakultäten, die den gesuchten Kurs anbieten.
     */
    @Query("""
        SELECT DISTINCT f
        FROM #{#entityName} f
        JOIN f.courses c
        WHERE lower(c.name) LIKE concat('%', lower(:course), '%')
    """)
    @EntityGraph(DEAN_COURSES_GRAPH)
    Collection<Faculty> findByCourse(CharSequence course);

    /**
     * Überprüft, ob ein Fakultätsname bereits existiert.
     *
     * @param name Der zu überprüfende Fakultätsname
     * @return true, wenn ein Fakultätsname existiert, der den angegebenen Namen enthält, ansonsten false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean isNameExist(String name);

    /**
     * Überprüft, ob ein Dekan bereits existiert.
     *
     * @param dean Der zu überprüfende Name des Dekans
     * @return true, wenn ein Dekan existiert, der den angegebenen Namen enthält, ansonsten false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean isDeanExist(String dean);
}
