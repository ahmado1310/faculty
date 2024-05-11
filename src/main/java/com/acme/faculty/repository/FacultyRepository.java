package com.acme.faculty.repository;

import com.acme.faculty.entity.Faculty;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import static com.acme.faculty.repository.DB.FACULTIES;
import static java.util.Collections.emptyList;

/**
 * Repository für den DB-Zugriff bei Kunden.
 *
 * @author Ahmad Hawarnah
 */
@Repository
@Slf4j
public class FacultyRepository {
    /**
     * Sucht eine Fakultät anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Fakultät
     * @return Ein Optional, das die gefundene Fakultät enthält, oder ein leeres Optional
     */
    public Optional<Faculty> findById(final UUID id) {
        return FACULTIES.stream()
            .filter(faculty -> Objects.equals(faculty.getId(), id))
            .findFirst();
    }

    /**
     * Fakultät anhand von searchCriteria ermitteln.
     * Z.B. mit GET <a href="https://localhost:8080/rest?name=Informatik">
     * https://localhost:8080/rest?name=Informatik</a>
     *
     * @param searchCriteria searchCriteria.
     * @return Gefundene Fakultät oder leere Collection.
     */
    @SuppressWarnings("UnreachableCode")
    public @NotNull Collection<Faculty> find(final Map<String, ? extends List<String>> searchCriteria) {
        log.debug("find: searchCriteria={}", searchCriteria);

        if (searchCriteria.isEmpty()) {
            return findAll();
        }

        for (final var entry : searchCriteria.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> {
                    return findByName(entry.getValue().getFirst());
                }
                case "course" -> {
                    return findByCourse(entry.getValue().getFirst());
                }
                case "dean" -> {
                    return findByDean(entry.getValue().getFirst());
                }
                default -> {
                    log.debug("find: ungültiges Suchkriterium={}", entry.getKey());
                    return emptyList();
                }
            }
        }
        return emptyList();
    }

    /**
     * Ermittelt alle Fakultäten als Collection, wie sie später auch aus der Datenbank kommen.
     *
     * @return Alle Fakultäten
     */
    public List<Faculty> findAll() {
        return FACULTIES;
    }

    /**
     * Findet Fakultäten anhand eines Namensbestandteils.
     *
     * @param name Teil des Namens, nach dem gefiltert wird.
     * @return Sammlung von Fakultäten, deren Namen den Suchbegriff enthalten.
     */
    public Collection<Faculty> findByName(final String name) {
        log.debug("findByName: name={}", name);
        final var faculties = FACULTIES.stream()
            .filter(faculty -> faculty.getName().contains(name))
            .toList();
        log.debug("findByName: faculty={}", faculties);
        return faculties;
    }

    /**
     * Findet Fakultäten anhand der Zugehörigkeit eines Dekans.
     *
     * @param dean Suchbegriff für den Namen des Dekans.
     * @return Sammlung von Fakultäten, in denen der gesuchte Dekan zugeordnet ist.
     */
    public Collection<Faculty> findByDean(final String dean) {
        log.debug("findByDekan: dean={}", dean);
        final var faculties = FACULTIES.stream()
            .filter(faculty -> faculty.getDean().getName().contains(dean))
            .toList();
        log.debug("findByDekan: {}", faculties);
        return faculties;
    }

    /**
     * Findet Fakultäten anhand des Namens eines Kurses.
     *
     * @param course Suchbegriff für den Namen des Kurses.
     * @return Eine Sammlung von Fakultäten, die den gesuchten Kurs anbieten.
     */
    public Collection<Faculty> findByCourse(final String course) {
        log.debug("findByCourse: course={}", course);
        final var faculties = FACULTIES.stream()
            .filter(faculty -> faculty.getCourses().stream()
                .anyMatch(c -> c.getName().contains(course)))
            .toList();
        log.debug("findByCourse: {}", faculties);
        return faculties;
    }

    /**
     * Überprüft, ob ein Fakultätsname bereits existiert.
     *
     * @param name Der zu überprüfende Fakultätsname
     * @return true, wenn ein Fakultätsname existiert, der den angegebenen Namen enthält, ansonsten false
     */
    public boolean isNameExist(final String name) {
        log.debug("isNameExist: name={}", name);
        final var count = FACULTIES.stream()
            .filter(faculty -> faculty.getName().contains(name))
            .count();
        log.debug("isNameExist: count={}", count);
        return count > 0L;
    }

    /**
     * Überprüft, ob ein Dekan bereits existiert.
     *
     * @param dean Der zu überprüfende Name des Dekans
     * @return true, wenn ein Dekan existiert, der den angegebenen Namen enthält, ansonsten false
     */
    public boolean isDeanExist(final String dean) {
        log.debug("isDeanExist: dean={}", dean);
        final var count = FACULTIES.stream()
            .filter(faculty -> faculty.getDean().getName().contains(dean))
            .count();
        log.debug("isDeanExist: count={}", count);
        return count > 0L;
    }

    /**
     * Erstellt eine neue Fakultät und weist ihr eine eindeutige ID zu.
     *
     * @param faculty Die zu erstellende Fakultät
     * @return Die erstellte Fakultät mit zugewiesener ID
     */
    public @NotNull Faculty creat(final @NotNull Faculty faculty) {
        log.debug("creat: faculty={}", faculty);
        final var facultyId = UUID.randomUUID();
        faculty.setId(facultyId);
        FACULTIES.add(faculty);
        log.debug("creat: faculty={}", faculty);
        return faculty;
    }

    /**
     * Aktualisiert die Informationen einer vorhandenen Fakultät.
     * Wenn die Fakultät nicht in der Datenbank vorhanden ist, wird keine Aktualisierung durchgeführt.
     *
     * @param faculty Die zu aktualisierende Fakultät
     */
    public void update(final @NotNull Faculty faculty) {
        log.debug("update: faculty={}", faculty);
        final OptionalInt index = IntStream
            .range(0, FACULTIES.size())
            .filter(i -> Objects.equals(FACULTIES.get(i).getId(), faculty.getId()))
            .findFirst();
        log.trace("update: index={}", index);

        if (index.isEmpty()) {
            return;
        }

        FACULTIES.set(index.getAsInt(), faculty);
        log.debug("update: faculty={}", faculty);
    }
}
