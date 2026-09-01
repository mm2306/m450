# Schnittstellen

### Aufgabe 1
#### *Schreiben Sie Tests für alle Klassen, benutzen Sie Annotationen wie @BeforeEach, fangen Sie an mit dem Testen von Adressen, testen Sie den Service durch Mocken der H2 Datenbank und implementieren Sie die Comparator Klasse korrekt.*

#### **1. Inbetriebnahme und Projektstruktur**
Das Projekt **`addressbook-backend`** (Java 21 / Spring Boot 3.5) wurde im Ordner [addressbook-backend-v1-1](addressbook-backend-v1-1) eingerichtet und konfiguriert.

#### **2. Implementierung der Comparator-Klasse (`AddressComparator`)**
Die Klasse [AddressComparator.java](addressbook-backend-v1-1/src/main/java/ch/tbz/m450/util/AddressComparator.java) wurde korrekt implementiert. Standardmässig vergleicht sie Adressen nach Nachname (`lastname`), Vorname (`firstname`) und ID (`id`).

#### **3. Unit Tests für Domain-Modelle (`Address`)**
- [AddressTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/repository/AddressTest.java): Testet Erstellung, Getter, Setter und Konstruktoren der Entity-Klasse `Address`. Zur Vorbereitung der Testdaten wird die Annotation `@BeforeEach` verwendet.

#### **4. Mocking der Datenbank im Service (`AddressServiceTest`)**
- [AddressServiceTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/service/AddressServiceTest.java): Die H2-Datenbank (`AddressRepository`) wird mittels Mockito (`@Mock`) weg-gemockt.
- **Isolierung**: Der `AddressService` wird mit `@InjectMocks` getestet, sodass keine echte Datenbankverbindung nötig ist.
- **Testabdeckung**: Testet die Methoden `save()`, `getAll()` (inkl. Überprüfung der Sortierung durch den Comparator) und `getAddress()`.

#### **5. Controller Tests (`AddressControllerTest`)**
- [AddressControllerTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/controller/AddressControllerTest.java): Testet alle REST-Endpunkte (`createAddress`, `getAddresses`, `getAddress`) isoliert mit Mocks für den `AddressService`.

---

### **Aufgabe 2**
#### *Erweitern Sie die Comparator Klasse, sodass nach zusätzlichen Attributen verglichen werden kann, und testen Sie entsprechend die neue Funktionalität.*

#### **1. Erweiterung der `AddressComparator`-Klasse**
Die Klasse [AddressComparator.java](addressbook-backend-v1-1/src/main/java/ch/tbz/m450/util/AddressComparator.java) wurde um ein Enum `SortField` sowie flexible Konstruktoren erweitert:
- **Attributes**: Vergleiche nach allen Attributen der Adresse (`LASTNAME`, `FIRSTNAME`, `PHONENUMBER`, `ID`, `REGISTRATION_DATE`).
- **Flexible Priorisierung**: Ermöglicht den Vergleich nach einzelnen primären Attributen sowie custom Listen von Sortierkriterien.

#### **2. Unit Tests für die erweiterte Comparator-Funktionalität**
- [AddressComparatorTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/util/AddressComparatorTest.java): Testet sowohl den Standard-Vergleich als auch den Vergleich nach zusätzlichen Attributen (`FIRSTNAME`, `PHONENUMBER`, `REGISTRATION_DATE`) sowie benutzerdefinierte Multi-Attribut-Sortierungen und Null-Handling.

---

### **Ausführung**

Die gesamte Testsuite (18 Tests) lässt sich wie folgt ausführen:

```bash
cd schnittstellen/addressbook-backend-v1-1
mvn test
```
