# Code Reviews – Übungen

## Projekt

Aus [recipe-planner-fronend-and-backend](./recipe-planner-fronend-and-backend):

- **Frontend** (`recipe-planner-fronend/`, React, CRA, react-bootstrap, react-hook-form, axios, react-router): `AddRecipe`, `AddIngredient`, `Recipe`, `Browse`, `App`
- **Backend** (`recipe-planner-backend/`, Spring Boot 17, Maven, H2, MapStruct, Lombok): `RecipeController`, `RecipeService`, `RecipeRepository`, `RecipeEntityMapper`

> Übung allein umgesetzt. Reviews nach Theorie-Checkliste gegen simulierte Partner-Variante. Funde entweder gefixt oder als Follow-up dokumentiert.

**PRs:**

| PR | Branch | Inhalt | Merge-Commit |
|---|---|---|---|
| #1 | `feat/add-recipe` | Rezept anlegen (Frontend) | `002d4aa` – *reviewed & approved* |
| #2 | `feat/edit-recipe-backend` | `PUT /api/recipes/recipe/{id}` (Backend) | `09ac3fb` |
| #3 | `feat/edit-recipe-frontend` | Edit-Formular + Route (Frontend) | `108348e` |

---
## Aufgabe 1 – Rezept hinzufügen (Frontend)

**Prozess:** Feature-Branch → Implementierung → PR → Review → Fixes → Approval/Merge

**Wichtige Fixes aus dem Review:**

| Fund | Fix |
|---|---|
| `Date.now()` für `listId` → Keys kollidieren bei schnellen Klicks | `useRef`-Zähler |
| Leerer String bei `amount` → falscher Typ ans Backend | Konvertierung zu `0` in `buildRecipe()` |
| DE/EN-Mix in Meldungen | Einheitlich Englisch |
| Kleinstyle (fehlende Newlines) | Behoben |

**Kernaussagen:** Kleine, fokrierte PRs sind besser reviewbar. Konventionen sollten vor dem PR festgelegt sein. Auch schwer reproduzierbare Bugs fallen erst im Cross-Review auf.

---

## Aufgabe 2 – Rezept editieren (Frontend + Backend)

### Backend (#2)
- `PUT /api/recipes/recipe/{id}` prüft Existenz (`404` falls unbekannt)
- ID aus Path gewinnt, ID im Body wird ignoriert
- PUT ersetzt das Rezept vollständig
- Tests für Update, ignorierte Body-ID und unbekannte ID ergänzt

### Frontend (#3)
- `AddRecipe` wird für Anlegen & Editieren wiederverwendet (`isEditing` via `useParams`)
- Prefill via `GET /api/recipes/recipe/{id}` direkt aus Backend (robust für Refresh/Direktaufruf)
- `PUT` statt `POST` im Edit-Modus
- Neuer Button „Edit Details" in `Recipe`, Route `/edit-recipe/:id`
- Altlasten (unbenutzte Imports, fehlender `key`) bereinigt
- Tests für Create & Edit ergänzt

**Kernaussagen:** Backend/Frontend getrennt reviewen, Wiederverwendung statt Duplikat, frisches Laden statt Router-State. Auch Bestandscode lohnt sich im Review sauber aufzuräumen.

## Follow-ups (ausserhalb der Features)

| ID | Thema | ID | Thema |
|---|---|---|---|
| FU-1 | `GET` unbekannter ID → `404` statt leerer Body | FU-6 | Nach Edit zur Übersicht navigieren statt Formular leeren |
| FU-2 | `POST` → `201 Created` + `Location` | FU-7 | Loading-Spinner + bessere Axios-Fehlermeldung |
| FU-3 | Validierung `@Valid`/`@NotBlank` für Rezept | FU-8 | Zutaten via `react-hook-form` `useFieldArray` validieren |
| FU-4 | `DELETE` + Button | FU-9 | Route `/new-menues` bereinigen (mit Redirect) |
| FU-5 | Zentrales `src/api/recipes.js` | FU-10 | CORS-Origin konfigurierbar |

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
