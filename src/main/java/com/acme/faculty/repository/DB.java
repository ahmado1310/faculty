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
public class DB {

    static final List<Faculty> FACULTIES = getFaculties();

    private DB() {
    }

    private static List<Faculty> getFaculties() {
        return Stream.of(
            Faculty.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .name("iwi")
                .dean(Dean.builder()
                    .name("Prof. Dr. Franz Nees")
                    .email("fraz.nees@iwi-hka.de")
                    .build())
                .courses(Stream.of(
                    Course.builder()
                        .name("Wirtschaftsinformatik")
                        .build(),
                    Course.builder()
                        .name("Informatik")
                        .build()
                ).collect(Collectors.toList()))
                .build(),
            Faculty.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("MET")
                .dean(Dean.builder()
                    .name("Prof. Dr. Leon Gauweiler")
                    .email("leon.gauweiler@iwi-hka.de")
                    .build())
                .courses(Stream.of(
                    Course.builder()
                        .name("Elektrotechnik")
                        .build(),
                    Course.builder()
                        .name("Maschinenbau")
                        .build()
                ).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());
    }
}
