# Unit Testing - Übungen

## Aufgabe 1 - Simpler Rechner

### Beschreibung
Erstellung einer Klasse `Calculator` mit den Grundrechenarten (`add`, `subtract`, `multiply`, `divide`) sowie einer zugehörigen JUnit 5 Testklasse `CalculatorTest`.

### Quellcode-Dateien:
* Hauptklasse: [`src/main/java/ch/tbz/Calculator.java`](file:///projects/m450/unit-testing/src/main/java/ch/tbz/Calculator.java)
* Testklasse: [`src/test/java/ch/tbz/CalculatorTest.java`](file:///projects/m450/unit-testing/src/test/java/ch/tbz/CalculatorTest.java)

### Testausführung:
1. **In der IDE (z. B. IntelliJ IDEA / Eclipse)**:
   * Rechtsklick auf `CalculatorTest.java` $\rightarrow$ `Run 'CalculatorTest'`
2. **Mit Maven auf der Kommandozeile**:
   ```bash
   mvn test
   ```

---

## Aufgabe 2 - JUnit 5 Zusammenfassung

### 1. Gängige JUnit 5 Annotations

| Annotation | Beschreibung | Anwendungsfall / Beispiel |
|---|---|---|
| `@Test` | Kennzeichnet eine Methode als Testmethode. | `@Test void testAdd() { ... }` |
| `@BeforeEach` | Wird vor JEDEM einzelnen Test in der Klasse ausgeführt (Setup). | Instanziieren von Testobjekten (`calculator = new Calculator();`). |
| `@AfterEach` | Wird nach JEDEM einzelnen Test ausgeführt (Teardown/Cleanup). | Ressourcen freigeben, Datenbank-State zurücksetzen. |
| `@BeforeAll` | Wird einmalig vor ALLEM Tests der Klasse ausgeführt (muss `static` sein). | Datenbankverbindungen aufbauen, Server starten. |
| `@AfterAll` | Wird einmalig nach ALLEM Tests der Klasse ausgeführt (muss `static` sein). | Datenbankverbindungen schließen, Server stoppen. |
| `@DisplayName` | Verleiht dem Test einen lesbaren, aussagekräftigen Namen. | `@DisplayName("Division durch Null wirft Exception")` |
| `@Disabled` | Deaktiviert/überspringt einen Test temporär (mit Begründung). | `@Disabled("Bug #123 noch offen")` |
| `@ParameterizedTest` | Führt denselben Test mehrmals mit verschiedenen Parametern aus. | Kombination mit `@ValueSource` oder `@CsvSource`. |
| `@Nested` | Erlaubt verschachtelte Testklassen zur logischen Gruppierung. | `@Nested class AdditionTests { ... }` |

### 2. Gängige JUnit 5 Assertions

* **`assertEquals(expected, actual)`**: Prüft Gleichheit zweier Werte.
* **`assertNotEquals(unexpected, actual)`**: Prüft Ungleichheit.
* **`assertTrue(booleanCondition)` / `assertFalse(booleanCondition)`**: Prüft Wahrheitsgehalt.
* **`assertNull(object)` / `assertNotNull(object)`**: Prüft auf `null`.
* **`assertThrows(ExceptionClass.class, executable)`**: Prüft, ob eine erwartete Exception geworfen wird.
* **`assertAll(executables...)`**: Grouped Assertions – führt alle Überprüfungen aus, selbst wenn eine davon fehlschlägt.

### 3. Empfohlene Referenzseiten
* [JUnit 5 Official User Guide](https://junit.org/junit5/docs/current/user-guide/)
* [Vogella JUnit 5 Tutorial](https://www.vogella.com/tutorials/JUnit/article.html)
* [Baeldung JUnit 5 Overview](https://www.baeldung.com/junit-5)

---

## Aufgabe 3 - Banken Simulation (Software-Dokumentation)

### Projektstruktur & Architektur
Die Banken-Simulation basiert auf einem objektorientierten Modell zur Verwaltung von Bankkonten und Buchungen:

```
                  +-------------------+
                  |      Account      | (Abstrakte / Basis-Kontoklasse)
                  +-------------------+
                    ^       ^       ^
                   /        |        \
    +-----------------+ +--------+ +--------------------------+
    | SavingsAccount  | | Salary | | PromoYouthSavingsAccount |
    +-----------------+ +--------+ +--------------------------+
```

### Funktionsweise & Zusammenhänge der Klassen:
* **`Account`**: Basisklasse für alle Kontoverwaltungen. Speichert die Kontonummer, den Kontostand (`balance`) und eine Liste aller `Booking`-Objekte. Bietet Methoden für `deposit` (Einzahlen) und `withdraw` (Abheben).
* **`SavingsAccount`**: Vererbt von `Account`. Implementiert ein Sparkonto (z. B. mit Auszahlungslimiten oder Zinsregeln).
* **`SalaryAccount`**: Vererbt von `Account`. Stellt ein Lohnkonto dar, welches Überziehungskredite (Credit Limit) erlaubt.
* **`PromoYouthSavingsAccount`**: Vererbt von `SavingsAccount`. Spezielles Jugend-Sparkonto mit Werbeboni/Zinszuschlägen.
* **`Booking`**: Repräsentiert eine einzelne Transaktion mit Datum und Betrag.
* **`Bank`**: Zentrale Verwaltungsklasse. Verwalter aller Konten in einer Liste. Ermöglicht Kontoerstellung, Überweisungen zwischen Konten und Gesamtabfragen.
* **`BankUtils`**: Hilfsklasse für Formatierungen und Datumsberechnungen.
* **`AccountBalanceComparator` / `AccountInverseBalanceComparator`**: `Comparator`-Klassen zum Sortieren der Konten nach Kontostand aufsteigend/absteigend.

---

## Aufgabe 4 - Unit-Tests für Banken-Simulation

### Test-Implementierung & Abdeckung
Die Testklassen im Paket `ch.schule.bank.junit5` wurden analysiert und ergänzt, um eine hohe Code-Abdeckung (Code Coverage) sicherzustellen:

* [`AccountTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/AccountTests.java): Testet Einzahlungen, Abhebungen und Kontostandsabfragen der Basisklasse.
* [`SavingsAccountTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/SavingsAccountTests.java): Testet Sparzinsen und Abbuchungslimiten.
* [`SalaryAccountTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/SalaryAccountTests.java): Testet Überziehungslimit-Berechnungen.
* [`PromoYouthSavingsAccountTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/PromoYouthSavingsAccountTests.java): Testet Bonuszinsen.
* [`BankTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/BankTests.java) / [`TestBank.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/TestBank.java): Testet Kontoanmeldungen, Überweisungen und Suchfunktionen.
* [`BookingTests.java`](file:///projects/m450/unit-testing/02_bank-vorgabe/src/test/java/ch/schule/bank/junit5/BookingTests.java): Testet Transaktionsprotokollierung.
