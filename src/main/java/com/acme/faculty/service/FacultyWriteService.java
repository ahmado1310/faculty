package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import jakarta.validation.Validator;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Fakultäten.
 * Diese Klasse enthält die Geschäftslogik für das Erstellen und Aktualisieren von Fakultäten.
 * Sie validiert die Eingabedaten und überprüft, ob die Fakultät oder der Dekan bereits existieren.
 * Wenn keine Validierungsfehler auftreten und keine Duplikate gefunden werden, werden die Daten in der
 * Datenbank gespeichert bzw. aktualisiert.
 *
 * @author Ahmad Hawarnah
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyWriteService {
    private final FacultyRepository repository;
    private final Validator validator;

    /**
     * Erstellt eine neue Fakultät.
     *
     * @param faculty Die zu erstellende Fakultät.
     * @return Die erstellte Fakultät mit einer zugewiesenen ID.
     * @throws ConstraintViolationsException falls Validierungsfehler auftreten.
     * @throws NameExistsException falls ein Fakultätsname bereits existiert.
     * @throws DeanExistsException falls ein Dekan bereits existiert.
     */
    public @NonNull Faculty create(@NonNull final Faculty faculty) {
        log.debug("create: faculty={}", faculty);
        final var violations = validator.validate(faculty);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }
        if (repository.isNameExist(faculty.getName())) {
            log.debug("create: isNameExist={}", faculty.getName());
            throw new NameExistsException(faculty.getName());
        }
        if (repository.isDeanExist(faculty.getDean().getName())) {
            log.debug("create: isDeanExist={}", faculty.getDean().getName());
            throw new DeanExistsException(faculty.getDean().getName());
        }

        final var facultyDB = repository.creat(faculty);
        log.debug("create: faculty={}", facultyDB);
        return facultyDB;
    }

    /**
     * Aktualisiert eine vorhandene Fakultät.
     *
     * @param id      Die ID der zu aktualisierenden Fakultät.
     * @param faculty Die neuen Daten der Fakultät.
     * @throws ConstraintViolationsException falls Validierungsfehler auftreten.
     * @throws NotFoundException falls die Fakultät nicht gefunden wurde.
     * @throws DeanExistsException falls ein Dekan bereits existiert.
     */
    public void update(final UUID id, final Faculty faculty) {
        log.debug("update: FacultyID: {}", id);
        log.debug("update: {}", faculty);

        final var violations = validator.validate(faculty);
        if (!violations.isEmpty()) {
            log.debug("update: violations: {}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var facultyDbOptional = repository.findById(id);
        if (facultyDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        if (repository.isDeanExist(faculty.getDean().getName())) {
            throw new DeanExistsException(faculty.getDean().getName());
        }

        faculty.setId(id);
        repository.update(faculty);
    }
}
