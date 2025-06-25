package uo.ri.cws.ext.domain.order;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;

class StateTests {

	private Order order;
	private Provider p1;
	private SparePart sp1;
	private Supply s;

	@BeforeEach
	void setUp() throws Exception {
		p1 = new Provider("P1");
		sp1 = new SparePart("sp1");
		s = new Supply(p1, sp1, 10.0, 5);
		order = new Order("o1");
		Associations.Deliver.link(p1, order);

		sp1.setMinStock(5);
		sp1.setMaxStock(10);
	}

	/**
	 * Default state is PENDING
	 */
	@Test
	void testConstructor() {
		assertTrue(order.isPending());
		assertFalse(order.isReceived());
	}

	/**
	 * Still pending after adding lines
	 */
	@Test
	void testPendingAfterAdding() {
		order.addSparePartFromSupply(s);

		assertTrue(order.isPending());
		assertFalse(order.isReceived());
	}

	/**
	 * Changes to RECEIVED after reception
	 */
	@Test
	void testReceivedAfterReception() {
		order.addSparePartFromSupply(s);

		order.receive();

		assertFalse(order.isPending());
		assertTrue(order.isReceived());
	}
}