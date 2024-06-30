package com.acme.faculty.repository;

import com.acme.faculty.entity.Faculty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import java.util.Collection;
import java.util.ArrayList;

@Component
@Slf4j
@SuppressWarnings({"LambdaParameterName", "IllegalIdentifierName"})
public class SpecificationBuilder {

    /**
     * Specification für eine Query mit Spring Data bauen.
     *
     * @param queryParams als Map.
     * @return Specification für eine Query mit Spring Data
     */
    public Optional<Specification<Faculty>> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);
        if (queryParams.isEmpty()) {
            // keine Suchkriterien
            return Optional.empty();
        }
        final var specs = queryParams
            .entrySet()
            .stream()
            .map(this::toSpecification)
            .toList();
        if (specs.isEmpty() || specs.contains(null)) {
            return Optional.empty();
        }
        return Optional.of(Specification.allOf(specs));
    }

    @SuppressWarnings("CyclomaticComplexity")
    private Specification<Faculty> toSpecification(final Map.Entry<String, ? extends List<String>> entry) {
        log.trace("toSpecification: entry={}", entry);
        final var key = entry.getKey();
        final var values = entry.getValue();
        if (values == null || values.size() != 1) {
            return null;
        }
        final var value = values.get(0);
        return switch (key) {
            case "name" -> name(value);
            case "dean" -> dean(value);
            case "course" -> course(value);
            default -> null;
        };
    }

    private Specification<Faculty> name(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get("name")),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    private Specification<Faculty> dean(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get("dean").get("name")),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    private Specification<Faculty> course(final String teil) {
        return (root, query, builder) -> {
            query.distinct(true);
            return builder.like(
                builder.lower(root.join("courses").get("name")),
                builder.lower(builder.literal("%" + teil + '%'))
            );
        };
    }
}
