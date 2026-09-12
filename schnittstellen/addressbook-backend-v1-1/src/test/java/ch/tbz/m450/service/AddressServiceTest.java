package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressServiceTest {

    @Autowired
    private AddressRepository addressRepository;

    private AddressService addressService;

    private Address addr1;
    private Address addr2;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);

        Date now = new Date();
        addr1 = new Address(1, "Hans", "Zimmermann", "0791112233", now);
        addr2 = new Address(2, "Anna", "Abächerli", "0794445566", now);
    }

    @Test
    @DisplayName("Test save() calls repository and returns saved address")
    void testSave() {
        Address result = addressService.save(addr1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Zimmermann", result.getLastname());
    }

    @Test
    @DisplayName("Test getAll() returns sorted list from repository")
    void testGetAll() {
        addressRepository.save(addr1);
        addressRepository.save(addr2);

        List<Address> sortedResult = addressService.getAll();

        assertEquals(2, sortedResult.size());
        assertEquals("Abächerli", sortedResult.get(0).getLastname());
        assertEquals("Zimmermann", sortedResult.get(1).getLastname());
    }

    @Test
    @DisplayName("Test getAddress() returns address when ID exists")
    void testGetAddressFound() {
        addressRepository.save(addr1);

        Optional<Address> result = addressService.getAddress(1);

        assertTrue(result.isPresent());
        assertEquals("Hans", result.get().getFirstname());
    }

    @Test
    @DisplayName("Test getAddress() returns empty Optional when ID does not exist")
    void testGetAddressNotFound() {
        Optional<Address> result = addressService.getAddress(999);

        assertTrue(result.isEmpty());
    }
}
