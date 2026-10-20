# CI/CD Pipeline – Übungen

## Projekt

* **`recipe-planner-backend/`** – Spring Boot 3.2 Backend (Java 17, Maven, H2, MapStruct, Lombok) des Recipe-Planner.
  * Controller: `RecipeController` (`GET /api/recipes`, `GET /api/recipes/recipe/{id}`, `POST /api/recipes`)
  * Domänen-Klassen: `Recipe`, `Ingredient` (inkl. `Unit`)
  * Mapper: `RecipeEntityMapper`, `IngredientEntityMapper` (MapStruct)

---

## Aufgabe 1 – Unit Testing

Alle Controller-Methoden und beide Mapper-Klassen werden automatisiert getestet.

### 1. Controller-Methoden via MockMvc

`recipe-planner-backend/src/test/java/ch/tbz/recipe/planner/controller/RecipeControllerTest.java`

* **`getRecipes_returnsListOfRecipes`** – `GET /api/recipes` liefert eine Liste von Rezepten mit Ingredients (Status 200).
* **`getRecipe_returnsRecipeById`** – Rezept via `POST` anlegen, zurückgegebene ID nutzen und `GET /api/recipes/recipe/{id}` prüfen (Status 200, ID und Name stimmen überein).
* **`addRecipe_createsAndReturnsRecipe`** – `POST /api/recipes` erstellt ein Rezept und gibt es inkl. `id` und `ingredients` zurück (Status 200).

Verwendet wird MockMvc über `@SpringBootTest` + `@AutoConfigureMockMvc` (volle Spring-Context- und H2-In-Memory-DB). Die Testdaten werden – wie in GitLab/GitHub – vor jedem Lauf frisch aufgebaut.

### 2. Mapper-Tests mit SoftAssertions

* `recipe-planner-backend/src/test/java/ch/tbz/recipe/planner/mapper/RecipeEntityMapperTest.java` – testet `entityToDomain` und `domainToEntity` für die Domänen-Klasse `Recipe` (inkl. verschachtelter `Ingredient`-Liste).
* `recipe-planner-backend/src/test/java/ch/tbz/recipe/planner/mapper/IngredientEntityMapperTest.java` – testet die Einzel- und Listen-Mappings für die Domänen-Klasse `Ingredient`.

Beide Tests verwenden **AssertJ SoftAssertions** aus `org.assertj.core.api.SoftAssertions`.

**Vorteil von SoftAssertions:** Alle Assertions eines Tests werden ausgeführt und die **gesamten Fehler gesammelt** mit `assertAll()` gemeldet. Mit den normalen Assertions bricht der Test nach dem **ersten** Fehlschlag ab. Dadurch sieht man z.B. in einem einzigen Mapper-Test sofort, *alle* Felder, die falsch gemappt wurden – und nicht nur das erste.

### Ausführen

```bash
cd ci-cd-pipeline/recipe-planner-backend
mvn clean test                              # alle Unit-Tests
mvn test -Dtest=RecipeControllerTest        # nur ein Test
```

---

## Aufgabe 2 – Reports

### Was sind Surefire und JaCoCo? (auf einen Blick)

| | **Surefire** | **JaCoCo** |
|---|---|---|
| **Wer?** | `maven-surefire-plugin` (Maven-Standard, bereits im Spring Boot Parent) | `jacoco-maven-plugin` (muss in `pom.xml` ergänzt werden) |
| **Was?** | Test-Runner: führt die JUnit-Tests aus und protokolliert das **Ergebnis** (bestanden/fehlgeschlagen, Dauer, Exceptions) | Coverage-Analyse: misst, **welcher Codeanteil** von den Tests ausgeführt wurde |
| **Wann?** | läuft in der Maven-Phase `test` | `prepare-agent` vor dem Testlauf, `report` in der Phase `test` |
| **Output** | `target/surefire-reports/` (XML pro Klasse + Text-Log) | `target/site/jacoco/index.html` (+ XML/CSV) |
| **Frage, die er beantwortet** | „Bestehen alle Tests?" | „Wurde der Code auch wirklich getestet?" |

**So funktionieren sie zusammen:**

```text
mvn test
   │
   ├─ 1. JaCoCo prepare-agent   → Java-Agent starten, der CPU-Instruktionen
   │                              und Zeilen der laufenden Tests zählt
   │
   ├─ 2. Surefire               → führt die Tests aus (@Test-Methoden),
   │                              schreibt Ergebnis pro Testklasse nach
   │                              target/surefire-reports/TEST-*.xml
   │
   └─ 3. JaCoCo report          → wertet die Zähldaten des Agents aus und
                                  erzeugt den Coverage-Report
                                  target/site/jacoco/index.html
```

* **Surefire** ist der *Runner*: Er startet JUnit, wertet Assertions aus und meldet jeden Test als `PASS`/`FAIL` – das ist die Grundlage, ob die Pipeline grün ist.
* **JaCoCo** ist der *Beobachter*: `prepare-agent` hängt als Java-Agent an die JVM und zeichnet auf, welche Klassen/Zeilen/Instruktionen während der Tests tatsächlich liefen. Nach dem Lauf liest `report` diese Daten aus und rendert die Coverage (z.B. 95 % Instruktionen, 90 % Zeilen).
* Fällt ein Test aus, erzeugt Surefire den Fehlerreport; fehlt Code in der Coverage, zeigt JaCoCo die betroffenen Zeilen rot – beide Reports sind damit komplementäre Qualitätsaussagen.

Zwei sichtbare Berichte werden bei jedem Testlauf automatisch erzeugt:

| Report | Tool | Ablageort |
|---|---|---|
| Test-Ergebnisse (XML + Text) | Surefire (Standard) | `target/surefire-reports/` |
| Coverage (HTML + XML + CSV) | JaCoCo | `target/site/jacoco/index.html` |

Konfiguration in `recipe-planner-backend/pom.xml`:

* **Surefire** braucht keine zusätzliche Konfiguration (kommt mit Spring Boot Parent) und schreibt `target/surefire-reports/TEST-*.xml`.
* **JaCoCo** (`jacoco-maven-plugin` 0.8.15) wird hinzugefügt:
  * `prepare-agent` – Instrumentierung beim Testlauf,
  * `report` gebunden an die **Test-Phase** – so entsteht schon nach `mvn test` die Coverage `target/site/jacoco/index.html`.

Beim Lauf der 9 Unit-Tests: **95 % Instruction-Coverage, ~90 % Line-Coverage**.

### Ausführen

```bash
cd ci-cd-pipeline/recipe-planner-backend
mvn clean test            # Tests ausführen + Reports erzeugen
```

Danach die Reports öffnen:

* Surefire: `target/surefire-reports/`
* JaCoCo: `target/site/jacoco/index.html` (im Browser öffnen)

---

## Aufgabe 3 – Build-Pipeline mit GitHub Actions

Datei: `recipe-planner-backend/.github/workflows/ci.yml`

```yaml
on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]
```

Ablauf des Jobs (ein Job mit mehreren Steps = "Stage/Job"-Gedanke der CI):

1. `actions/checkout` – Code aus dem Repo holen.
2. `actions/setup-java` – JDK 17 (Temurin), Maven-Cache aktiviert.
3. `mvn -B test` – Build + Ausführung aller Unit-Tests (Surefire & JaCoCo laufen mit).
4. `actions/upload-artifact` – **Surefire-Report** (`target/surefire-reports/`) je Pipeline-Durchlauf hochladen.
5. `actions/upload-artifact` – **JaCoCo-Report** (`target/site/jacoco/`) je Pipeline-Durchlauf hochladen.

Damit gelten die Anforderungen:

* **Commit/Push triggert die Pipeline** – jeder Push bzw. Pull Request auf `main` startet den Job und damit die Unit-Tests.
* **Bericht pro Pipeline-Durchlauf** – unter *Actions → Run → Artifacts* kann der Surefire- und der JaCoCo-Report jedes einzelnen Laufs heruntergeladen und angesehen werden.
* **Optional (wie GitLab Pages):** den JaCoCo-Report mit dem GitHub-Pages-Deployment (`actions/configure-pages` + `actions/upload-pages-artifact`) dauerhaft veröffentlichen.

### GitLab ↔ GitHub

Das Prinzip ist identisch zur Theorie. Die Konzepte von `.gitlab-ci.yml` werden auf GitHub wie folgt abgebildet:

| GitLab | GitHub Actions | In dieser Lösung |
|---|---|---|
| `.gitlab-ci.yml` | `.github/workflows/*.yml` | `ci.yml` |
| `stages` (build/test/deploy) | `jobs` + `steps` | 1 Job "build" mit 5 Steps |
| `job.script` | `job.steps[].run` | `mvn -B test` |
| GitLab Runner | GitHub-hosted Runner (`ubuntu-latest`) | `runs-on: ubuntu-latest` |
| `image: maven` | `actions/setup-java` | JDK 17 Temurin |
| `artifacts.reports.junit` | `actions/upload-artifact` | Surefire-Report |
| Pages | `actions/upload-pages-artifact` | (optional) |

### Deployment-Environments

Das Deployment der Pipeline ist bewusst nicht ausgeführt, damit die Software die Environments gemäss
[deployment-environment/README.md](../m450-gitlab/m450/Unterlagen/deployment-environment/README.md)
kontrolliert durchläuft:

```text
Development ──► Testing (CI: Unit-Tests) ──► Staging ──► Production
                 ▲ dieser Schritt läuft                 (manuell gated)
```

Ein späterer Deploy-Job kann mit GitHub **Environments** (`dev` → `staging` → `production`) abgesichert werden
(`environment: staging`, `environment: production` mit Required reviewers). So bleibt die Produktion – wie in der
Staging-Beschreibung gefordert – die heikelste Umgebung und wird nicht automatisch, sondern nur über eine Freigabe
bespielt.

---

## Ausführung

### Lokal (recommendations: build & reports)

```bash
cd ci-cd-pipeline/recipe-planner-backend
mvn clean test            # Build + Unit-Tests (9/9) + Reports
```

Danach die Reports öffnen:

* Coverage: `target/site/jacoco/index.html` (im Browser öffnen)
* Tests: `target/surefire-reports/TEST-ch.tbz.recipe.planner.controller.RecipeControllerTest.xml` etc.

### Im GitHub (CI)

1. Projekt `recipe-planner-backend` in ein GitHub-Repository pushen.
2. Jeder Push/PR auf `main` startet den Workflow unter **Actions**.
3. Pro Lauf: **Artifacts** (`surefire-reports`, `jacoco-coverage-report`) herunterladen und öffnen.