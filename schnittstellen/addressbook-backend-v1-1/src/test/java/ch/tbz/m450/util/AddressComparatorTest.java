package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private Address addr1;
    private Address addr2;
    private Address addr3;
    private Address addr4;

    @BeforeEach
    void setUp() {
        Date now = new Date();
        Date later = new Date(now.getTime() + 10000);

        addr1 = new Address(1, "Anna", "Bern", "0791111111", now);
        addr2 = new Address(2, "Zoe", "Bern", "0792222222", later);
        addr3 = new Address(3, "Bob", "Adam", "0793333333", now);
        addr4 = new Address(4, "Anna", "Bern", "0790000000", now);
    }

    @Test
    @DisplayName("Aufgabe 1: Default comparison sorts by lastname, then firstname, then id")
    void testDefaultComparator() {
        AddressComparator comparator = new AddressComparator();

        // Adam comes before Bern
        assertTrue(comparator.compare(addr3, addr1) < 0);
        assertTrue(comparator.compare(addr1, addr3) > 0);

        // Same lastname ("Bern"), Anna comes before Zoe
        assertTrue(comparator.compare(addr1, addr2) < 0);

        // Same lastname ("Bern") & firstname ("Anna"), id 1 comes before id 4
        assertTrue(comparator.compare(addr1, addr4) < 0);

        // Same object returns 0
        assertEquals(0, comparator.compare(addr1, addr1));
    }

    @Test
    @DisplayName("Aufgabe 2: Compare by primary attribute FIRSTNAME")
    void testCompareByFirstname() {
        AddressComparator comparator = new AddressComparator(AddressComparator.SortField.FIRSTNAME);

        // Anna (addr1) comes before Bob (addr3)
        assertTrue(comparator.compare(addr1, addr3) < 0);

        // Bob (addr3) comes before Zoe (addr2)
        assertTrue(comparator.compare(addr3, addr2) < 0);
    }

    @Test
    @DisplayName("Aufgabe 2: Compare by primary attribute PHONENUMBER")
    void testCompareByPhonenumber() {
        AddressComparator comparator = new AddressComparator(AddressComparator.SortField.PHONENUMBER);

        // 0790000000 (addr4) comes before 0791111111 (addr1)
        assertTrue(comparator.compare(addr4, addr1) < 0);
    }

    @Test
    @DisplayName("Aufgabe 2: Compare by primary attribute REGISTRATION_DATE")
    void testCompareByRegistrationDate() {
        AddressComparator comparator = new AddressComparator(AddressComparator.SortField.REGISTRATION_DATE);

        // addr1 (now) comes before addr2 (later)
        assertTrue(comparator.compare(addr1, addr2) < 0);
    }

    @Test
    @DisplayName("Aufgabe 2: Custom multi-field sort sequence")
    void testCustomSortFields() {
        AddressComparator comparator = new AddressComparator(
                List.of(AddressComparator.SortField.PHONENUMBER, AddressComparator.SortField.LASTNAME)
        );

        List<Address> list = new ArrayList<>(List.of(addr1, addr2, addr3, addr4));
        list.sort(comparator);

        assertEquals("0790000000", list.get(0).getPhonenumber());
        assertEquals("0791111111", list.get(1).getPhonenumber());
        assertEquals("0792222222", list.get(2).getPhonenumber());
        assertEquals("0793333333", list.get(3).getPhonenumber());
    }

    @Test
    @DisplayName("Test null safety in AddressComparator")
    void testNullHandling() {
        AddressComparator comparator = new AddressComparator();

        assertTrue(comparator.compare(null, addr1) < 0);
        assertTrue(comparator.compare(addr1, null) > 0);
        assertEquals(0, comparator.compare(null, null));

        Address nullFieldsAddress = new Address(5, null, null, null, null);
        assertTrue(comparator.compare(nullFieldsAddress, addr1) < 0);
    }
}
