package uo.ri.cws.ext.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;

class GetAmountTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * amount is 0 for an empty order
	 */
	@Test
	void testAmountIsZero() {
		Order order = new Order("order");

		assertEquals(0, order.getAmount(), 0.0001);
	}

	/**
	 * amount equals amount of order line for a single line
	 */
	@Test
	void testAmountEqualsOrderLine() {
		double SUPPLY_PRICE = 15.0;
		int MAX_STOCK = 10;
		int STOCK = 4;
		Provider p1 = new Provider("P1");
		SparePart sp1 = new SparePart("SP1", "Desc", 10.0, STOCK, 5, MAX_STOCK);
		Supply s = new Supply(p1, sp1, SUPPLY_PRICE, sp1.getQuantityToOrder());
		Order order = new Order("order");
		double expectedAmount = SUPPLY_PRICE * (MAX_STOCK - STOCK);

		order.addSparePartFromSupply(s);

		assertEquals(expectedAmount, order.getAmount(), 0.0001);
	}

	/**
	 * amount equals the addition of all order lines amount
	 */
	@Test
	void testAmountEqualsAdditionOfOrderLines() {
		double SUPPLY_PRICE_1 = 15.0;
		double SUPPLY_PRICE_2 = 10.0;
		int MAX_STOCK = 10;
		int STOCK = 4;

		Provider p1 = new Provider("P1");
		SparePart sp1 = new SparePart("SP1", "Desc", 10.0, STOCK, 5, MAX_STOCK);
		SparePart sp2 = new SparePart("SP2", "Desc2", 10.0, STOCK, 5, MAX_STOCK);
		Supply s1 = new Supply(p1, sp1, SUPPLY_PRICE_1, sp1.getQuantityToOrder());
		Supply s2 = new Supply(p1, sp2, SUPPLY_PRICE_2, sp1.getQuantityToOrder());
		Order order = new Order("order");
		double expectedAmount = SUPPLY_PRICE_1 * (MAX_STOCK - STOCK) + SUPPLY_PRICE_2 * (MAX_STOCK - STOCK);

		order.addSparePartFromSupply(s1);
		order.addSparePartFromSupply(s2);

		assertEquals(expectedAmount, order.getAmount(), 0.0001);
	}

}