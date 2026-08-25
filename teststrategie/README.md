# Teststrategie

## Übung 1: Rabattregeln Verkaufssoftware

### Beschreibung
Eine Verkaufssoftware berechnet Rabatte basierend auf dem Kaufpreis:
* Kaufpreis < 15’000 CHF: 0 % Rabatt
* Kaufpreis 15’000 CHF bis 20’000 CHF: 5 % Rabatt
* Kaufpreis > 20’000 CHF bis < 25’000 CHF: 7 % Rabatt
* Kaufpreis >= 25’000 CHF: 8,5 % Rabatt

---

### Abstrakte Testfälle
Abstrakte Testfälle nutzen Äquivalenzklassen und logische Operatoren (`<`, `<=`, `>`, `>=`) zur Beschreibung der Bedingungen ohne konkrete Zahlenwerte.

| ID | Beschreibung / Äquivalenzklasse | Logische Bedingung | Erwarteter Rabatt |
|---|---|---|---|
| TC-A01 | Ungültiger Kaufpreis | Kaufpreis <= 0 CHF | Fehler / Ungültige Eingabe |
| TC-A02 | Keinerlei Rabatt (Basisbereich) | 0 CHF < Kaufpreis < 15'000 CHF | 0,0 % Rabatt |
| TC-A03 | Stufe 1 Rabatt | 15'000 CHF <= Kaufpreis <= 20'000 CHF | 5,0 % Rabatt |
| TC-A04 | Stufe 2 Rabatt | 20'000 CHF < Kaufpreis < 25'000 CHF | 7,0 % Rabatt |
| TC-A05 | Stufe 3 Höchstrabatt | Kaufpreis >= 25'000 CHF | 8,5 % Rabatt |

---

### Konkrete Testfälle (inkl. Grenzwertanalyse)
Konkrete Testfälle verwenden exakte Zahlenwerte an den Grenzen (Boundary Value Analysis) und im Inneren der Äquivalenzklassen.

| ID | Testziel / Grenzwert | Eingabe (Kaufpreis) | Erwarteter Rabatt (%) | Erwarteter Rabatt (CHF) | Erwarteter Endpreis (CHF) |
|---|---|---|---|---|---|
| TC-K01 | Ungültige Eingabe (Negativwert) | -500.00 CHF | N/A | N/A | Fehlermeldung |
| TC-K02 | Untere Grenze Gültigkeit (0 CHF) | 0.00 CHF | 0,0 % | 0.00 CHF | 0.00 CHF |
| TC-K03 | Äquivalenzklasse 1 (Mitte) | 10'000.00 CHF | 0,0 % | 0.00 CHF | 10'000.00 CHF |
| TC-K04 | Obere Grenze kein Rabatt | 14'999.95 CHF | 0,0 % | 0.00 CHF | 14'999.95 CHF |
| TC-K05 | Untere Grenze 5% Rabatt | 15'000.00 CHF | 5,0 % | 750.00 CHF | 14'250.00 CHF |
| TC-K06 | Äquivalenzklasse 2 (Mitte) | 18'000.00 CHF | 5,0 % | 900.00 CHF | 17'100.00 CHF |
| TC-K07 | Obere Grenze 5% Rabatt | 20'000.00 CHF | 5,0 % | 1'000.00 CHF | 19'000.00 CHF |
| TC-K08 | Untere Grenze 7% Rabatt | 20'000.05 CHF | 7,0 % | 1'400.00 CHF | 18'600.05 CHF |
| TC-K09 | Äquivalenzklasse 3 (Mitte) | 22'500.00 CHF | 7,0 % | 1'575.00 CHF | 20'925.00 CHF |
| TC-K10 | Obere Grenze 7% Rabatt | 24'999.95 CHF | 7,0 % | 1'749.996 CHF | 23'249.95 CHF |
| TC-K11 | Untere Grenze 8.5% Rabatt | 25'000.00 CHF | 8,5 % | 2'125.00 CHF | 22'875.00 CHF |
| TC-K12 | Äquivalenzklasse 4 (Hoher Wert) | 30'000.00 CHF | 8,5 % | 2'550.00 CHF | 27'450.00 CHF |

---

## Übung 2: Black-Box Tests einer Autovermietungs-Plattform

### Beispiel-Plattform
**Testobjekt**: Online-Autovermietung (z. B. Sixt / Mobility)

### 5 Wichtigste Funktionale Black-Box Testfälle

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|---|---|---|---|---|---|
| 1 | Fahrzeugsuche mit gültigen Reisedaten (Ort, Abhol- & Rückgabedatum) | Verfügbare Fahrzeuge am gewählten Standort werden mit Preis und Details aufgelistet. | Verfügbare Fahrzeuge werden korrekt angezeigt. | OK | - |
| 2 | Eingabe eines Rückgabedatums vor dem Abholdatum | System zeigt eine Fehlermeldung "Rückgabedatum muss nach dem Abholdatum liegen" an und verhindert die Suche. | Fehlermeldung wird wie erwartet angezeigt. | OK | - |
| 3 | Filterung nach Fahrzeugkategorie (z. B. "SUV / Allrad") | Es werden ausschliesslich Fahrzeuge der Kategorie SUV angezeigt. | Es werden auch Kombis und Sedans in den Ergebnissen angezeigt. | Fehler | Filter-Logik in der Datenbanksuchabfrage ignoriert die gewählte Kategorie. |
| 4 | Buchungsabschluss mit gültiger Kreditkarte | Buchung wird erfolgreich bestätigt, Buchungsnummer wird ausgegeben und Bestätigungs-E-Mail gesendet. | Buchung wird erfolgreich mit Buchungsnummer und E-Mail abgeschlossen. | OK | - |
| 5 | Stornierung einer bestehenden Buchung über den Kundenbereich | Buchungsstatus wechselt auf "Storniert", Stornierungsbestätigung wird angezeigt. | Buchung bleibt im Status "Aktiv", Fehlermeldung "Stornierung fehlgeschlagen". | Fehler | Schnittstelle zum Buchungssystem meldet Timeout oder fehlerhafte Stornierungs-API. |

---

## Übung 3: Analyse & Testfälle der Bank-Software

### 1. Black-Box Testfälle (Benutzerperspektive)
Black-Box Tests überprüfen das Verhalten der Anwendung über das Menu / die Konsole, ohne den Quellcode zu kennen.

| ID | Testfall / Szenario | Eingabewerte | Erwartetes Resultat |
|---|---|---|---|
| BB-01 | Neuanlage eines Kontos | Name: "Muster", Währung: "CHF" | Konto wird angelegt, zugewiesene ID (z.B. 1) und Startguthaben 0.00 CHF angezeigt. |
| BB-02 | Geldeinzahlung (Gültiger Betrag) | Einzahlungsbetrag: `150.00` | Kontostand erhöht sich auf `150.00 CHF`. |
| BB-03 | Geldeinzahlung (Ungültiger Betrag / Text) | Einzahlungsbetrag: `abc` | Meldung `! Ungültige Eingabe, bitte nochmals!` erscheint; Kontostand bleibt unverändert. |
| BB-04 | Geldabhebung (Guthaben ausreichend) | Abhebebetrag: `50.00` bei Kontostand `150.00` | Abhebung erfolgreich; neuer Kontostand beträgt `100.00 CHF`. |
| BB-05 | Geldabhebung (Guthaben unzureichend) | Abhebebetrag: `200.00` bei Kontostand `100.00` | Fehlermeldung `! Kontostand zu niedrig!`; Abhebung wird abgebrochen. |
| BB-06 | Kontoüberweisung (Gleiche Währung) | Von Konto 1 zu Konto 2, Betrag `40.00` | Betrag wird von Konto 1 abgezogen und Konto 2 gutgeschrieben. |
| BB-07 | Überweisung auf eigenes Konto | Zielkonto = Quellkonto (z. B. `1`) | Fehlermeldung `! Bitte ein anderes Konto als das momentane Konto auswählen!`. |
| BB-08 | Wechselkursabfrage (CHF zu USD) | Eingabe: `CHF USD` | Aktueller Kurs wird ausgegeben (z. B. `1 CHF = 1.11 USD`). |

---

### 2. White-Box Testfälle (Entwicklerperspektive & Methodenanalyse)
White-Box Tests prüfen die interne Logik, Pfade und Randbedingungen im Java-Quellcode.

#### Für White-Box Tests geeignete Methoden:

1. **`Account.deposit(double amount)`**
   * *Testpfade*: Positiver Betrag (`amount > 0`), Nullbetrag (`amount == 0`), Negativer Betrag (`amount < 0`).
   * *Ziel*: Prüfen, ob negative Einzahlungen das Guthaben verringern (aktueller Bug im Code).

2. **`Account.withdraw(double amount)`**
   * *Testpfade*: `amount < balance` (Return: `true`), `amount == balance` (Return: `true`), `amount > balance` (Return: `false`), `amount <= 0` (Randfall).
   * *Ziel*: Korrekte Rückgabewerte und Saldoprüfung verifizieren.

3. **`Bank.getAccount(int nr)`**
   * *Testpfade*: Existierende Kontonummer in Liste, Nicht-existierende Kontonummer (Return: `null`), Leere Kontoliste.
   * *Ziel*: Suchen-Logik und Behandlung von Nicht-Funden prüfen.

4. **`Bank.deleteAccount(Account a)`**
   * *Testpfade*: Konto in Liste vorhanden, Konto nicht in Liste / `null`.
   * *Ziel*: Entfernen aus der `ArrayList` verifizieren.

5. **`Counter.convertCurrency(double amount, Currency currencyFrom, Currency currencyTo)`**
   * *Testpfade*: USD -> CHF, USD -> EUR, CHF -> USD, Nicht unterstützte Währungspaare (z. B. EUR -> CHF).
   * *Ziel*: Korrekte Umrechnungsfaktoren und Fallback-Verhalten prüfen.

---

### 3. Generelle Verbesserungsvorschläge & Best Practices

1. **Datentyp für Geldbeträge verändern (`BigDecimal` statt `double`)**
   * *Problem*: Der Datentyp `double` verursacht Fließkomma-Rundungsfehler (z. B. `0.1 + 0.2 = 0.30000000000000004`).
   * *Lösung*: Verwenden von `java.math.BigDecimal` oder Ganzzahlen (Cents) für exakte kaufmännische Berechnungen.

2. **Eingabe- & Logikvalidierung im Domänenmodell (Guard Clauses)**
   * *Problem*: In `Account.java` akzeptiert `deposit()` negative Beträge und `withdraw()` negative Abhebungen.
   * *Lösung*: Validierungslogik direkt in den Methoden der Klasse `Account` einbauen (`if (amount <= 0) throw new IllegalArgumentException("Betrag muss positiv sein");`).

3. **Rechtschreibfehler & Naming Conventions**
   * *Problem*: Die Exception-Klasse in `Counter.java` heißt `AccountExeption` (Fehlendes "c").
   * *Lösung*: Umbenennen in `AccountException`.

4. **Separation of Concerns (Trennung von Geschäftslogik und UI)**
   * *Problem*: Klassen wie `Bank` enthalten direkte `System.out.println()` Ausgaben.
   * *Lösung*: Entkopplung von Datenmodell/Geschäftslogik und Benutzeroberfläche (Konsolenausgaben nur in `Counter` / UI-Schicht ausführen).

5. **Unvollständige Währungsumrechnung in `convertCurrency`**
   * *Problem*: Es sind nur 3 Umrechnungskombinationen hartcodiert. Bei anderen Kombinationen (z. B. EUR zu CHF) wird 1:1 zurückgegeben mit einem Konsolenhinweis.
   * *Lösung*: Vollständige Wechselkurs-Matrix implementieren oder direkt den Webservice aus `ExchangeRateOkhttp` einbinden.

6. **Threadsicherheit & ID-Generierung**
   * *Problem*: `static int counter` in `Account` ist nicht threadsicher und setzt sich beim Neustart zurück.
   * *Lösung*: Verwenden von `AtomicInteger` oder Erzeugung eindeutiger IDs (UUID) zentral in der `Bank`-Klasse.

7. **Beseitigung von Dead Code**
   * *Problem*: Methode `pseudoDeleteAccount()` in `Account.java` wird nicht verwendet und enthält auskommentierte Fehlversuche bezüglich Java Pass-By-Value.
   * *Lösung*: Nicht genutzten/toten Code ersatzlos entfernen.
