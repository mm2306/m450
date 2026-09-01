package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für das Promo-Jugend-Sparkonto.
 *
 * @author XXXX
 * @version 1.0
 */
public class PromoYouthSavingsAccountTests
{
	@Test
	public void testInitialization()
	{
		PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");
		assertEquals("Y-1000", account.getId());
		assertEquals(0, account.getBalance());
	}

	@Test
	public void testDepositWithBonus()
	{
		PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");
		// Deposit 10000 -> 1% bonus (100) added -> 10100
		assertTrue(account.deposit(1, 10000));
		assertEquals(10100, account.getBalance());

		// Deposit 5000 -> 1% bonus (50) added -> 10100 + 5050 = 15150
		assertTrue(account.deposit(2, 5000));
		assertEquals(15150, account.getBalance());
	}

	@Test
	public void testWithdrawal()
	{
		PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");
		account.deposit(1, 10000); // balance = 10100

		// Withdraw 5000 -> balance becomes 5100
		assertTrue(account.withdraw(2, 5000));
		assertEquals(5100, account.getBalance());

		// Withdraw 6000 (exceeds balance 5100) -> false
		assertFalse(account.withdraw(3, 6000));
		assertEquals(5100, account.getBalance());
	}
}
