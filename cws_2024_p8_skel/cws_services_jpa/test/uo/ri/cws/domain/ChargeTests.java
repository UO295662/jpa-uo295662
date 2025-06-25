package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChargeTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * A charge to a card increases the accumulated
	 */
	@Test
	void testCardCharge() {
		LocalDate tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS);
		CreditCard t = new CreditCard("123", "visa", tomorrow);
		Invoice f = new Invoice(123L);

		new Charge(f, t, 100.0);

		assertEquals(100.0, t.getAccumulated());
	}

	/**
	 * A charge to cash increases the accumulated
	 */
	@Test
	void testCashCharge() {
		Cash m = new Cash(new Client("123", "n", "a"));
		Invoice f = new Invoice(123L);

		new Charge(f, m, 100.0);

		assertEquals(100.0, m.getAccumulated());
	}

	/**
	 * A charge to a voucher increases the accumulated and decreases the
	 * available
	 */
	@Test
	void testVoucherCharge() {
		Voucher b = new Voucher("123", "For testing", 150.0);
		Invoice f = new Invoice(123L);

		new Charge(f, b, 100.0);

		assertEquals(100.0, b.getAccumulated());
		assertEquals(50.0, b.getAvailable());
	}

	/**
	 * A credit card cannot be charged if its expiration date is over
	 * @throws IllegalStateException
	 */
	@Test
	void tesChargeWithExpiredCard() {
		LocalDate expired = LocalDate.now().minus(1, ChronoUnit.DAYS);
		CreditCard t = new CreditCard("123", "TTT", expired);
		Invoice f = new Invoice(123L);

		assertThrows(IllegalStateException.class, () -> 
			new Charge(f, t, 100.0) // Card validity date expired exception
		);
	}

	/**
	 * A voucher cannot be charged if it has no available money
	 * @throws IllegalStateException
	 */
	@Test
	void testEmptyVoucherCannotBeCharged() {
		Voucher b = new Voucher("123", "For testing", 150.0);
		Invoice f = new Invoice(123L);

		assertThrows(IllegalStateException.class, () -> 
			new Charge(f, b, 151.0)  // Not enough money exception
		);
	}
}