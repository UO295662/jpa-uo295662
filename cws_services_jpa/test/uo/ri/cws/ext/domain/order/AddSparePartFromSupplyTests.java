package uo.ri.cws.ext.domain.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

class AddSparePartFromSupplyTests {

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

		sp1.setMinStock(5);
		sp1.setMaxStock(10);
	}

	/**
	 * A null supply raises exception
	 */
	@Test
	void testNullSupply() {
		assertThrows(IllegalArgumentException.class, () -> {
			order.addSparePartFromSupply(null);
		});
	}

	/**
	 * Add for a spare with stock equals to min stock
	 */
	@Test
	void testAddOverStockSupply() {
		sp1.setStock(sp1.getMinStock());

		order.addSparePartFromSupply(s);

		assertTrue(order.getOrderLines().isEmpty());
	}

	/**
	 * Add for a spare under stock
	 */
	@Test
	void testAddUnderStockSupply() {
		sp1.setStock(sp1.getMinStock() - 1);

		order.addSparePartFromSupply(s);

		assertEquals(1, order.getOrderLines().size());
		OrderLine ol = order.getOrderLines().iterator().next();

		assertEquals(sp1, ol.getSparePart());
		assertEquals(6, ol.getQuantity());
		assertEquals(10.0, ol.getPrice(), 0.001);
		assertEquals(60.0, ol.getAmount(), 0.001);
		assertEquals(60.0, order.getAmount(), 0.001);
	}

	/**
	 * Add for a spare that is already added (same supply) to the order throws
	 * exception
	 */
	@Test
	void testAddSpareFromRepeatSupply() {
		sp1.setStock(4);
		order.addSparePartFromSupply(s);

		assertThrows(IllegalStateException.class, () -> {
			order.addSparePartFromSupply(s);
		});
	}

	/**
	 * Add for a spare from other provider that is already added to the order
	 */
	@Test
	void testAddRepeatedSpareFromDifferentSupply() {
		Provider p2 = new Provider("P2");
		Supply s2 = new Supply(p2, sp1, 100, 4);
		sp1.setStock(4);

		assertDoesNotThrow(() -> {
			order.addSparePartFromSupply(s); // not here...
		});

		assertThrows(IllegalStateException.class, () -> {
			order.addSparePartFromSupply(s2); // ...but here
		});
	}

}