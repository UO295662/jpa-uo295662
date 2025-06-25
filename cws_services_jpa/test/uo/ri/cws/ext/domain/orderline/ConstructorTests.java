package uo.ri.cws.ext.domain.orderline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.OrderLine;
import uo.ri.cws.domain.SparePart;

class ConstructorTests {

	private static final int MAX_STOCK = 10;
	private static final int STOCK = 4;
	private static final int QUANTITY = MAX_STOCK - STOCK;
	private static final double SUPPLY_PRICE = 20.0;

	private SparePart sparePart;

	@BeforeEach
	void setUp() throws Exception {
		sparePart = new SparePart("SP1", "SP1 desc", 10.0, STOCK, 5, MAX_STOCK);
	}

	/**
	 * new OrderLine for a spare part not under stock throws
	 * IllegalArgumentException
	 */
	@Test
	void testForOverStock() {
		sparePart.setStock(sparePart.getMaxStock());

		assertThrows(IllegalArgumentException.class, () -> new OrderLine(sparePart, SUPPLY_PRICE));
	}

	/**
	 * new OrderLine for a spare part under stock has the price and quantity
	 * computed after the supply spare part current stock and max stock
	 */
	@Test
	void testForUnderStock() {
		OrderLine line = new OrderLine(sparePart, SUPPLY_PRICE);

		assertEquals(SUPPLY_PRICE, line.getPrice(), 0.001);
		assertEquals(QUANTITY, line.getQuantity());
	}

}