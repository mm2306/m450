package ch.tbz.m450.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        address = new Address(1, "Max", "Muster", "0791234567", now);
    }

    @Test
    @DisplayName("Test constructor and getters")
    void testConstructorAndGetters() {
        assertEquals(1, address.getId());
        assertEquals("Max", address.getFirstname());
        assertEquals("Muster", address.getLastname());
        assertEquals("0791234567", address.getPhonenumber());
        assertEquals(now, address.getRegistrationDate());
    }

    @Test
    @DisplayName("Test setters")
    void testSetters() {
        Date newDate = new Date(now.getTime() + 10000);
        address.setId(2);
        address.setFirstname("Erika");
        address.setLastname("Mustermann");
        address.setPhonenumber("0789876543");
        address.setRegistrationDate(newDate);

        assertEquals(2, address.getId());
        assertEquals("Erika", address.getFirstname());
        assertEquals("Mustermann", address.getLastname());
        assertEquals("0789876543", address.getPhonenumber());
        assertEquals(newDate, address.getRegistrationDate());
    }

    @Test
    @DisplayName("Test no-args constructor")
    void testNoArgsConstructor() {
        Address emptyAddress = new Address();
        assertNull(emptyAddress.getFirstname());
        assertNull(emptyAddress.getLastname());
        assertEquals(0, emptyAddress.getId());
    }
}
