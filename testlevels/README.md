# Testlevels

### Aufgabe 1

##### **1. Mit welchen Test Levels hatten Sie bereits zu tun?**

* **Unit Testing (Stufe 1)**:
  * *Charakteristik*: White-Box-Test auf der untersten Ebene. Testet einzelne isolierte Einheiten (Klassen, Methoden) in Java/Python/TypeScript.
  * *Verantwortung*: Entwickler schreiben und pflegen die Tests. Sie steuern das Softwaredesign und bilden die Basis für sicheres Code-Refactoring.
  * *Werkzeuge*: JUnit 5, PyTest, Jest.
* **Component Testing (Komponententest)**:
  * *Charakteristik*: White-Box-Test von zusammenhängenden Modulen. Externe Schnittstellen (Datenbanken, Web-APIs, Message Queues) werden konsequent isoliert und **gemockt** (z. B. mit Mockito).
* **Integration Testing (Integrationstest)**:
  * *Charakteristik*: Sowohl Black-Box- als auch White-Box-Test. Überprüft das Zusammenspiel mehrerer Schnittstellen mit *echten* Anbindungen (z. B. echte Test-Datenbanken via Docker/Testcontainers anstelle von Mocks).
  * *Verantwortung*: Wird heutzutage sowohl von Backend-Entwicklern (White-Box) als auch von QA-Teams (Black-Box) durchgeführt.
* **End-to-End Testing**:
  * *Charakteristik*: Black-Box-Test des Gesamtsystems in einer live-nahen Staging-Umgebung gegen funktionale und nicht-funktionale Anforderungen.
  * *Prüfbereiche*:
    * *Funktional*: Vollständige Geschäftsprozesse.
    * *Nicht-Funktional*: Performance (Load & Stress Testing), Usability sowie Security (Authentifizierung/Penetrationstests).

---

##### **2. Wann werden Tests ausgeführt? (Testausführung & Ausführungszeitpunkte)**

* **Lokal / Pre-Commit**: Entwickler führen schnelle Unit- und Komponententests lokal auf ihrer Entwicklermaschine aus.
* **CI/CD Pipeline (Continuous Integration)**: Bei jedem Push / Merge Request werden automatisch alle Unit- und Komponententests im Build-Prozess ausgeführt (inkl. statischer Code-Analyse mit SonarQube).
* **Continuous Deployment (Staging)**: Nach erfolgreichem Merge in den (`main`) werden automatisierte Integrationstests und E2E-Systemtests auf der Staging-Umgebung getriggert.
* **Nightly Builds / Scheduled**: Zeitintensive Performance-Tests (Load & Stress) sowie umfangreiche Regressions-Testsuiten laufen zeitgesteuert nachts ab, um Entwicklungs-Pipelines tagsüber nicht zu blockieren.
* **Pre-Release (UAT)**: Manuelle Akzeptanztests und exploratives Testen finden kurz vor der Produktionsfreigabe am Ende eines Sprints statt.

---

##### **3. Dedizierte Testing oder QA Teams?**

* **Agile Softwareentwicklung (DevOps / Scrum)**:
  * Entwickler sind direkt für Unit-, Komponenten- und Integrationstests verantwortlich (*"Quality is everyone's responsibility"*).
* **Spezialisierte QA-Teams / QA-Engineers**:
  * In größeren Projekten unterstützen dedizierte QA-Engineers das Team beim Aufbau von E2E-Automationsframeworks (z. B. Cypress, Selenium) sowie bei komplexen System-, Performance- und Security-Tests.

---

##### **4. Testing Lifecycle (STLC - Software Testing Life Cycle)**

1. **Anforderungsanalyse**: Analyse von User Stories und Akzeptanzkriterien auf Testbarkeit.
2. **Testplanung**: Festlegung von Teststrategie, Testebenen, Testumgebungen und Werkzeugen.
3. **Testentwurf**: Erstellung von Testfällen, Testdaten und Mocks.
4. **Testumgebungsaufbau**: Bereitstellung von Testumgebungen (Staging) und Pipelines.
5. **Testausführung**: Automatisierte Ausführung in CI/CD & manuelle Durchführung; Protokollierung von Abweichungen (Bugs).
6. **Bug-Tracking & Retesting**: Erfassung von Fehlern im Issue-Tracker (Jira), Behebung durch Entwickler und Re-Test.
7. **Testabschluss & Freigabe**: Auswertung der Testabdeckung (Test Coverage) und finale Freigabe.

---

### Aufgabe 2

#### **1. Begriffsdefinitionen & Einordnung**

* **Testing Approach (Testansatz / Teststrategie)**
  * **Definition**: Das übergeordnete "Wie" und die grundlegende Testphilosophie / Strategie des Projekts.
  * **Beispiele**: Agile Testing, Test-Driven Development (TDD), Behavior-Driven Development (BDD), Risk-Based Testing, Shift-Left Testing.
  * **Ebene**: Strategische / methodische Gesamtausrichtung.

* **Testing Levels (Testebenen / Teststufen)**
  * **Definition**: Die chronologischen Stufen im Entwicklungszyklus, auf denen getestet wird (Granularität & Testobjekt).
  * **Stufen (nach Testpyramide)**:
    $\text{Unit Testing} \rightarrow \text{Component Testing} \rightarrow \text{Integration Testing} \rightarrow \text{System Testing} \rightarrow \text{Acceptance Testing}$
  * **Ebene**: Zeitliche und strukturelle Einordnung im Software-Lebenszyklus.

* **Testing Types, Techniques and Tactics (Testarten, -techniken & -taktiken)**
  * **Definition**: Die konkreten Werkzeuge, Verfahren und Taktiken zur Prüfung spezifischer Eigenschaften auf den jeweiligen Testebenen.
  * **Testarten (Types)**:
    * *Funktional*: Was das System tut (Anforderungskonformität).
    * *Nicht-funktional*: Wie das System arbeitet (Performance, Load, Stress, Usability, Security).
  * **Testtechniken (Techniques)**:
    * *White-Box Testing*: Testen mit Kenntnis des Quellcodes (Code-Abdeckung, Pfadtests).
    * *Black-Box Testing*: Testen ohne Codekenntnis nach Spezifikation (Äquivalenzklassenbildung, Grenzwertanalyse).
  * **Testtaktiken (Tactics)**: Automated Testing vs. Manual Testing, Exploratives Testen, Regressions-Testing.
  * **Ebene**: Operative Instrumente und Werkzeuge.

---

#### **2. Hierarchie und Abhängigkeiten**

**Testing Approach** (Strategie / Gedanke) -> **Testing Levels** (Stufenmodell) -> **Types, Techniques & Tactics** (Konkrete Metoden)

* **Zusammenhang**: Der *Testing Approach* legt die Strategie fest. Auf jedem *Testing Level* werden spezifische *Testing Types, Techniques & Tactics* eingesetzt.
* **Beispiel**:
  > *"Unter einem **Agile Testing Approach** (Approach) schreiben Entwickler auf dem **Unit Testing Level** (Level) automatisierte **White-Box Tests** (Technique) mit **JUnit 5** (Tactic) zur Prüfung von **funktionalen Anforderungen** (Type)."*
