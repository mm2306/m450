# Testlevels

Lösungen zu den Fragen aus den Unterlagen.

---

## Aufgabe 1

### 1. Test-Levels im Überblick
* **Unit Testing**: Isolierter Code-Test (Klassen/Methoden). Entwickler schreiben JUnit/PyTest.
* **Component Testing**: Test von Modulen. Externe Schnittstellen (DBs, APIs) werden gemockt.
* **Integration Testing**: Test vom Zusammenspiel mehrerer Komponenten (oft mit echten DBs via Docker).
* **End-to-End Testing**: Test des gesamten Systems im Staging (funktional & nicht-funktional wie Performance/Security).

### 2. Wann wird getestet?
* **Pre-Commit**: Schnelle Unit-Tests lokal auf dem PC.
* **CI/CD Pipeline**: Automatische Unit/Component-Tests bei jedem Push.
* **Staging**: Integration- & E2E-Tests nach dem Merge auf `main`.
* **Nightly**: Aufwendige Last- & Performance-Tests über Nacht.
* **Pre-Release**: Manuelle Abnahmetests (UAT) vor der Freigabe.

### 3. Dedicated QA-Teams vs. Entwickler
* **Scrum/DevOps**: Entwickler testen selbst ("Quality is everyone's responsibility").
* **QA-Teams**: Helfen bei großen Projekten für E2E-Frameworks, Performance & Security.

### 4. Testing Lifecycle (STLC)
1. **Anforderungsanalyse**: User Stories prüfen.
2. **Testplanung**: Strategie & Tools wählen.
3. **Testentwurf**: Testfälle & Mocks erstellen.
4. **Testumgebung**: Pipeline/Staging bereitstellen.
5. **Testausführung**: Tests ausführen & Bugs erfassen.
6. **Bug-Tracking**: Bugs beheben & re-testen.
7. **Testabschluss**: Coverage prüfen & Freigabe.

---

## Aufgabe 2

### 1. Begriffe kurz erklärt
* **Testing Approach**: Die übergeordnete Strategie (z. B. Agile Testing, TDD).
* **Testing Levels**: Die Stufen im Lebenszyklus (Unit -> Component -> Integration -> System -> Acceptance).
* **Types, Techniques & Tactics**: Konkrete Werkzeuge und Methoden.
  * *Types*: Funktional vs. Nicht-Funktional (Performance, Security).
  * *Techniques*: White-Box (mit Code-Kenntnis) vs. Black-Box (nur Spezifikation).
  * *Tactics*: Manuell, Explorativ vs. Automatisiert.

### 2. Zusammenfassung
**Approach** (Strategie) -> **Levels** (Stufe) -> **Techniques & Tactics** (Werkzeuge)

> *Beispiel:* Bei **Agile Testing** (Approach) schreiben wir auf dem **Unit Level** (Level) automatisierte **White-Box Tests** (Technique/Tactic) mit JUnit 5.
