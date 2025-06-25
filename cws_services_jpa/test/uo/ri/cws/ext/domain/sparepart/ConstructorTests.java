package uo.ri.cws.ext.domain.sparepart;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.SparePart;

class ConstructorTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Spare part code cannot be null
	 */
	@Test
	void testCodeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart(null);
		});
	}

	/**
	 * Spare part code cannot be empty
	 */
	@Test
	void testCodeEmpty() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("");
		});
	}

	/**
	 * Spare part description cannot be null
	 */
	@Test
	void testDescriptionCannotBeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", null, 0, 0, 0, 0);
		});
	}

	/**
	 * Spare part description cannot be empty
	 */
	@Test
	void testDescriptionCannotBeEmpty() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", "", 0, 0, 0, 0);
		});
	}

	/**
	 * Spare part price cannot be negative
	 */
	@Test
	void testPriceCannotBeNgative() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", "description", -1.0, 0, 0, 0);
		});
	}

	/**
	 * Spare part code stock cannot be negative
	 */
	@Test
	void testStockCannotBeNgative() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", "description", 0.0, -1, 0, 0);
		});
	}

	/**
	 * Spare part code minStock cannot be negative
	 */
	@Test
	void testMinStockCannotBeNgative() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", "description", 0.0, 0, -1, 0);
		});
	}

	/**
	 * Spare part code maxStock cannot be negative
	 */
	@Test
	void testMaxStockCannotBeNgative() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SparePart("code", "description", 0.0, 0, 0, -1);
		});
	}
}