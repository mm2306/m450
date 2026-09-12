package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private Address addr1;
    private Address addr2;
    private Address addr3;
    private Address addr4;

    @BeforeEach
    void setUp() {
        Date now = new Date();

        addr1 = new Address(1, "Anna", "Bauer", "0791111111", now);
        addr2 = new Address(2, "Zoe", "Bauer", "0792222222", now);
        addr3 = new Address(3, "Bob", "Anders", "0793333333", now);
        addr4 = new Address(4, "Anna", "Bauer", "0790000000", now);
    }

    @Test
    @DisplayName("Default comparison sorts by lastname, then firstname, then id")
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
