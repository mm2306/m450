package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private Address addr1;
    private Address addr2;

    @BeforeEach
    void setUp() {
        Date now = new Date();
        addr1 = new Address(1, "Peter", "Keller", "0795556677", now);
        addr2 = new Address(2, "Sarah", "Meier", "0798889900", now);
    }

    @Test
    @DisplayName("Test createAddress returns 201 Created and saved address")
    void testCreateAddress() {
        when(addressService.save(addr1)).thenReturn(addr1);

        ResponseEntity<Address> response = addressController.createAddress(addr1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Peter", response.getBody().getFirstname());
        verify(addressService, times(1)).save(addr1);
    }

    @Test
    @DisplayName("Test getAddresses returns 200 OK and list of addresses")
    void testGetAddresses() {
        when(addressService.getAll()).thenReturn(List.of(addr1, addr2));

        ResponseEntity<List<Address>> response = addressController.getAddresses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(addressService, times(1)).getAll();
    }

    @Test
    @DisplayName("Test getAddress returns 200 OK when address exists")
    void testGetAddressSuccess() {
        when(addressService.getAddress(1)).thenReturn(Optional.of(addr1));

        ResponseEntity<Address> response = addressController.getAddress(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Peter", response.getBody().getFirstname());
        verify(addressService, times(1)).getAddress(1);
    }

    @Test
    @DisplayName("Test getAddress returns 404 Not Found when address does not exist")
    void testGetAddressNotFound() {
        when(addressService.getAddress(999)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = addressController.getAddress(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(addressService, times(1)).getAddress(999);
    }
}
