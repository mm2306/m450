package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address addr1;
    private Address addr2;

    @BeforeEach
    void setUp() {
        Date now = new Date();
        addr1 = new Address(1, "Hans", "Zimmermann", "0791112233", now);
        addr2 = new Address(2, "Anna", "Abächerli", "0794445566", now);
    }

    @Test
    @DisplayName("Test save() calls repository and returns saved address")
    void testSave() {
        when(addressRepository.save(addr1)).thenReturn(addr1);

        Address result = addressService.save(addr1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Zimmermann", result.getLastname());
        verify(addressRepository, times(1)).save(addr1);
    }

    @Test
    @DisplayName("Test getAll() returns sorted list from repository without accessing H2 DB")
    void testGetAll() {
        // Return unsorted list from mock
        when(addressRepository.findAll()).thenReturn(List.of(addr1, addr2));

        List<Address> sortedResult = addressService.getAll();

        assertEquals(2, sortedResult.size());
        // Abächerli (addr2) must be first, Zimmermann (addr1) second
        assertEquals("Abächerli", sortedResult.get(0).getLastname());
        assertEquals("Zimmermann", sortedResult.get(1).getLastname());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getAddress() returns address when ID exists")
    void testGetAddressFound() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(addr1));

        Optional<Address> result = addressService.getAddress(1);

        assertTrue(result.isPresent());
        assertEquals("Hans", result.get().getFirstname());
        verify(addressRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test getAddress() returns empty Optional when ID does not exist")
    void testGetAddressNotFound() {
        when(addressRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Address> result = addressService.getAddress(999);

        assertTrue(result.isEmpty());
        verify(addressRepository, times(1)).findById(999);
    }
}
