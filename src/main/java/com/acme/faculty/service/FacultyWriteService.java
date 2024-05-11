package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Fakultäten.
 * Diese Klasse enthält die Geschäftslogik für das Erstellen und Aktualisieren von Fakultäten.
 * Sie überprüft die Eingabedaten, ob die Fakultät oder der Dekan bereits existieren.
 * Wenn keine Duplikate gefunden werden, werden die Daten in der
 * Datenbank gespeichert bzw. aktualisiert.
 * <img src="../../../../../../../extras/compose/doc/FacultyWriteService.svg" alt="Klassendiagramm">
 *
 * @author Ahmad Hawarnah
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyWriteService {
    private final FacultyRepository repository;

    /**
     * Erstellt eine neue Fakultät.
     *
     * @param faculty Die zu erstellende Fakultät.
     * @return Die erstellte Fakultät mit einer zugewiesenen ID.
     * @throws NameExistsException           falls ein Fakultätsname bereits existiert.
     * @throws DeanExistsException           falls ein Dekan bereits existiert.
     */
    public @NonNull Faculty create(@NonNull final Faculty faculty) {
        log.debug("create: faculty={}", faculty);

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
     * @throws NotFoundException             falls die Fakultät nicht gefunden wurde.
     * @throws DeanExistsException           falls ein Dekan bereits existiert.
     */
    public void update(final UUID id, final Faculty faculty) {
        log.debug("update: FacultyID: {}", id);
        log.debug("update: {}", faculty);

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
