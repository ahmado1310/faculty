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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.faculty.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import static com.acme.faculty.security.AuthController.AUTH_PATH;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller für Abfragen bei Security.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping(AUTH_PATH)
@Tag(name = "Authentifizierung API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class AuthController {
    /**
     * Pfad für Authentifizierung.
     */
    public static final String AUTH_PATH = "/auth";

    private final com.acme.faculty.security.KeycloakRepository keycloakRepository;
    private final CompromisedPasswordChecker passwordChecker;

    private final com.acme.faculty.security.KeycloakProps keycloakProps;

    private String clientAndSecretEncoded;

    @PostConstruct
    private void encodeClientAndSecret() {
        final var clientAndSecret = keycloakProps.clientId() + ':' + keycloakProps.clientSecret();
        clientAndSecretEncoded = Base64
            .getEncoder()
            .encodeToString(clientAndSecret.getBytes(Charset.defaultCharset()));
        log.debug("clientAndSecretEncoded={}", clientAndSecretEncoded);
    }

    @GetMapping("/me")
    @Operation(summary = "JWT bei OAuth 2.0 abfragen", tags = "Auth")
    @ApiResponse(responseCode = "200", description = "Eingeloggt")
    @ApiResponse(responseCode = "401", description = "Fehler bei Username oder Passwort")
    Map<String, Object> me(@AuthenticationPrincipal final Jwt jwt) {
        // https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
        log.debug("me: isCompromised() bei Passwort 'pass1234': {}", passwordChecker.check("pass1234").isCompromised());

        return Map.of(
            "subject", jwt.getSubject(),
            "claims", jwt.getClaims()
        );
    }

    @PostMapping(path = "/login", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Login mit Benutzername und Passwort", tags = "Auth")
    @ApiResponse(responseCode = "200", description = "Eingeloggt")
    @ApiResponse(responseCode = "401", description = "Fehler bei Username oder Passwort")
    com.acme.faculty.security.TokenDTO login(@RequestBody final com.acme.faculty.security.LoginDTO loginDto) {
        log.debug("login: loginDto={}", loginDto);
        final var tokenDTO = keycloakRepository.login(
            "grant_type=password&username=" + loginDto.username() + "&password=" + loginDto.password(),
            "Basic " + clientAndSecretEncoded,
            APPLICATION_FORM_URLENCODED_VALUE
        );
        log.debug("login: tokenDTO={}", tokenDTO);
        return tokenDTO;
    }

    @ExceptionHandler
    @ResponseStatus(UNAUTHORIZED)
    void onUnauthorized(@SuppressWarnings("unused") final HttpClientErrorException.Unauthorized ex) {
    }
}
