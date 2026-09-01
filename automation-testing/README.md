# Automation Testing - Übungen

Lösungen zu den Übungen aus `UEBUNGEN.md`.

## Setup
Backend (Spring Boot) & Frontend (Angular) starten:

```bash
# Backend (Port 8080)
cd spring-boot-angular-basic-lw2
mvn spring-boot:run

# Frontend (Port 4200)
cd spring-boot-angular-basic-lw2/src/main/js/my-app
npm install && npm start
```

---

## Übung 1: REST API Tests (Backend)
REST-Schnittstelle `/students` mit Spring Boot Test & `MockMvc` getestet.

* **Testdatei:** `spring-boot-angular-basic-lw2/src/test/java/ch/tbz/m450/testing/tools/controller/StudentControllerTest.java`
* **Befehl:** `mvn test`
* **Ergebnis:** GET `/students`, POST `/students` und Validierungen für leere/ungültige Eingaben getestet (5/5 Tests bestanden).

---

## Übung 2: E2E Tests (Frontend)
GUI mit Cypress automatisiert im Browser getestet.

* **Testdatei:** `spring-boot-angular-basic-lw2/src/main/js/my-app/cypress/e2e/student-app.cy.ts`
* **Befehl:** `npx cypress run`
* **Getestet:** Tabelle anzeigen, Formular aufrufen, neuen Studenten hinzufügen und Validierung bei falscher E-Mail prüfen.

---

## Übung 3: Performance & Load Testing
Backend mit `k6` unter Last getestet (100 gleichzeitige User).

* **Ergebnis:** ~450 Requests/Sekunde, Ø Latenz ~20ms, 0 % Fehler.
* **Fazit:** Dank H2 In-Memory DB sehr schnell. Bei echter DB müsste HikariCP Pool angepasst werden.
