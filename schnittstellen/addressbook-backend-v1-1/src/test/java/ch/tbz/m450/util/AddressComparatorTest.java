package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private Address addr1; // Anna Bauer (ID 1)
    private Address addr2; // Zoe Bauer (ID 2)
    private Address addr3; // Bob Anders (ID 3)
    private Address addr4; // Anna Bauer (ID 4)
    private AddressComparator comparator;

    @BeforeEach
    void setUp() {
        Date now = new Date();
        comparator = new AddressComparator();

        addr1 = new Address(1, "Anna", "Bauer", "0791111111", now);
        addr2 = new Address(2, "Zoe", "Bauer", "0792222222", now);
        addr3 = new Address(3, "Bob", "Anders", "0793333333", now);
        addr4 = new Address(4, "Anna", "Bauer", "0790000000", now);
    }

    @Test
    @DisplayName("Sortiert Adressen korrekt: Nachname, Vorname, ID")
    void testSortsAddressesCorrectly() {
        List<Address> addresses = Arrays.asList(addr2, addr4, addr1, addr3);

        // Mit dem Comparator sortieren
        addresses.sort(comparator);

        // Erwartete Reihenfolge:
        // 1. Bob Anders (Nachname A)
        // 2. Anna Bauer ID 1 (Nachname B, Vorname A, ID 1)
        // 3. Anna Bauer ID 4 (Nachname B, Vorname A, ID 4)
        // 4. Zoe Bauer ID 2 (Nachname B, Vorname Z)
        List<Address> expectedOrder = List.of(addr3, addr1, addr4, addr2);

        assertIterableEquals(expectedOrder, addresses);
    }

    @Test
    @DisplayName("Null-Werte werden an den Anfang sortiert (nullsFirst)")
    void testSortsWithNullValues() {
        Address nullFieldsAddress = new Address(5, null, null, null, null);
        List<Address> addresses = Arrays.asList(addr1, null, nullFieldsAddress);

        addresses.sort(comparator);

        // nullsFirst schiebt 'null' ganz nach vorne, danach Adressen mit null-Feldern
        List<Address> expectedOrder = List.of(null, nullFieldsAddress, addr1);

        assertIterableEquals(expectedOrder, addresses);
    }
}