package com.acme.faculty.service;

import com.acme.faculty.entity.Faculty;
import com.acme.faculty.repository.FacultyRepository;
import com.acme.faculty.repository.SpecificationBuilder;
import com.acme.faculty.security.Rolle;
import io.micrometer.observation.annotation.Observed;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.acme.faculty.security.Rolle.ADMIN;

/**
 * Anwendungslogik für Faculty.
 * <img src="../../../../../../../extras/compose/doc/FacultyReadService.svg" alt="Klassendiagramm">
 *
 * @author Ahmad Hawarnah
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FacultyReadService {
    private final FacultyRepository repo;
    private final SpecificationBuilder specificationBuilder;

    /**
     * Sucht eine Fakultät anhand ihrer ID.
     *
     * @param id Die ID der gesuchten Fakultät
     * @param username UserDetails-Objekt
     * @return Die gefundene Fakultät
     * @throws NotFoundException falls keine Fakultät mit der angegebenen ID gefunden wird
     * @throws AccessForbiddenException falls die erforderlichen Rollen nicht gegeben sind
     */
    @Observed(name = "find-by-id")
    public @NonNull Faculty findById(
        final UUID id,
        final String username,
        final List<Rolle> rollen,
        final boolean fetchDeanAndCourses) {
        log.debug("findById: id={}, username={}, rollen={}", id, username, rollen);
        final var facultyOptional = fetchDeanAndCourses ? repo.findById(id) : repo.findByIdFetchDeanAndCourses(id);
        final var faculty = facultyOptional.orElse(null);
        log.trace("findById: faculty={}", faculty);

        if (faculty != null && faculty.getUsername().contentEquals(username.getUsername())) {
            return faculty;
        }

        final var roles = username
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .map(str -> str.substring(Rolle.ROLE_PREFIX.length()))
            .map(Rolle::valueOf)
            .toList();

        if (!roles.contains(ADMIN)) {
            throw new AccessForbiddenException(roles);
        }

        if (faculty == null) {
            throw new NotFoundException(id);
        }

        log.debug("findById: faculty={}", faculty);
        return faculty;
    }

    /**
     * Fakultät anhand von Suchkriterien als Collection suchen.
     *
     * @param searchCriteria Die Suchkriterien
     * @return Die gefundene Fakultät oder eine leere Collection
     * @throws NotFoundException falls keine Fakultät gefunden wurden
     */
    @SuppressWarnings("ReturnCount")
    public @NonNull Collection<Faculty> find(@NonNull final Map<String, List<String>> searchCriteria) {
        log.debug("find: searchCriteria={}", searchCriteria);
        if (searchCriteria.isEmpty()) {
            return repo.findAll();
        }

        if (searchCriteria.size() == 1) {
            final var names = searchCriteria.get("name");
            if (names != null && names.size() == 1) {
                return findByName(names.get(0), searchCriteria);
            }
        }

        final var specification = specificationBuilder
            .build(searchCriteria)
            .orElseThrow(() -> new NotFoundException(searchCriteria));
        final var faculties = repo.findAll(specification);

        if (faculties.isEmpty()) {
            throw new NotFoundException(searchCriteria);
        }

        log.debug("find: {}", faculties);
        return faculties;
    }

    private @NonNull Collection<Faculty> findByName(final String name, final Map<String, List<String>> searchCriteria) {
        log.trace("findByName: {}", name);
        final var faculties = repo.findByName(name);

        if (faculties.isEmpty()) {
            throw new NotFoundException(searchCriteria);
        }

        log.debug("findByName: {}", faculties);
        return faculties;
    }
}
