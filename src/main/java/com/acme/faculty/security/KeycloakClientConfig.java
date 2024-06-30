/*
 * Copyright (C) 2024 - present Juergen Zimmermann, Hochschule Karlsruhe
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.faculty.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Konfiguration für einen Spring-HTTP-Client für Keycloak.
 */
public interface KeycloakClientConfig {
    /**
     * Logger-Objekt.
     */
    Logger LOGGER = LoggerFactory.getLogger(KeycloakClientConfig.class);

    /**
     * Bean-Methode, um ein Objekt zum Interface KeycloakRepository zu erstellen.
     *
     * @param clientBuilder Injiziertes Objekt vom Typ RestClient.Builder
     * @param keycloak Spring-Properties für Keycloak
     * @return Objekt zum Interface KeycloakRepository
     */
    @Bean
    default com.acme.faculty.security.KeycloakRepository keycloakRepository(final RestClient.Builder clientBuilder, com.acme.faculty.security.KeycloakProps keycloak) {
        final var baseUri = UriComponentsBuilder.newInstance()
            .scheme(keycloak.schema())
            .host(keycloak.host())
            .port(keycloak.port())
            .build();
        LOGGER.debug("keycloakRepository: baseUri={}", baseUri);

        final var restClient = clientBuilder.baseUrl(baseUri.toUriString()).build();
        final var clientAdapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        return proxyFactory.createClient(com.acme.faculty.security.KeycloakRepository.class);
    }
}
