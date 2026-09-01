# Grundlagen

### Aufgabe 1
#### *Welche Formen von Tests kennen Sie aus der Informatik? Wie werden die Tests durchgeführt?*

#### **Komponententest** (Unit Test)

**Beschreibung**: Testet kleinste isolierte Einheiten des Quellcodes (z. B. einzelne Funktionen, Methoden oder Klassen).

**Durchführung**: Entwickler schreiben automatisierte Testfälle mit Test-Frameworks (z. B. JUnit, PyTest). Diese laufen häufig direkt bei jedem Build ab.

#### **Integrationstest**

**Beschreibung**: Überprüft das Zusammenspiel mehrerer Schnittstellen oder Module (z. B. Datenbankanbindung an die Logikschicht).

**Durchführung**: Komponenten werden zusammengeführt und durch automatisierte Skripte oder Testtools auf korrekte Kommunikation hin überprüft.

#### **Systemtest**

**Beschreibung**: Prüft das Gesamtsystem inklusive aller Schnittstellen und der Benutzeroberfläche gegen die definierten Anforderungen.

**Durchführung**: Spezielle Tester führen automatisierte End-to-End-Tests (z. B. mit Selenium) oder manuelle Testfälle durch.

---

### **Aufgabe 2**
#### *Nennen Sie ein Beispiel eines SW-Fehlers und eines SW-Mangels. Nennen Sie ein Beispiel für einen hohen Schaden bei einem SW-Fehler.*

#### **SW-Fehler**

**Beispiel**:
Ein Navigationssystem zeigt an einer T-Kreuzung „Bitte geradeaus fahren“ – obwohl dort direkt ein Baum steht. -> IST-Verhalten != SOLL-Verhalten

#### **SW-Mangel**

**Beispiel**:
Ein Online-Rezeptbuch speichert Rezepte perfekt, erlaubt aber nur die Suche nach dem exakten Titel. Tippt man „Pfannkuchen“ statt „Omas Pfannkuchen“, wird nichts gefunden. -> SOLL-Verhalten nicht angemessen erfüllt

---

### **Aufgabe 3**

Die Implementierung inkl. Tests befinden sich in [Preisberechnung.java](Preisberechnung.java).

Ausführung:
```bash
java grundlagen/Preisberechnung.java
```
