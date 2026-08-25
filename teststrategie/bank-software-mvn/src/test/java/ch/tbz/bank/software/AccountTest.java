package ch.tbz.bank.software;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("White-Box Tests: Account Class")
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        // Reset static counter for predictable IDs in tests
        Account.counter = 0;
        account = new Account("Muster", Currency.CHF, 100.00);
    }

    @Nested
    @DisplayName("White-Box: Account.deposit(double amount)")
    class DepositTests {

        @Test
        @DisplayName("WB: Positiver Betrag (amount > 0)")
        void testDepositPositiveAmount() {
            account.deposit(50.00);
            assertEquals(150.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: Nullbetrag (amount == 0)")
        void testDepositZeroAmount() {
            account.deposit(0.00);
            assertEquals(100.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: Negativer Betrag (amount < 0)")
        void testDepositNegativeAmount() {
            // Documenting current behavior: balance += amount (decreases balance when negative)
            account.deposit(-30.00);
            assertEquals(70.00, account.getBalance(), 0.001);
        }
    }

    @Nested
    @DisplayName("White-Box: Account.withdraw(double amount)")
    class WithdrawTests {

        @Test
        @DisplayName("WB: amount < balance (Return: true)")
        void testWithdrawAmountLessThanBalance() {
            boolean success = account.withdraw(40.00);
            assertTrue(success);
            assertEquals(60.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: amount == balance (Return: true)")
        void testWithdrawAmountEqualsBalance() {
            boolean success = account.withdraw(100.00);
            assertTrue(success);
            assertEquals(0.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: amount > balance (Return: false)")
        void testWithdrawAmountGreaterThanBalance() {
            boolean success = account.withdraw(150.00);
            assertFalse(success);
            assertEquals(100.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: Randfall amount <= 0")
        void testWithdrawNegativeOrZeroAmount() {
            boolean success = account.withdraw(-20.00);
            // In current code: amount <= balance is true, so balance becomes 100 - (-20) = 120
            assertTrue(success);
            assertEquals(120.00, account.getBalance(), 0.001);
        }
    }

    @Nested
    @DisplayName("White-Box: Getters & Auxiliary methods")
    class AuxiliaryTests {

        @Test
        @DisplayName("WB: Verify Account attributes initialization")
        void testAccountInitialization() {
            assertEquals(1, account.getId());
            assertEquals("Muster", account.getUserLastName());
            assertEquals(Currency.CHF, account.getCurrency());
            assertEquals(100.00, account.getBalance(), 0.001);
        }

        @Test
        @DisplayName("WB: pseudoDeleteAccount resets fields")
        void testPseudoDeleteAccount() {
            account.pseudoDeleteAccount();
            assertNull(account.getUserLastName());
            assertNull(account.getCurrency());
            assertEquals(0, account.getId());
            assertEquals(0.00, account.getBalance(), 0.001);
        }
    }
}
