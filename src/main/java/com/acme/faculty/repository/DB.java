package com.acme.faculty.repository;

import com.acme.faculty.entity.Course;
import com.acme.faculty.entity.Dean;
import com.acme.faculty.entity.Faculty;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Emulation der Datenbasis für persistente Fakultäten.
 */
final class DB {

    static final List<Faculty> FACULTIES = getFaculties();

    private DB() {
    }

    private static List<Faculty> getFaculties() {
        return Stream.of(
            Faculty.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .name("iwi")
                .dean(Dean.builder()
                    .id(UUID.fromString("10000000-0000-0000-0000-000000000000"))
                    .name("Prof. Dr. Franz Nees")
                    .email("fraz.nees@iwi-hka.de")
                    .build())
                .courses(Stream.of(
                    Course.builder()
                        .id(UUID.fromString("20000000-0000-0000-0000-000000000000"))
                        .name("Wirtschaftsinformatik")
                        .build(),
                    Course.builder()
                        .id(UUID.fromString("20000000-0000-0000-0000-000000000001"))
                        .name("Informatik")
                        .build()
                ).collect(Collectors.toList()))
                .build(),
            Faculty.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("MET")
                .dean(Dean.builder()
                    .id(UUID.fromString("10000000-0000-0000-0000-000000000001"))
                    .name("Prof. Dr. Leon Gauweiler")
                    .email("leon.gauweiler@iwi-hka.de")
                    .build())
                .courses(Stream.of(
                    Course.builder()
                        .id(UUID.fromString("20000000-0000-0000-0000-000000000002"))
                        .name("Elektrotechnik")
                        .build(),
                    Course.builder()
                        .id(UUID.fromString("20000000-0000-0000-0000-000000000003"))
                        .name("Maschinenbau")
                        .build()
                ).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());
    }
}
