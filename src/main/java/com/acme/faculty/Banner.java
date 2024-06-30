/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.faculty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * Banner als String-Konstante für den Start des Servers.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@SuppressWarnings({
    "AccessOfSystemProperties",
    "CallToSystemGetenv",
    "UtilityClassCanBeEnum",
    "UtilityClass",
    "ClassUnconnectedToPackage"
})
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
final class Banner {

    // http://patorjk.com/software/taag/#p=display&f=Ivrit&t=faculty%202024.04.1
    private static final String FIGLET = """
         _                    _        ____   ___ ____  _  _    ___  _  _    _
        | | ___   _ _ __   __| | ___  |___ \\ / _ \\___ \\| || |  / _ \\| || |  / |
        | |/ / | | | '_ \\ / _` |/ _ \\   __) | | | |__) | || |_| | | | || |_ | |
        |   <| |_| | | | | (_| |  __/  / __/| |_| / __/|__   _| |_| |__   _|| |
        |_|\\_\\\\__,_|_| |_|\\__,_|\\___| |_____|\\___/_____|  |_|(_)___/   |_|(_)_|
        """;
    private static final String SERVICE_HOST = System.getenv("faculty_SERVICE_HOST");
    private static final String KUBERNETES = SERVICE_HOST == null
        ? "N/A"
        : STR."faculty_SERVICE_HOST=\{SERVICE_HOST}, faculty_SERVICE_PORT=\{System.getenv("faculty_SERVICE_PORT")}";

    /**
     * Banner für den Server-Start.
     */
    static final String TEXT = """

        $figlet
        (C) Juergen Zimmermann, Hochschule Karlsruhe
        Version             2024.04.1
        Spring Boot         $springBoot
        Spring Security     $springSecurity
        Spring Framework    $spring
        Hibernate           $hibernate
        Java                $java
        Betriebssystem      $os
        Rechnername         $rechnername
        IP-Adresse          $ip
        Heap: Size          $heapSize
        Heap: Free          $heapFree
        Kubernetes          $kubernetes
        Username            $username
        JVM Locale          $locale
        GraphiQL            /graphiql
        OpenAPI             /swagger-ui.html /v3/api-docs.yaml
        H2 Console          /h2-console (JDBC URL: "jdbc:h2:mem:testdb" mit User "sa" und Passwort "")
        """
        .replace("$figlet", FIGLET)
        .replace("$springBoot", SpringBootVersion.getVersion())
        .replace("$springSecurity", SpringSecurityCoreVersion.getVersion())
        .replace("$spring", Objects.requireNonNull(SpringVersion.getVersion()))
        .replace("$hibernate", org.hibernate.Version.getVersionString())
        .replace("$java", Runtime.version() + " - " + System.getProperty("java.vendor"))
        .replace("$os", System.getProperty("os.name"))
        .replace("$rechnername", getLocalhost().getHostName())
        .replace("$ip", getLocalhost().getHostAddress())
        .replace("$heapSize", Runtime.getRuntime().totalMemory() / (1024L * 1024L) + " MiB")
        .replace("$heapFree", Runtime.getRuntime().freeMemory() / (1024L * 1024L) + " MiB")
        .replace("$kubernetes", KUBERNETES)
        .replace("$username", System.getProperty("user.name"))
        .replace("$locale", Locale.getDefault().toString());

    @SuppressWarnings("ImplicitCallToSuper")
    private Banner() {
    }

    private static InetAddress getLocalhost() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
