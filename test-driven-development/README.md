# Test Driven Development - Übungen


## Voraussetzungen

* PHP (compatible Version, `mbstring`-Erweiterung benötigt)
* PHPUnit (5.x für die vorgegebenen Legacy-Tests, oder Anpassung des `use`-Statements für neuere Versionen)

---

## Aufgabe 1: Setup Environment

PHPUnit wird in der IDE bzw. auf dem Entwicklungsserver installiert:

* **PHPStorm**: Native PHPUnit-Support (PHP & Composer einrichten, PHPUnit als Dependency hinzufügen).
* **Alternativ ohne IDE**: PHPUnit als phar herunterladen:
  ```bash
  wget https://phar.phpunit.de/phpunit-5.7.phar -O phpunit.phar
  ```

---

## Aufgabe 2: Methoden implementieren

FancyApp vom ZIP `FancyApp.zip` wurde entpackt und die Methoden der Klasse `MyFancyClass` so implementiert, dass alle Unit-Tests grün werden.

### Struktur

```
FancyApp/
├── Application/
│   └── MyFancyClass.php   <- Klasse mit implementierten Methoden
└── Testcases/
    └── Test.php           <- Unittests (unverändert)
```

### Ausführung

```bash
docker run --rm -v $(pwd):/app:z -w /app/FancyApp/Testcases php:7.1-cli php /app/phpunit.phar Test.php
```

**Ergebnis:** `OK (3 tests, 27 assertions)` – alle Testcases bestehen.

### Testprotokoll der einzelnen Methoden

#### `shortString($text, $length, $ending = '...')`

| Assertion in `Test.php` | Verhalten |
|---|---|
| `shortString('', 100)` | Leerer Text → `''` |
| `shortString($text, 100)` wo `mb_strlen($text) <= 100` | Text kürzer/gleich Limit → Originaltext |
| `shortString($text130Char, 100)` | Text länger als Limit → gekürzt auf `$length - strlen($ending)` Zeichen + `$ending` |
| `shortString($text, 20, $endingLong)` | Ending länger als `$length` → `false` |
| `shortString($text35Char, 35, $endingLong)` | Grenzfall (Umlaut `ä` = 2 Byte) → mit `mb_strlen`/`mb_substr` korrekt als 35 Zeichen erkannt |

Implementation:
```php
public function shortString($text, $length, $ending = '...') {
    if (mb_strlen($ending) > $length) {
        return false;
    }
    if (mb_strlen($text) <= $length) {
        return $text;
    }
    return mb_substr($text, 0, $length - mb_strlen($ending)) . $ending;
}
```

> **Wichtig:** Es werden die Multibyte-Funktionen `mb_strlen()`/`mb_substr()` verwendet, damit Sonderzeichen wie `ä` korrekt als ein Zeichen (und nicht als 2 Bytes) gezählt werden. Andernfalls schlägt der Grenzfall `shortString('Ich bin Text mit 35 Char Länge (35)', 35, $endingLong)` fehl.

#### `calcAverage($values)`

| Assertion in `Test.php` | Verhalten |
|---|---|
| `calcAverage([])` | Leeres Array → `0` |
| `calcAverage([4.5, 5.0, 5.5])` | Mittelwert → `5` |
| `calcAverage(['4.5','5.0','5.5'])` | Numerische Strings → gleiches Resultat wie Float-Array |
| `calcAverage(['3.44','bestanden',...])` | Enthält Nicht-Zahl → `false` |

Implementation:
```php
public function calcAverage($values) {
    if (empty($values)) {
        return 0;
    }
    foreach ($values as $value) {
        if (!is_numeric($value)) {
            return false;
        }
    }
    return array_sum($values) / count($values);
}
```

#### `getOpposite($value, $delimiter = ',')`

| Assertion in `Test.php` | Verhalten |
|---|---|
| `getOpposite([4.5, 5.0, 5.5])` | Array → implode mit Delimiter (`'4.5,5,5.5'`) |
| `getOpposite('Apfel,Birne,Pflaume,Pfirsich')` | String → explode mit Delimiter (Array) |
| `getOpposite($object)` | Kein Array/String → `false` |
| `getOpposite(getOpposite($stringList))` | Hin- & Rückweg (implode/explode) sind invers zueinander |

Implementation:
```php
public function getOpposite($value, $delimiter = ',') {
    if (is_array($value)) {
        return implode($delimiter, $value);
    }
    if (is_string($value)) {
        return explode($delimiter, $value);
    }
    return false;
}
```

---

## Aufgabe 3: Auswerten

Vergleich meiner Lösung mit den anderen via text diff.

**Vergleichspunkte, die zu prüfen sind:**
* Wurden dieselben Funktionen (`mb_strlen`/`mb_substr` vs. `strlen`/`substr`) verwendet?
* Gleiche Rückgabewerte für Fehlerfälle (`false` bei ungültigen Eingaben)?
* Unterschiede nur im Stil (z. B. `empty()` vs. `count() === 0`) oder in der Logik?

**Mögliche Abweichungen:**
* Bei `shortString()` führt die Verwendung von `strlen()` statt `mb_strlen()` zu falschen Ergebnissen bei Umlauten.
* Bei `calcAverage()` fehlende Validierung der Werte (gibt dann `false` nicht zurück) würde Tests rot machen.
* Andere Delimiter-Handhabung bei `getOpposite()` (z. B. Trimmen von Leerzeichen) ändert das Verhalten.

Ich konnte meine Lösung nicht mit anderen abgleichen.