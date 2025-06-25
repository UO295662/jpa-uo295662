package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterventionTests {

	private Mechanic mechanic;
	private WorkOrder workOrder;
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
	}

	/**
	 * An intervention without no time nor substitutions amount 0.0 €
	 */
	@Test
	void testAmountsZero() {
		Intervention i = new Intervention(mechanic, workOrder, 0);

		assertEquals( 0.0, i.getAmount() );
	}

	/**
	 * An intervention with 60 minutes amounts the price of an labor hour
	 */
	@Test
	void testAmountOneHour() {
		Intervention i = new Intervention(mechanic, workOrder, 60);

		assertEquals( vehicleType.getPricePerHour(), i.getAmount(), 0.001 );
	}

	/**
	 * An intervention with just one spare part amounts the price of it
	 */
	@Test
	void testImporteRepuesto() {
		Intervention i = new Intervention(mechanic, workOrder, 0);
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 1);

		assertEquals( r.getPrice(), i.getAmount(), 0.001 );
	}

	/**
	 * An intervention with time and spare parts returns the right amount
	 */
	@Test
	void testImporteIntervencionCompleta() {
		Intervention i = new Intervention(mechanic, workOrder, 60);

		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 2);

		final double TOTAL =
					   50.0  // 60 mins * 50 €/hour for the vehicle type
				+ 2 * 100.0; // 2 spare parts sold at 100.0 €

		assertEquals( TOTAL, i.getAmount(), 0.001 );
	}

	/**
	 * The date of an intervention must be stored truncated to milliseconds
	 */
	@Test
	void testTimestampStoredTruncatedToMillisDefault() {
		LocalDateTime now = LocalDateTime.now();
		Intervention i = new Intervention(mechanic, workOrder, 60);
		LocalDateTime expected = now.truncatedTo( ChronoUnit.MILLIS );

		LocalDateTime date = i.getDate().truncatedTo( ChronoUnit.MILLIS );
		
		assertEquals( expected, date );
	}	
}