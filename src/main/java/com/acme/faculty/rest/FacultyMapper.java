package com.acme.faculty.rest;

import com.acme.faculty.entity.Course;
import com.acme.faculty.entity.Dean;
import com.acme.faculty.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen.
 * Siehe build\generated\sources\annotationProcessor\java\main\...\KundeMapperImpl.java.
 *
 * @author <a href="mailto:haah104@h-ka.de">Ahmad Hawarnah</a>
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface FacultyMapper {
    /**
     * Konvertiert ein FakultaetDTO in ein Entity-Objekt für Fakultaet.
     *
     * @param dto FakultaetDTO-Objekt ohne ID.
     * @return Eine Entity-Instanz von Fakultaet mit null als ID.
     */
    @Mapping(target = "id", ignore = true)
    Faculty toFaculty(FacultyDTO dto);

    /**
     * Konvertiert eine DekanDTO in ein Entity-Objekt für Dekan.
     *
     * @param dto DekanDTO-Objekt ohne ID.
     * @return Eine Entity-Instanz von Dekan mit null als ID.
     */
    @Mapping(target = "id", ignore = true)
    Dean toDean(DeanDTO dto);

    /**
     * Konvertiert eine KursDTO in ein Entity-Objekt für Kurs.
     *
     * @param dto KursDTO-Objekt ohne ID.
     * @return Eine Entity-Instanz von Kurs mit null als ID.
     */
    @Mapping(target = "id", ignore = true)
    Course toCourse(CourseDTO dto);
}
