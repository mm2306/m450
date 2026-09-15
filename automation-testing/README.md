# Automation Testing - Übungen

Lösungen zu den Übungen aus `UEBUNGEN.md`.

## Voraussetzungen

* **Java 17+** und **Maven** für Backend-Tests
* **Node.js** und **npm** für Cypress E2E-Tests (`npm install` lädt den Cypress-Binary automatisch)
* **k6** oder **Docker** für Load-Tests (`make test-load` nutzt Docker als Fallback)

k6 installieren (Fedora):

```bash
sudo dnf install https://dl.k6.io/rpm/repo.rpm
sudo dnf install k6
```

## Setup
Backend (Spring Boot) & Frontend (Angular) starten:

```bash
# Backend (Port 8081)
cd spring-boot-angular-basic-lw2
mvn spring-boot:run

# Frontend (Port 4200)
cd spring-boot-angular-basic-lw2/src/main/js/my-app
npm install && npm start
```

---

## Tests ausführen

Alle Befehle aus dem Ordner `automation-testing` ausführen.

**Übung 2 und 3** benötigen laufende Server. Backend zuerst in einem separaten Terminal starten:

```bash
make start-backend
# wartet bis "Started StudentApplication" erscheint
```

Für E2E zusätzlich das Frontend in einem weiteren Terminal:

```bash
cd spring-boot-angular-basic-lw2/src/main/js/my-app && npm start
```

Dann die Tests ausführen:

```bash
make test-api    # Übung 1: REST API Tests (MockMvc)
make test-e2e    # Übung 2: Cypress E2E (Backend + Frontend müssen laufen)
make test-load   # Übung 3: k6 Load Test (Backend muss laufen)
```

Alternativ die einzelnen Befehle direkt:

```bash
# Übung 1
cd spring-boot-angular-basic-lw2 && mvn test

# Übung 2 (Backend + Frontend müssen laufen)
cd spring-boot-angular-basic-lw2/src/main/js/my-app && npm install && npm run e2e

# Übung 3 (Backend muss laufen – zuerst: make start-backend)
k6 run k6/load-test.js
# oder mit Docker/Podman-Fallback:
make test-load
```

---

## Übung 1: REST API Tests (Backend)
REST-Schnittstelle `/students` mit Spring Boot Test & `MockMvc` getestet.

* **Testdatei:** `spring-boot-angular-basic-lw2/src/test/java/ch/tbz/m450/testing/tools/controller/StudentControllerTest.java`
* **Befehl:** `make test-api` oder `cd spring-boot-angular-basic-lw2 && mvn test`
* **Ergebnis:** GET `/students`, POST `/students` und Validierungen für leere/ungültige Eingaben getestet (5/5 Tests bestanden).

---

## Übung 2: E2E Tests (Frontend)
GUI mit Cypress automatisiert im Browser getestet.

* **Testdatei:** `spring-boot-angular-basic-lw2/src/main/js/my-app/cypress/e2e/student-app.cy.ts`
* **Befehl:** `make test-e2e` oder `cd spring-boot-angular-basic-lw2/src/main/js/my-app && npm run e2e`
* **Getestet:** Tabelle anzeigen, Formular aufrufen, neuen Studenten hinzufügen und Validierung bei falscher E-Mail prüfen.

---

## Übung 3: Performance & Load Testing
Backend mit `k6` unter Last getestet (100 gleichzeitige User).

* **Testdatei:** `k6/load-test.js`
* **Befehl:** `make test-load` oder `k6 run k6/load-test.js`
* **Ergebnis:** ~450 Requests/Sekunde, Ø Latenz ~20ms, 0 % Fehler.
* **Fazit:** Dank H2 In-Memory DB sehr schnell. Bei echter DB müsste HikariCP Pool angepasst werden.
