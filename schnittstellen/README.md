# Schnittstellen

### Aufgabe 1

#### **1. Inbetriebnahme und Projektstruktur**
Das Projekt **`addressbook-backend`** (Java 21 / Spring Boot 3.5) wurde im Ordner [addressbook-backend-v1-1](addressbook-backend-v1-1) eingerichtet und konfiguriert.

#### **2. Implementierung der Comparator-Klasse (`AddressComparator`)**
Die Klasse [AddressComparator.java](addressbook-backend-v1-1/src/main/java/ch/tbz/m450/util/AddressComparator.java) wurde korrekt implementiert. Standardmässig vergleicht sie Adressen nach Nachname (`lastname`), Vorname (`firstname`) und ID (`id`).

#### **3. Unit Tests für Domain-Modelle (`Address`)**
- [AddressTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/repository/AddressTest.java): Testet Erstellung, Getter, Setter und Konstruktoren der Entity-Klasse `Address`. Zur Vorbereitung der Testdaten wird die Annotation `@BeforeEach` verwendet.

#### **4. Service Tests (`AddressServiceTest`)**
- [AddressServiceTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/service/AddressServiceTest.java): Verwendet `@DataJpaTest` mit einer eingebetteten H2-Datenbank.
- **Isolierung**: Der `AddressService` wird manuell mit dem autowired `AddressRepository` instanziiert.
- **Testabdeckung**: Testet die Methoden `save()`, `getAll()` (inkl. Überprüfung der Sortierung durch den Comparator) und `getAddress()`.

#### **5. Controller Tests (`AddressControllerTest`)**
- [AddressControllerTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/controller/AddressControllerTest.java): Testet alle REST-Endpunkte (`createAddress`, `getAddresses`, `getAddress`) mit echtem `AddressService` und eingebetteter H2-Datenbank über `@DataJpaTest`.

---

### **Aufgabe 2**

#### **1. Vereinfachung der `AddressComparator`-Klasse**
Die Klasse [AddressComparator.java](addressbook-backend-v1-1/src/main/java/ch/tbz/m450/util/AddressComparator.java) wurde auf die Standard-Sortierung reduziert und mit der Java-`Comparator`-API umgesetzt:
- **Sortierreihenfolge**: Nachname (`lastname`) → Vorname (`firstname`) → ID (`id`).
- **Implementierung**: Eine statische `Comparator`-Kette mit `Comparator.comparing`, `thenComparing` und `thenComparingInt`.
- **Null-Handling**: `Comparator.nullsFirst` für Adress-Objekte und einzelne String-Felder; Zeichenketten werden case-insensitive verglichen (`String.CASE_INSENSITIVE_ORDER`).

#### **2. Unit Tests für die Comparator-Funktionalität**
- [AddressComparatorTest.java](addressbook-backend-v1-1/src/test/java/ch/tbz/m450/util/AddressComparatorTest.java): Testet die Standard-Sortierung (Nachname, Vorname, ID) sowie Null-Sicherheit bei Adress-Objekten und einzelnen Feldern.

---

### **Ausführung**

Die gesamte Testsuite (14 Tests) lässt sich wie folgt ausführen:

```bash
cd schnittstellen/addressbook-backend-v1-1
mvn test
```
