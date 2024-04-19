package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Faculty.
 *
 * @author Ahmad Hawarnah
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyReadService {
    private final FacultyRepository repository;

    /**
     * Sucht eine Fakultät anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Fakultät
     * @return Die gefundene Fakultät
     * @throws NotFoundException falls keine Fakultät mit der angegebenen ID gefunden wird
     */
    public @NonNull Faculty findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var faculty = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", faculty);
        return faculty;
    }

    /**
     * Ruft alle Fakultäten ab.
     *
     * @return Eine Liste aller Fakultäten
     */
    public @NonNull List<Faculty> findAll() {
        log.debug("findAll");
        return repository.findAll();
    }
}
