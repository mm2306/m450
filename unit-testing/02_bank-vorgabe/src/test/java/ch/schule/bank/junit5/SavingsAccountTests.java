package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Klasse SavingsAccount.
 *
 * @author XXX
 * @version 1.0
 */
public class SavingsAccountTests
{
	@Test
	public void testInitialization()
	{
		SavingsAccount account = new SavingsAccount("S-1000");
		assertEquals("S-1000", account.getId());
		assertEquals(0, account.getBalance());
	}

	@Test
	public void testDeposit()
	{
		SavingsAccount account = new SavingsAccount("S-1000");
		assertTrue(account.deposit(1, 10000));
		assertEquals(10000, account.getBalance());
	}

	@Test
	public void testWithdraw()
	{
		SavingsAccount account = new SavingsAccount("S-1000");
		account.deposit(1, 10000);

		// Successful withdrawal within balance
		assertTrue(account.withdraw(2, 5000));
		assertEquals(5000, account.getBalance());

		// Failed withdrawal exceeding balance
		assertFalse(account.withdraw(3, 6000));
		assertEquals(5000, account.getBalance());

		// Failed withdrawal with negative amount
		assertFalse(account.withdraw(4, -1000));
		assertEquals(5000, account.getBalance());
	}
}
