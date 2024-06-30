# Hinweise zum Monitoring mit Prometheus und Grafana

[Juergen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)

Inhalt

- [Anpassungen für das eigene Projekt](#anpassungen-für-das-eigene-projekt)
- [Vorbereitung](#vorbereitung)
- [Actuator von Spring Boot für den Produktivbetrieb](#actuator-von-spring-boot-für-den-produktivbetrieb)
- [Prometheus als Toolkit für Monitoring](#prometheus-als-toolkit-für-monitoring)
- [Grafana: Dashboards zur Visualisierung](#grafana-dashboards-zur-visualisierung)
  - [Initiale Konfiguration des Grafana-Dashboards](#initiale-konfiguration-des-grafana-dashboards)
  - [Aufruf eines existierenden Dashboards](#aufruf-eines-existierenden-dashboards)
  - [SQLite als DB-System](#sqlite-als-db-system)
- [Links](#links)

---

## Anpassungen für das eigene Projekt

In der Konfigurationsdatei `prometheus.yml` im Verzeichnis `extras\compose\prometheus-grafana`
muss man die Property `scrape_configs.static_configs.labels.application` auf den
Namen des eigenen Servers setzen.

## Vorbereitung

Im Verzeichnis `extras\compose\prometheus-grafana` muss man sicherstellen, dass
die Datei `grafana.db` existiert und leer ist.

Mit _Docker Compose_ werden die diversen Backend-Server gestartet:
- DB-Server
- Zipkin für Tracing
- Prometheus für Monitoring und Grafana für Visualisierung

Danach startet man den Microservice "kunde":
- `.\gradlew bootRun` oder
- `.\mvnw spring-boot:run`

Nun produziert man Requests, z.B. mit dem Skript `generate-load.ps1` im
Verzeichnis `extras`.

## Metriken

_Metriken_ sind _Messwerte_ entlang der _Zeitachse_, z.B. Anzahl Requests,
Antwortzeiten, Heap und RAM. Damit kann man im RZ beobachten, ob ein Server
ausgelastet oder sogar überlastet ist.

## Actuator von Spring Boot für den Produktivbetrieb

Durch _Actuator_ von Spring Boot werden Features für den Produktivbetrieb
bereitgestellt. Der Endpunkt `https://localhost:8080/actuator/prometheus`
liefert Metriken für Prometheus zum Monitoring.

## Prometheus als Toolkit für Monitoring

Mit der URL `http://localhost:9090` gemäß der Konfiguration in `compose.yml` im
Verzeichnis `extras\compose\prometheus+grafana` kann man nun die Metriken anzeigen,
die der Prometheus-Server über HTTP von einer Anwendung abruft ("Polling").
Port `8080`, Pfad `/actuator/prometheus` und TLS sind in der Konfigurationsdatei
`prometheus.yml` im Verzeichnis `extras\compose\prometheus-grafana` spezifiziert.

Zunächst ruft man also einen Webbrowser mit der URL `http://localhost:9090` auf.
Dann gibt man im Suchfeld den Ausdruck `http_server_requests_active_seconds_bucket`
ein und klickt den Button `Execute` an. Dann sieht man die _Zeitreihen-Daten_
(= time-series data) im Format von Prometheus.

Welche URLs von Prometheus abgefragt werden, kann man sich mit `http://localhost:9090/targets`
anzeigen lassen.

## Grafana: Dashboards zur Visualisierung

Mit der URL `http://localhost:3000` gemäß der Konfiguration in `compose.yml` im
Verzeichnis `extras\compose\prometheus+grafana` greift man auf den Grafana-Server
zu und muss zunächst ein vorhandenes Dashboard auswählen oder ein eigenes Dashboard
implementieren.

Auf der Webseite `https://rigorousthemes.com/blog/best-grafana-dashboard-examples`
kann man einen Eindruck erhalten, welche Visualisierungen mit Grafana möglich sind.

**BEACHTE**: Grafana nutzt als Default-Port _3000_ und damit denselben Port wie
JavaScript-basierte Serveranwendungen mit _Node_, _Express_, _Nest_, _Next_, _Remix_,
_Fastify_ oder _Koa_.

### Initiale Konfiguration des Grafana-Dashboards

Wenn man in einem Webbrowser `http://localhost:3000` aufruft, muss man sich nicht
einloggen, weil in `compose.yml` die Umgebungsvariable `GF_AUTH_ANONYMOUS_ENABLED`
auf `true` gesetzt ist und `GF_AUTH_ANONYMOUS_ORG_ROLE` auf `Admin`.

Wenn man Grafana zum ersten Mal startet ([s.o.](#vorbereitung)) und ein Dashboard
konfigurieren möchte, muss man eine leere Datei `grafana.db` im Verzeichnis
`extras\compose\prometheus-grafana` bereitstellen. In dieser Datei werden die
Daten von Grafana als SQLite-DB gespeichert ([s.u.](#sqlite-als-db-system)).
Ggf. fährt man Grafana und Prometheus wieder herunter, um `grafana.db` zu löschen
und als leere Datei anzulegen.

Zuerst klickt man auf die Kachel _Create your first dashboard_ und anschließend
auf den Button _Import dashboard_. Im Eingabefeld für _Grafana.com dashboard URL or ID_
gibt man nun die ID `4701` (siehe https://grafana.com/grafana/dashboards/4701-jvm-micrometer
mit 10+ Mio. Downloads) ein und klickt auf den Button _Load_.
Im Dropdown-Menü _Prometheus_ wählt man jetzt die Option _prometheus default_
aus, was in der Datei `datasource.yml` im Verzeichnis
extras\compose\prometheus-grafana\grafana-datasources` konfiguriert ist.
Abschließend klickt man auf den Button _Import_.

Als Monitoring-Zeitraum sind 24 Stunden ("Last 24 hours") rechts oben voreingestellt.
Das sollte man im Dropdown-Menü auf z.B. _Last 5 minutes_ ändern.

### Aufruf eines existierenden Dashboards

Wenn man einmal das obige Dashboard konfiguriert hat, kann man später die beiden
Server für Prometheus und Grafana starten und für Grafana die URL `http://localhost:3000`
aufrufen. Im Overflow-Menü in der linken oberen Ecke wählt man den Menüpunkt
_Dashboards_ aus, setzt einen Haken bei _JVM (Micrometer)_ und klickt auf diesen
Menüpunkt. Auch hier ist es empfehlenswert rechts oben: _Last 5 minutes_ im
Dropdown-Menü auszuwählen.

### SQLite als DB-System

Grafana speichert die anfallenden Daten in mit dem "Embedded" DB-System _SQLite_
in der Datei `grafana.db`. Dort gibt es z.B. die Tabelle `dashboard` oder `user`.
Durch einen Doppelklick auf `grafana.db` kann man den DB-Inhalt mit IntelliJ IDEA inspizieren.

## Links

- https://stackabuse.com/monitoring-spring-boot-apps-with-micrometer-prometheus-and-grafana
- https://medium.com/swlh/monitoring-spring-boot-application-with-micrometer-prometheus-and-grafana-using-custom-metrics-9d33de107ad8
- https://github.com/micrometer-metrics/micrometer-samples/tree/main/micrometer-samples-boot3-web
- https://docs.micrometer.io/micrometer/reference/concepts.html
