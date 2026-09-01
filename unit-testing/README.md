# Unit Testing - Übungen

Lösungen zu den Aufgaben aus `unit-testing`.

---

## Aufgabe 1: Simpler Rechner
`Calculator`-Klasse mit `CalculatorTest` (JUnit 5).

* **Source:** `src/main/java/ch/tbz/Calculator.java`
* **Test:** `src/test/java/ch/tbz/CalculatorTest.java`

---

## Aufgabe 2: JUnit 5 Zusammenfassung

### Wichtige Annotations
* `@Test`: Testmethode kennzeichnen.
* `@BeforeEach` / `@AfterEach`: Vor/nach jedem Test (Setup/Cleanup).
* `@BeforeAll` / `@AfterAll`: Einmal vor/nach allen Tests (`static`).
* `@DisplayName`: Lesbarer Testname.
* `@Disabled`: Test überspringen.
* `@ParameterizedTest`: Test mit mehreren Datenreihen.

### Wichtige Assertions
* `assertEquals(exp, act)` / `assertNotEquals(...)`: Werte vergleichen.
* `assertTrue(cond)` / `assertFalse(cond)`: Boolean prüfen.
* `assertNull(obj)` / `assertNotNull(obj)`: Null-Check.
* `assertThrows(Exception.class, lambda)`: Exception erwarten.
* `assertAll(...)`: Gruppierte Assertions (bricht bei Fehler nicht ab).

---

## Aufgabe 3: Banken-Simulation (Klassen)
* `Account`: Abstrakte Basisklasse für Konten (Salden & Buchungsliste).
* `SavingsAccount`: Sparkonto mit Zinsen.
* `SalaryAccount`: Lohnkonto mit Überziehungskredit.
* `PromoYouthSavingsAccount`: Jugendkonto mit Bonuszins.
* `Booking`: Transaktion mit Datum und Betrag.
* `Bank`: Verwalter aller Konten & Überweisungen.

---

## Aufgabe 4: Bank-Tests
Tests im Paket `ch.schule.bank.junit5`:

* **`AccountTests.java`**: Einzahlungen & Abhebungen.
* **`SavingsAccountTests.java`**: Limits & Sparzinsen.
* **`SalaryAccountTests.java`**: Überziehungsrahmen.
* **`PromoYouthSavingsAccountTests.java`**: Bonuszinsen.
* **`BankTests.java`** / **`BookingTests.java`**: Überweisungen & Buchungen.

### Testausführung
```bash
cd unit-testing/02_bank-vorgabe
mvn test
```
