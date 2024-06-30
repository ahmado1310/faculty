package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.bytecode.enhance.VersionMismatchException;
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
    @Transactional
    public Faculty create(final Faculty faculty) {
        log.debug("create: faculty={}", faculty);

        if (repository.isNameExist(faculty.getName())) {
            log.debug("create: isNameExist={}", faculty.getName());
            throw new NameExistsException(faculty.getName());
        }

        if (repository.isDeanExist(faculty.getDean().getName())) {
            log.debug("create: isDeanExist={}", faculty.getDean().getName());
            throw new DeanExistsException(faculty.getDean().getName());
        }

        final var facultyDB = repository.save(faculty);
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
    @Transactional
    public Faculty update(final UUID id, final Faculty faculty, final int version) {
        log.debug("update: {}", faculty);
        log.debug("update: id={}, version={}", id, version);

        var facultyDb = repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.trace("update: faculty={}", facultyDb);

        if (version != facultyDb.getVersion()) {
            throw  new VersionOutdatedException(version);
        }

        if (repository.isDeanExist(faculty.getDean().getName())) {
            throw new DeanExistsException(faculty.getDean().getName());
        }

        facultyDb.setId(id);
        facultyDb = repository.save(facultyDb);
        return facultyDb;
    }
}
