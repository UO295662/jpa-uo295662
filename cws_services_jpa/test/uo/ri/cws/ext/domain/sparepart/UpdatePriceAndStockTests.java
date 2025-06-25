package uo.ri.cws.ext.domain.sparepart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.SparePart;

class UpdatePriceAndStockTests {

	private SparePart spare;

	@BeforeEach
	void setUp() throws Exception {
		double price = 10.0;
		int stock = 4;
		int minStock = 5;
		int maxStock = 10;

		spare = new SparePart("sp1", "desc", price, stock, minStock, maxStock);
	}

	/**
	 * new price and stock is computed following the specification
	 */
	@Test
	void testSimpleComputation() {
		int newQuantity = 6;
		double purchasePrice = 100.0;
		int expectedStock = spare.getStock() + newQuantity;
		double expectedPrice = (spare.getStock() * spare.getPrice() + newQuantity * purchasePrice * 1.2)
				/ expectedStock;

		spare.updatePriceAndStock(purchasePrice, newQuantity);

		assertEquals(expectedStock, spare.getStock());
		assertEquals(expectedPrice, spare.getPrice(), 0.0001);
	}

	/**
	 * new quantity must be over zero
	 */
	@Test
	void testQuantityMustBeOverZero() {
		int QUANTITY_ZERO = 0;

		assertThrows(IllegalArgumentException.class, () -> {
			spare.updatePriceAndStock(100.0, QUANTITY_ZERO);
		});
	}

	/**
	 * purchase price cannot be negative
	 */
	@Test
	void testPurchasePriceCannotBeNegative() {
		double purchasePrice = -1.0;

		assertThrows(IllegalArgumentException.class, () -> {
			spare.updatePriceAndStock(purchasePrice, 10);
		});
	}

	/**
	 * update quantity even over max stock (is not the responsibility of spare part
	 * to check this)
	 */
	@Test
	void testComputationOverMaxStock() {
		int newQuantity = 100;
		double purchasePrice = 100.0;
		int expectedStock = spare.getStock() + newQuantity;
		double expectedPrice = (spare.getStock() * spare.getPrice() + newQuantity * purchasePrice * 1.2)
				/ expectedStock;

		spare.updatePriceAndStock(purchasePrice, newQuantity);

		assertEquals(expectedStock, spare.getStock());
		assertEquals(expectedPrice, spare.getPrice(), 0.0001);
	}
}