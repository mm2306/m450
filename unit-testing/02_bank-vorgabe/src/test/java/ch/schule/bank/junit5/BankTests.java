package ch.schule.bank.junit5;

import ch.schule.Bank;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Klasse 'Bank'.
 *
 * @author xxxx
 * @version 1.0
 */
public class BankTests {
    /**
     * Tests to create new Accounts
     */
    @Test
    public void testCreate() {
        Bank bank = new Bank();

        String savingsId = bank.createSavingsAccount();
        assertEquals("S-1000", savingsId);

        String promoId = bank.createPromoYouthSavingsAccount();
        assertEquals("Y-1001", promoId);

        String salaryId = bank.createSalaryAccount(-50000);
        assertEquals("P-1002", salaryId);

        // Creating salary account with positive credit limit must fail (return null)
        String invalidSalaryId = bank.createSalaryAccount(50000);
        assertNull(invalidSalaryId);

        // Check reference getter/setter
        SavingsAccount dummy = new SavingsAccount("S-9999");
        bank.setAccount(dummy);
        assertEquals(dummy, bank.getAccount());
    }

    /**
     * Testet das Einzahlen auf ein Konto.
     */
    @Test
    public void testDeposit() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();

        assertTrue(bank.deposit(id, 1, 10000));
        assertEquals(10000, bank.getBalance(id));

        // Deposit on non-existent account should fail
        assertFalse(bank.deposit("NON_EXISTENT", 1, 10000));

        // Deposit negative amount should fail
        assertFalse(bank.deposit(id, 2, -1000));
    }

    /**
     * Testet das Abheben von einem Konto.
     */
    @Test
    public void testWithdraw() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 10000);

        assertTrue(bank.withdraw(id, 2, 4000));
        assertEquals(6000, bank.getBalance(id));

        // Withdraw from non-existent account should fail
        assertFalse(bank.withdraw("NON_EXISTENT", 3, 1000));

        // Exceeding balance on savings account should fail
        assertFalse(bank.withdraw(id, 4, 10000));
        assertEquals(6000, bank.getBalance(id));
    }

    /**
     * Experimente mit print().
     */
    @Test
    public void testPrint() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 1000);

        bank.print(id);
        bank.print("NON_EXISTENT");
    }

    /**
     * Experimente mit print(year, month).
     */
    @Test
    public void testMonthlyPrint() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();
        bank.deposit(id, 5, 1000);

        bank.print(id, 1970, 1);
        bank.print("NON_EXISTENT", 1970, 1);
    }

    /**
     * Testet den Gesamtkontostand der Bank.
     */
    @Test
    public void testBalance() {
        Bank bank = new Bank();
        assertEquals(0, bank.getBalance());
        assertEquals(0, bank.getBalance("NON_EXISTENT"));

        String sId = bank.createSavingsAccount();
        String pId = bank.createSalaryAccount(-50000);

        bank.deposit(sId, 1, 10000);
        bank.deposit(pId, 1, 5000);

        assertEquals(10000, bank.getBalance(sId));
        assertEquals(5000, bank.getBalance(pId));

        // Bank.getBalance() calculates total by subtracting account balances: balance -= account.getBalance()
        assertEquals(-15000, bank.getBalance());
    }

    /**
     * Tested die Ausgabe der "top 5" konten.
     */
    @Test
    public void testTop5() {
        Bank bank = new Bank();

        for (int i = 0; i < 7; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, (i + 1) * 1000);
        }

        bank.printTop5();
    }

    /**
     * Tested die Ausgabe der "bottom 5" konten.
     */
    @Test
    public void testBottom5() {
        Bank bank = new Bank();

        for (int i = 0; i < 7; i++) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, 1, (i + 1) * 1000);
        }

        bank.printBottom5();
    }
}
