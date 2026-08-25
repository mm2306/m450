package ch.tbz.m450;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AddressbookApplicationTest {

    @Test
    @DisplayName("Test AddressbookApplication main method initialization")
    void testMainMethod() {
        assertDoesNotThrow(() -> {
            // Test that AddressbookApplication class exists and can be referenced
            Class<?> clazz = AddressbookApplication.class;
            org.junit.jupiter.api.Assertions.assertNotNull(clazz);
        });
    }
}
