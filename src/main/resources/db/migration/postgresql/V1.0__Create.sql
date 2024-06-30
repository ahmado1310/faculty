-- Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <https://www.gnu.org/licenses/>.

-- docker compose exec postgres bash
-- psql --dbname=kunde --username=kunde [--file=/sql/V1.0__Create.sql]

-- Indexe mit pgAdmin auflisten: "Query Tool" verwenden mit
--  SELECT   tablename, indexname, indexdef, tablespace
--  FROM     pg_indexes
--  WHERE    schemaname = 'kunde'
--  ORDER BY tablename, indexname;

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- BEACHTE: user ist ein Schluesselwort
CREATE TABLE IF NOT EXISTS login (
             -- https://www.postgresql.org/docs/current/datatype-uuid.html
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-PRIMARY-KEYS
             -- impliziter Index fuer Primary Key
    id       uuid PRIMARY KEY USING INDEX TABLESPACE facultyspace,
             -- generierte Sequenz "login_id_seq":
    -- id    integer GENERATED ALWAYS AS IDENTITY(START WITH 1000) PRIMARY KEY USING INDEX TABLESPACE kundespace,
    username varchar(20) NOT NULL UNIQUE USING INDEX TABLESPACE facultyspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE facultyspace;

-- https://www.postgresql.org/docs/current/sql-createtype.html
-- https://www.postgresql.org/docs/current/datatype-enum.html
-- CREATE TYPE geschlecht AS ENUM ('MAENNLICH', 'WEIBLICH', 'DIVERS');

-- Erstelle Tabelle für Deans
CREATE TABLE IF NOT EXISTS dean (
    id      uuid PRIMARY KEY USING INDEX TABLESPACE facultyspace,
    name    varchar(100) NOT NULL,
    email   varchar(100) NOT NULL UNIQUE USING INDEX TABLESPACE facultyspace
) TABLESPACE facultyspace;

-- Erstelle Tabelle für Courses
CREATE TABLE IF NOT EXISTS course (
    id      uuid PRIMARY KEY USING INDEX TABLESPACE facultyspace,
    name    varchar(100) NOT NULL
) TABLESPACE facultyspace;

-- Erstelle Tabelle für Faculties
CREATE TABLE IF NOT EXISTS faculty (
    id          uuid PRIMARY KEY USING INDEX TABLESPACE facultyspace,
    name        varchar(100) NOT NULL,
    dean_id     uuid NOT NULL REFERENCES dean(id),
    UNIQUE(name, dean_id) USING INDEX TABLESPACE facultyspace
) TABLESPACE facultyspace;

-- Additional Indexes
CREATE INDEX IF NOT EXISTS dean_email_idx ON dean(email) TABLESPACE facultyspace;
CREATE INDEX IF NOT EXISTS faculty_name_idx ON faculty(name) TABLESPACE facultyspace;
CREATE INDEX IF NOT EXISTS course_name_idx ON course(name) TABLESPACE facultyspace;
CREATE INDEX IF NOT EXISTS course_faculty_id_idx ON course(faculty_id) TABLESPACE facultyspace;
