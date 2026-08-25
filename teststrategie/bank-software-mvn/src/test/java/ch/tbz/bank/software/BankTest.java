package ch.tbz.bank.software;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("White-Box Tests: Bank Class")
class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        Account.counter = 0;
        bank = new Bank();
    }

    @Nested
    @DisplayName("White-Box: Bank.getAccount(int nr)")
    class GetAccountTests {

        @Test
        @DisplayName("WB: Existierende Kontonummer in Liste")
        void testGetAccountExisting() {
            Account acc1 = bank.createAccount("Muster", Currency.CHF, 100.0);
            Account acc2 = bank.createAccount("Meier", Currency.EUR, 200.0);

            Account found = bank.getAccount(acc2.getId());
            assertNotNull(found);
            assertEquals(acc2.getId(), found.getId());
            assertEquals("Meier", found.getUserLastName());
        }

        @Test
        @DisplayName("WB: Nicht-existierende Kontonummer (Return: null)")
        void testGetAccountNonExisting() {
            bank.createAccount("Muster", Currency.CHF, 100.0);
            Account found = bank.getAccount(999);
            assertNull(found);
        }

        @Test
        @DisplayName("WB: Leere Kontoliste (Return: null)")
        void testGetAccountEmptyBank() {
            Account found = bank.getAccount(1);
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("White-Box: Bank.deleteAccount(Account a)")
    class DeleteAccountTests {

        @Test
        @DisplayName("WB: Konto in Liste vorhanden")
        void testDeleteAccountExisting() {
            Account acc1 = bank.createAccount("Muster", Currency.CHF, 100.0);
            assertEquals(1, bank.getNumberOfAccounts());

            bank.deleteAccount(acc1);
            assertEquals(0, bank.getNumberOfAccounts());
            assertNull(bank.getAccount(acc1.getId()));
        }

        @Test
        @DisplayName("WB: Konto nicht in Liste / null")
        void testDeleteAccountNonExisting() {
            Account acc1 = bank.createAccount("Muster", Currency.CHF, 100.0);
            Account foreignAccount = new Account("Fremd", Currency.USD, 500.0);

            bank.deleteAccount(foreignAccount);
            assertEquals(1, bank.getNumberOfAccounts());
            assertNotNull(bank.getAccount(acc1.getId()));
        }
    }

    @Nested
    @DisplayName("White-Box: Auxiliary Bank Methods")
    class AuxiliaryBankTests {

        @Test
        @DisplayName("WB: Account creation and count")
        void testCreateAccountAndCount() {
            assertEquals(0, bank.getNumberOfAccounts());

            bank.createAccount("Rockefeller", Currency.USD, 1500);
            bank.createAccount("Gates", Currency.EUR, 2000);

            assertEquals(2, bank.getNumberOfAccounts());
        }

        @Test
        @DisplayName("WB: Print methods executed without exceptions")
        void testPrintMethods() {
            Account acc1 = bank.createAccount("Muster", Currency.CHF, 100.0);
            Account acc2 = bank.createAccount("Meier", Currency.EUR, 200.0);

            assertDoesNotThrow(() -> bank.printAccountDetails(acc1));
            assertDoesNotThrow(() -> bank.printBalance(acc1));
            assertDoesNotThrow(() -> bank.printAccountsList());
            assertDoesNotThrow(() -> bank.printOtherAccounts(acc1));

            // Print non-existing account
            Account nonExisting = new Account("Ghost", Currency.USD, 0);
            assertDoesNotThrow(() -> bank.printAccountDetails(nonExisting));
        }
    }
}
