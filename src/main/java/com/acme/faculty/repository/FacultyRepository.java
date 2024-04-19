package com.acme.faculty.repository;

import com.acme.faculty.entity.Faculty;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import static com.acme.faculty.repository.DB.FACULTIES;
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
     * Ermittelt alle Fakultäten als Collection, wie sie später auch aus der Datenbank kommen.
     *
     * @return Alle Fakultäten
     */
    public List<Faculty> findAll() {
        return FACULTIES;
    }

}
