package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubstitutionTests {

	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Own.link(client, vehicle);

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");

		intervention = new Intervention(mechanic, workOrder, 0);
	}

	/**
	 * A substitution with 0 spare parts throws IllegalArgumentException
	 */
	@Test
	void testSustitutionThrowsExceptionIfZero() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		
		assertThrows(IllegalArgumentException.class, 
				() -> new Substitution(r, intervention, 0)
			);
	}

	/**
	 * A substitution with negative spare parts throws IllegalArgumentException
	 */
	@Test
	void testSustitutionThrowsExceptionIfNegative() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		
		assertThrows(IllegalArgumentException.class, 
				() -> new Substitution(r, intervention, -1)
			);
	}

	/**
	 * It computes the right amount of a substitution
	 */
	@Test
	void testSubstitutionAmount() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		Substitution s = new Substitution(r, intervention, 2);

		assertEquals( 200.0, s.getAmount(), 0.001 );
	}

}