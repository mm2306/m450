package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressControllerTest {

    @Autowired
    private AddressRepository addressRepository;

    private AddressService addressService;
    private AddressController addressController;

    private Address addr1;
    private Address addr2;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);
        addressController = new AddressController(addressService);

        Date now = new Date();
        addr1 = new Address(1, "Peter", "Keller", "0795556677", now);
        addr2 = new Address(2, "Sarah", "Meier", "0798889900", now);
    }

    @Test
    @DisplayName("Test createAddress returns 201 Created and saved address")
    void testCreateAddress() {
        ResponseEntity<Address> response = addressController.createAddress(addr1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Peter", response.getBody().getFirstname());
    }

    @Test
    @DisplayName("Test getAddresses returns 200 OK and list of addresses")
    void testGetAddresses() {
        addressRepository.save(addr1);
        addressRepository.save(addr2);

        ResponseEntity<List<Address>> response = addressController.getAddresses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Test getAddress returns 200 OK when address exists")
    void testGetAddressSuccess() {
        addressRepository.save(addr1);

        ResponseEntity<Address> response = addressController.getAddress(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Peter", response.getBody().getFirstname());
    }

    @Test
    @DisplayName("Test getAddress returns 404 Not Found when address does not exist")
    void testGetAddressNotFound() {
        ResponseEntity<Address> response = addressController.getAddress(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
