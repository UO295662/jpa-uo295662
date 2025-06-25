package uo.ri.cws.ext.domain.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;

class ReceiveTests {

	private static final int SP1_MAX_STOCK = 10;
	private static final int SP1_STOCK = 4;
	private static final double SP1_PRICE = 10.0;

	private static final int SP2_MAX_STOCK = 15;
	private static final int SP2_STOCK = 1;
	private static final double SP2_PRICE = 20.0;

	private static final double SUPPLY_FOR_SP1_PRICE = 25.0;
	private static final double SUPPLY_FOR_SP2_PRICE = 8.0;

	private static final double NEW_SP1_PRICE = (SP1_PRICE * SP1_STOCK
			+ (SP1_MAX_STOCK - SP1_STOCK) * SUPPLY_FOR_SP1_PRICE * 1.2) / (SP1_MAX_STOCK);

	private static final double NEW_SP2_PRICE = (SP2_PRICE * SP2_STOCK
			+ (SP2_MAX_STOCK - SP2_STOCK) * SUPPLY_FOR_SP2_PRICE * 1.2) / (SP2_MAX_STOCK);

	private Provider p1;
	private SparePart sp1;
	private SparePart sp2;
	private Supply s1;
	private Supply s2;
	private Order order;

	@BeforeEach
	void setUp() throws Exception {
		p1 = new Provider("P1");
		sp1 = new SparePart("sp1", "sp1-desc", SP1_PRICE, SP1_STOCK, 5, SP1_MAX_STOCK);
		sp2 = new SparePart("sp2", "sp2-desc", SP2_PRICE, SP2_STOCK, 5, SP2_MAX_STOCK);
		s1 = new Supply(p1, sp1, SUPPLY_FOR_SP1_PRICE, 5);
		s2 = new Supply(p1, sp2, SUPPLY_FOR_SP2_PRICE, 5);
		order = new Order("o1");
		Associations.Deliver.link(p1, order);
	}

	/**
	 * Receive an empty order
	 */
	@Test
	void testReceiveEmpty() {
		order.receive();

		assertEquals(0, order.getAmount(), 0.001);
		assertTrue(order.isReceived());
	}

	/**
	 * Receive an order with one order line
	 */
	@Test
	void testReceiveOrderWithOneLine() {
		order.addSparePartFromSupply(s1);
		double previousAmount = order.getAmount();

		order.receive();

		assertTrue(order.isReceived());
		assertEquals(LocalDate.now(), order.getReceptionDate());
		assertEquals(previousAmount, order.getAmount(), 0.001);
		assertEquals(SP1_MAX_STOCK, sp1.getStock(), 0.001);
		assertEquals(NEW_SP1_PRICE, sp1.getPrice(), 0.001);
	}

	/**
	 * Receive an order with two order lines
	 */
	@Test
	void testReceiveOrderWithTwoLines() {
		order.addSparePartFromSupply(s1);
		order.addSparePartFromSupply(s2);
		double previousAmount = order.getAmount();

		order.receive();

		assertTrue(order.isReceived());
		assertEquals(LocalDate.now(), order.getReceptionDate());
		assertEquals(previousAmount, order.getAmount(), 0.001);

		assertEquals(SP1_MAX_STOCK, sp1.getStock(), 0.001);
		assertEquals(NEW_SP1_PRICE, sp1.getPrice(), 0.001);

		assertEquals(SP2_MAX_STOCK, sp2.getStock(), 0.001);
		assertEquals(NEW_SP2_PRICE, sp2.getPrice(), 0.001);
	}

	/**
	 * Try to receive an already received order raises an IllegalStateException
	 */
	@Test
	void testReceiveAlreadyReceived() {
		order.addSparePartFromSupply(s1);

		assertDoesNotThrow(() -> {
			order.receive(); // not this
		});

		assertThrows(IllegalStateException.class, () -> {
			order.receive(); // but this one
		});
	}

	/**
	 * order.receive() calls orderline.receive()
	 */
	@Test
	void testReceiveCallOrderLineReceive() {
		order.addSparePartFromSupply(s1);

		order.receive();
	}
}