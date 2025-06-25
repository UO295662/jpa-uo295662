package uo.ri.cws.ext.domain.orderline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.OrderLine;
import uo.ri.cws.domain.SparePart;

class ReceiveTests {

	private static final double PRICE = 10.0;
	private static final int STOCK = 4;
	private static final int MAX_STOCK = 10;

	private static final double PURCHASE_PRICE = 15.0;

	private static final double NEW_PRICE = (STOCK * PRICE + 1.2 * PURCHASE_PRICE * (MAX_STOCK - STOCK)) / MAX_STOCK;

	private SparePart sp1;

	@BeforeEach
	void setUp() throws Exception {
		sp1 = new SparePart("sp1", "desc", PRICE, STOCK, 5, MAX_STOCK);
	}

	/**
	 * orderLine.receive() makes spare parts to be updated for stock and price
	 */
	@Test
	void test() {
		OrderLine line = new OrderLine(sp1, PURCHASE_PRICE);

		line.receive();

		assertEquals(MAX_STOCK, sp1.getStock());
		assertEquals(NEW_PRICE, sp1.getPrice(), 0.001);
	}

}