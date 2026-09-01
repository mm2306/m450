package ch.schule.bank.junit5;

import ch.schule.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests für die Klasse Booking.
 *
 * @author Luigi Cavuoti
 * @version 1.1
 */
public class BookingTests
{
	/**
	 * Tests für die Erzeugung von Buchungen.
	 */
	@Test
	public void testInitialization()
	{
		int date = 100;
		long amount = 50000;
		Booking booking = new Booking(date, amount);

		assertEquals(date, booking.getDate());
		assertEquals(amount, booking.getAmount());

		Booking negativeBooking = new Booking(10, -25000);
		assertEquals(10, negativeBooking.getDate());
		assertEquals(-25000, negativeBooking.getAmount());
	}

	/**
	 * Experimente mit print().
	 */
	@Test
	public void testPrint()
	{
		Booking booking = new Booking(100, 50000);
		booking.print(10000);
		booking.print(0);
	}
}
