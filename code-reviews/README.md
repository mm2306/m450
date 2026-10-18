# Code Reviews – Übungen

## Projekt

Aus dem Pipeline-Übungsprojekt [recipe-planner-fronend-and-backend](./recipe-planner-fronend-and-backend):

* **`recipe-planner-fronend/`** – React (CRA, react-bootstrap, react-hook-form, axios, react-router)
  * `components/AddRecipe/AddRecipe.js` – Formular für Anlegen **und** Editieren
  * `components/AddIngredient/AddIngredient.js` – Zutatszeile (Name, Unit, Quantity)
  * `components/Recipe/Recipe.js`, `components/Browse/Browse.js`, `App.js` (Routen)
* **`recipe-planner-backend/`** – Spring Boot (Java 17, Maven, H2, MapStruct, Lombok)
  * `RecipeController` (`GET/POST /api/recipes`, `GET/PUT /api/recipes/recipe/{id}`)
  * `RecipeService`, `RecipeRepository`, `RecipeEntityMapper`

> **Hinweis Partnerarbeit:** Die Übungen sind „zu zweit" gefordert, ich habe sie jedoch allein
> absolviert. Wo ein Gegenüber nötig ist, ist die zweite, **unabhängig** implementierte Variante
> (Person B) als simulierte Partner-Lösung beschrieben – reviewt wurde tatsächlich gegen die
> Checkliste aus der [Theorie](../m450-gitlab/m450/Unterlagen/code-reviews/README.md)
> (Qualität des Codes + soziale Aspekte). Alle Review-Funde sind entweder in einem Fix-Commit
> umgesetzt oder als Follow-up (siehe Aufgabe 2) dokumentiert.

**Git-Log dieser Übung (PR-Konvention wie in der Theorie):**

| PR | Branch | Inhalt | Merge-Commit |
|---|---|---|---|
| #1 | `feat/add-recipe` | Feature „Recipe anlegen" (Frontend) | `002d4aa` – *reviewed & approved* |
| #2 | `feat/edit-recipe-backend` | `PUT /api/recipes/recipe/{id}` | `09ac3fb` |
| #3 | `feat/edit-recipe-frontend` | Edit-Formular + Route | `108348e` |

---

## Aufgabe 1 – Neues 'Recipe' hinzufügen (Frontend)

### Ablauf (6 Schritte wie in der Theorie)

1. Feature-Branch: `git checkout -b feature/add-recipe`
2. Implementierung in der IDE, Commits auf den Branch
3. `git push --set-upstream origin feature/add-recipe` → Link für den Merge Request → **PR #1** erstellt (Titel, Description mit den geänderten Dateien, Reviewer = Person B)
4. Review durch Person B – Dateien durchgehen, Kommentare an den konkreten Zeilen
5. Kommentare diskutiert, Fixes in Commit `b669dcb` *„fix: address code review findings"*
6. Approved → Merge-Commit `002d4aa` auf `main`

### Implementiertes Feature

* `AddRecipe.js` hält den State `ingredientList` (mit internem `listId`) und baut per `buildRecipe()` das JSON für `POST /api/recipes`.
* `AddIngredient.js` ist eine reine, wiederverwendbare Zeilen-Komponente (`updateIngredient` / `removeIngredient` über Callbacks).
* Feedback-States: `submitState = {submitting, error, success}` → Submit-Button disabled, Fehler-/Erfolgs-Alert.

### Review-Funde (Person B → Umsetzung A)

| # | Fundstelle (Theorie-Aspekt) | Kommentar im Review | Umsetzung |
|---|---|---|---|
| 1 | Logik-Fehler | `listId: Date.now()` – zwei „Add Ingredient"-Klicks in derselben Millisekunde erzeugen doppelte React-Keys, Zutaten gehen verloren | Fix: `useRef`-Zähler `nextListId.current++` |
| 2 | Logik-Fehler | Leeres `amount` (`''`) wird als String ans Backend geschickt → `amount` im JSON ist kein Number | Fix: `amount: ingredient.amount === '' ? 0 : ingredient.amount` in `buildRecipe()` |
| 3 | Konsistenz / Typos | Mischung DE/EN in einer Oberfläche (`'Ein Name ist erforderlich'`, `'Recipe wurde erstellt.'`) | Fix: durchgängig EN (`'Recipe name is required'`, `'Recipe created successfully.'`) |
| 4 | Code-Pflege | Dateien ohne Newline am Ende | Fix |
| 5 | Naming / Struktur | Zu viele/wenige Kommentare, Komponente mischt State und HTTP-Aufruf | Diskutiert: Beibehaltung, da Formular klein ist; Aufruf in `onSubmit` gebündelt (ein POST statt pro Zutat) |

### Zwei unabhängige Implementierungen im Vergleich

| Thema | Umsetzung A (PR #1) | Umsetzung B (simuliert) | Erkenntnis aus dem Gespräch |
|---|---|---|---|
| Zutats-Liste | Array mit eigenem `listId` | Array indiziert (`index` als Key) | **`listId` gewinnt**: Indizes werden instabil, sobald eine Zutat in der Mitte entfernt wird |
| Amount-Typ | `string` im State, erst beim Senden konvertiert | `number` direkt im State | Zahl im State ist ehrlicher; `' '`-Fall muss trotzdem abgefangen werden |
| Meldungen | erst DE, dann EN | EN | Sprache muss im Team **einmal** festgelegt werden – erst im Review sichtbar geworden |
| Tests | in Aufgabe 1 keine | Button-Test | Tests fehlten → bei Aufgabe 2 nachgezogen (`AddRecipe.test.js`) |
| PR-Grösse | ~170 Zeilen | – | Im grünen Bereich der 200–400-Zeilen-Empfehlung, Review in ~60 Minuten machbar |

### Erkenntnisse (zusammengetragen)

* Erst der Cross-Review macht „unsichtbare" Fehler sichtbar – die `Date.now()`-Kollision ist im
  Browser nur bei sehr schnellen Klicks reproduzierbar.
* Konventionen (Sprache, Namensgebung, Fehlermeldungen) müssen **vor** dem PR im Team stehen,
  sonst wird im Review über Stil gestritten statt über Inhalt.
* Kleine, thematisch getrennte PRs (Anlegen vs. Editieren) lassen sich viel besser reviewen.
* Fix-Commits gehören in den gleichen PR – der Reviewer prüft die Nachbesserung erneut, erst
  dann „Approved".

---

## Aufgabe 2 – Existierendes 'Recipe' editieren (Frontend + Backend)

### PR #2 – Backend: `PUT /api/recipes/recipe/{recipeId}`

* `RecipeController.updateRecipe()` (recipe-planner-backend/src/main/java/ch/tbz/recipe/planner/controller/RecipeController.java:44)
* `RecipeService.updateRecipe()` (recipe-planner-backend/src/main/java/ch/tbz/recipe/planner/service/RecipeService.java:40)
  * Existenz prüfen → `ResponseStatusException(NOT_FOUND)` bei unbekannter ID
  * `recipe.setId(recipeId)` – **die ID aus dem Path gewinnt**, eine abweichende ID im Body wird ignoriert
  * Map + `save()` (PUT ersetzt das Rezept vollständig – korrekte PUT-Semantik)
* `pom.xml`: Lombok auf 1.18.40 gehoben (JDK-25-Kompatibilität, im Review als Build-Blocker erkannt)
* Tests: `RecipeControllerTest` (recipe-planner-backend/src/test/java/ch/tbz/recipe/planner/controller/RecipeControllerTest.java)
  * `updateRecipe_updatesExistingRecipe` – PUT + GET-Verifikation (200, Name/Beschreibung/Zutaten aktualisiert)
  * `updateRecipe_ignoresIdInRequestBody`
  * `updateRecipe_unknownId_returnsNotFound`

**Review-Kommentare Backend**

| Fundstelle | Kommentar | Entscheidung |
|---|---|---|
| `getRecipeById` | `findById(...).orElse(null)` → MapStruct liefert `null` → **200 mit leerem Body** statt 404; inkonsistent zu `updateRecipe` | Nicht Teil des Edit-Features → **Follow-up FU-1** |
| `addRecipe` | `POST` antwortet mit `200 OK`, REST-Konvention wäre `201 Created` | **Follow-up FU-2** |
| Validierung | Kein `@Valid`/`@NotNull` auf `Recipe.name` – leeres Rezept speicherbar | **Follow-up FU-3** |
| Fehlermeldung | `"... with id %s not found"` – gut, enthält die betroffene ID | übernommen, als Vorbild für andere Endpunkte |
| SRP | Controller → Service → Repository sauber getrennt, Mapper nicht im Controller | erfüllt |

### PR #3 – Frontend: Edit-Formular

* Neue Route `App.js:19`: `/edit-recipe/:id` rendert dasselbe `AddRecipe` (Wiederverwendung, DRY)
* `AddRecipe.js`: `useParams()` → `isEditing`; `useEffect` lädt `GET /api/recipes/recipe/{id}` und
  befüllt Formular + Zutaten (`reset()`, `listId` wird für die geladenen Zutaten vergeben);
  `onSubmit` unterscheidet `PUT` vs. `POST`
* `Recipe.js`: Button „Edit Details" → `navigate('/edit-recipe/${recipe.id}')`
* `Browse.js`: `key={d.id}` auf dem `Col` ergänzt (fehlte) und `recipe`-Objekt an `Recipe` übergeben
* Aufgeräumt im Review: unbenutzte Imports in `App.js` (`Recipe`, `findAllByDisplayValue`, `Container`, `Nav`) und `Browse.js` entfernt; `typescript` in `package.json` auf `^5` gepinnt (npm löste peer-Dependency auf TS 7 auf und brach die ESLint-Chain)
* Tests: `AddRecipe.test.js` (axios gemockt)
  * `creates a new recipe on submit` → `POST`
  * `prefills the form and updates the recipe on submit` → `GET`-Prefill + `PUT` mit ID aus dem Route

**Review-Kommentare Frontend**

| Fundstelle | Kommentar | Entscheidung |
|---|---|---|
| `baseURL` | Doppelt definiert in `AddRecipe.js` und `Browse.js` (DRY) | **Follow-up FU-5** |
| Erfolgs-Flow | Nach erfolgreichem PUT werden Formular **und** Zutaten geleert – der Nutzer sieht ein leeres Formular, statt zur Übersicht zurückzukehren | **Follow-up FU-6** |
| Fehlerbehandlung | `error.message` eines Axios-Fehlers ist generisch (`Request failed with status code 404`), Response-Body wird nicht ausgewertet; kein Loading-Spinner beim Prefill | **Follow-up FU-7** |
| Zutaten-Validierung | `required` nur als HTML-Attribut, nicht via `react-hook-form` registriert | **Follow-up FU-8** |
| Key-Warnung | Fehlender `key` in `Browse.js` | im PR korrigiert |
| Naming | Route `/new-menues` (Tippfehler zu „menus"/„recipes") existiert bereits | bewusst nicht geändert → **Follow-up FU-9** (Breaking Change für Lesezeichen) |

### Erkenntnisse (zusammengetragen)

* **Zwei PRs statt einer Mischung:** Backend und Frontend wurden getrennt reviewt – jeder Review
  blieb unter 200 Zeilen und war in kurzer Zeit gründlich machbar.
* **Wiederverwendung statt Copy-Paste:** Ein Formular für Anlegen und Editieren (über `isEditing`)
  verhindert zwei künftig auseinanderlaufende Formulare – das kam erst durch die Diskussion der
  beiden Varianten zustande (Variante B hätte ein eigenes `EditRecipe.js` erstellt).
* **Ein Wahrheitsquell-Pfad:** Edit lädt das Rezept per `GET` neu, statt es über den Router-State
  mitzuschicken – damit sind Refresh und Direktaufruf der URL korrekt, und die Daten im Formular
  entsprechen genau dem, was das Backend speichert.
* **Review findet auch Altlasten:** unbenutzte Imports und der fehlende `key` stammten aus
  Bestandscode – sie wurden mitgenommen, aber als eigenständige, kleine Änderung dokumentiert.
* **„Don't be a jerk":** Funde wurden als konkrete Frage/Beobachtung formuliert („Was passiert,
  wenn zwei Zutaten im selben Millisekund entstehen?"), Vorschläge ausserhalb des Features kamen
  nie in den PR-Thread, sondern als Follow-up – der PR blieb beim eigentlichen Thema.

### Follow-ups (separate Tickets/PRs, ausserhalb des Feature-Themas)

Gemäss Theorie: *„Vorschläge, die über das Thema des Features hinausgehen, sollen separat als
follow up Ticket/PR gemacht werden."*

| Ticket | Vorschlag | Grund | Aufwand |
|---|---|---|---|
| FU-1 | `GET /api/recipes/recipe/{id}` antwortet `404` statt `200` + leerem Body | Konsistenz mit `PUT`; Frontend kann sauber auf Fehler reagieren | S |
| FU-2 | `POST /api/recipes` antwortet `201 Created` + `Location`-Header | REST-Konvention | S |
| FU-3 | Validierung `@Valid` + `@NotNull/@NotBlank` auf `Recipe.name` (ggf. `ErrorResponse`-Body) | Datenqualität in der DB | M |
| FU-4 | `DELETE /api/recipes/recipe/{id}` + Button im Recipe-Card | Feature-Lücke, aus dem Review heraus aufgefallen | M |
| FU-5 | Zentrales `src/api/recipes.js` mit `baseURL` und GET/POST/PUT-Helfern | DRY, eine Stelle für Base-URL/Credentials | S |
| FU-6 | Nach erfolgreichem Editieren navigieren statt Formular leeren (zurück zu `/`) | UX-Fund aus dem Review | S |
| FU-7 | Ladezustand (Spinner) beim Prefill + Auswertung des Axios-Response-Body in der Fehlermeldung | UX / Debuggbarkeit | M |
| FU-8 | Zutaten via `react-hook-form` registrieren (`useFieldArray`) statt eigenem State + HTML-`required` | ein Validierungskonzept statt zwei | M |
| FU-9 | Route `/new-menues` bereinigen (301/Redirect) | Bestehender Tippfehler, Änderung nur mit Redirect möglich | S |
| FU-10 | CORS-Origin (`localhost:3000`) konfigurierbar machen | sonst bricht jedes andere Deployment | S |

---

## Ausführung

### Backend

```bash
cd code-reviews/recipe-planner-fronend-and-backend/recipe-planner-backend
mvn clean test
```

**Ergebnis:** `Tests run: 3, Failures: 0, Errors: 0` (RecipeControllerTest, MockMvc + H2).
Reports: `target/surefire-reports/`

### Frontend

```bash
cd code-reviews/recipe-planner-fronend-and-backend/recipe-planner-fronend
npm install
CI=true npm test
```

**Ergebnis:** `Tests: 2 passed, 2 total` (`AddRecipe.test.js`, axios gemockt).

### Manuell (Review-Demo)

```bash
# Terminal 1
cd code-reviews/recipe-planner-fronend-and-backend/recipe-planner-backend && mvn spring-boot:run
# Terminal 2
cd code-reviews/recipe-planner-fronend-and-backend/recipe-planner-fronend && npm start
```

1. `http://localhost:3000` → Recipe-Card → **Edit Details** → Formular ist befüllt → ändern → Submit
   (Netwerk-Tab zeigt `GET`, dann `PUT /api/recipes/recipe/{id}`).
2. Ohne Backend läuft das Prefill in den Fehler-Alert (Fund für FU-7).
