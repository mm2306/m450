package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests der Klasse SalaryAccount.
 *
 * @author XXX
 * @version 1.1
 */
public class SalaryAccountTests
{
	@Test
	public void testInitialization()
	{
		SalaryAccount account = new SalaryAccount("P-1000", -50000);
		assertEquals("P-1000", account.getId());
		assertEquals(0, account.getBalance());
	}

	@Test
	public void testWithdrawWithinLimit()
	{
		SalaryAccount account = new SalaryAccount("P-1000", -50000);
		
		// Withdraw allowed into credit limit (up to -50000)
		assertTrue(account.withdraw(1, 30000));
		assertEquals(-30000, account.getBalance());
	}

	@Test
	public void testWithdrawExceedingLimit()
	{
		SalaryAccount account = new SalaryAccount("P-1000", -50000);
		account.withdraw(1, 30000); // balance is now -30000

		// Attempt withdrawal that would exceed credit limit (-60000 < -50000)
		assertFalse(account.withdraw(2, 30000));
		assertEquals(-30000, account.getBalance());
	}

	@Test
	public void testDepositAndWithdraw()
	{
		SalaryAccount account = new SalaryAccount("P-1000", -50000);
		assertTrue(account.deposit(1, 20000));
		assertEquals(20000, account.getBalance());

		assertTrue(account.withdraw(2, 60000));
		assertEquals(-40000, account.getBalance());
	}
}
