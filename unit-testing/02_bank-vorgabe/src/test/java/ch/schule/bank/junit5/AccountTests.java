package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Booking;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Klasse Account.
 *
 * @author xxxx
 * @version 1.0
 */
public class AccountTests {
    /**
     * Testet die Initialisierung eines Kontos.
     */
    @Test
    public void testInit() {
        Account account = new SavingsAccount("S-100");
        assertEquals("S-100", account.getId());
        assertEquals(0, account.getBalance());
        assertTrue(account.canTransact(0));
    }

    /**
     * Testet das Einzahlen auf ein Konto.
     */
    @Test
    public void testDeposit() {
        Account account = new SavingsAccount("S-100");

        // Positive deposit
        assertTrue(account.deposit(1, 1000));
        assertEquals(1000, account.getBalance());

        // Negative deposit should fail
        assertFalse(account.deposit(2, -500));
        assertEquals(1000, account.getBalance());

        // Deposit with date prior to last transaction should fail
        assertFalse(account.deposit(0, 500));
        assertEquals(1000, account.getBalance());
    }

    /**
     * Testet das Abheben von einem Konto.
     */
    @Test
    public void testWithdraw() {
        Account account = new SavingsAccount("S-100");
        account.deposit(1, 1000);

        // Positive withdrawal
        assertTrue(account.withdraw(2, 400));
        assertEquals(600, account.getBalance());

        // Negative withdrawal should fail
        assertFalse(account.withdraw(3, -200));
        assertEquals(600, account.getBalance());

        // Withdrawal with date prior to last transaction should fail
        assertFalse(account.withdraw(1, 200));
        assertEquals(600, account.getBalance());
    }

    /**
     * Tests the reference from SavingsAccount
     */
    @Test
    public void testReferences() {
        Account account = new SavingsAccount("S-100");
        Booking booking = new Booking(1, 500);

        assertNull(account.getBooking());
        account.setBooking(booking);
        assertEquals(booking, account.getBooking());
    }

    /**
     * Tests the canTransact Flag
     */
    @Test
    public void testCanTransact() {
        Account account = new SavingsAccount("S-100");

        // Initial empty account accepts any transaction date
        assertTrue(account.canTransact(0));
        assertTrue(account.canTransact(5));

        account.deposit(10, 1000);

        assertTrue(account.canTransact(10));
        assertTrue(account.canTransact(15));
        assertFalse(account.canTransact(9));
    }

    /**
     * Experimente mit print().
     */
    @Test
    public void testPrint() {
        Account account = new SavingsAccount("S-100");
        account.deposit(1, 1000);
        account.withdraw(2, 300);

        account.print();
    }

    /**
     * Experimente mit print(year,month).
     */
    @Test
    public void testMonthlyPrint() {
        Account account = new SavingsAccount("S-100");
        // Date 5 corresponds to Jan 1970
        account.deposit(5, 1000);
        // Date 35 corresponds to Feb 1970
        account.deposit(35, 500);

        account.print(1970, 1);
        account.print(1970, 2);
    }
}
