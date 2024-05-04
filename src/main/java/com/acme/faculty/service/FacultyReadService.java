package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * Fakultät anhand von searchCriteria als Collection suchen.
     *
     * @param searchCriteria Die searchCriteria
     * @return Die gefundene Fakultät oder eine leere Collection
     * @throws NotFoundException Falls keine Kunden gefunden wurden
     */
    public @NonNull Collection<Faculty> find(@NonNull final Map<String, List<String>> searchCriteria) {
        log.debug("find: searchCriteria={}", searchCriteria);

        if (searchCriteria.isEmpty()) {
            return repository.findAll();
        }

        if (searchCriteria.size() == 1) {
            final var name = searchCriteria.get("name");
            if (name != null && name.size() == 1) {
                final var faculty = repository.findByName(name.getFirst());
                if (faculty.isEmpty()) {
                    throw new NotFoundException(searchCriteria);
                }
                log.debug("find (name): {}", faculty);
                return faculty;
            }
        }

        final var faculty = repository.find(searchCriteria);
        if (faculty.isEmpty()) {
            throw new NotFoundException(searchCriteria);
        }
        log.debug("find: {}", faculty);
        return faculty;
    }
}
