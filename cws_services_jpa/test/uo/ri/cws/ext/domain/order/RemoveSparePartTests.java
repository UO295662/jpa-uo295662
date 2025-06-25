package uo.ri.cws.ext.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Order;
import uo.ri.cws.domain.OrderLine;
import uo.ri.cws.domain.Provider;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Supply;

class RemoveSparePartTests {

	private Provider p1;
	private SparePart sp1;
	private Supply s;
	private Order order;

	@BeforeEach
	void setUp() throws Exception {
		p1 = new Provider("P1");
		sp1 = new SparePart("sp1");
		s = new Supply(p1, sp1, 10.0, 5);
		order = new Order("o1");
		Associations.Deliver.link(p1, order);

		order.addSparePartFromSupply(s);
	}

	/**
	 * Remove from a non included spare part
	 */
	@Test
	void testRemoveFromEmpty() {
		SparePart sp2 = new SparePart("sp2");
		int previousSize = order.getOrderLines().size();
		double previousAmount = order.getAmount();

		order.removeSparePart(sp2);

		// no changes
		assertEquals(previousSize, order.getOrderLines().size());
		assertEquals(previousAmount, order.getAmount(), 0.001);
	}

	/**
	 * Remove a null spare part
	 */
	@Test
	void testRemoveNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			order.removeSparePart(null);
		});
	}

	/**
	 * Remove the unique spare part of the order
	 */
	@Test
	void testRemoveUnique() {
		order.removeSparePart(sp1);

		assertTrue(order.getOrderLines().isEmpty());
		assertEquals(0, order.getAmount(), 0.001);
	}

	/**
	 * Remove one spare spare part from an order with two order lines
	 */
	@Test
	void testRemoveOneFromTwo() {
		double SUPPLY_PRICE = 5.0;
		int MAX_STOCK = 10;
		int STOCK = 4;
		SparePart sp2 = new SparePart("sp2", "sp2", 10.0, STOCK, 5, MAX_STOCK);
		Supply s2 = new Supply(p1, sp2, SUPPLY_PRICE, 3);
		order.addSparePartFromSupply(s2);

		order.removeSparePart(sp1);

		OrderLine expectedOrderLine = new OrderLine(sp2, s2.getPrice());
		double expectedAmount = SUPPLY_PRICE * (MAX_STOCK - STOCK);

		assertEquals(1, order.getOrderLines().size());
		assertTrue(order.getOrderLines().contains(expectedOrderLine));
		assertEquals(expectedAmount, order.getAmount(), 0.0001);
	}

}