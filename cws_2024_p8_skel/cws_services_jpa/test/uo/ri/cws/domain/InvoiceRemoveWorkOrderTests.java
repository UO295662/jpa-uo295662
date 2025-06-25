package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceRemoveWorkOrderTests {

	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		client = new Client("nif-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		Associations.Own.link(client, vehicle);

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
		workOrder.assignTo(mechanic);

		intervention = new Intervention(mechanic, workOrder, 60);

		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(sparePart, intervention, 2);

		workOrder.markAsFinished();
		
		invoice = new Invoice(0L, LocalDate.now());
		invoice.addWorkOrder(workOrder);
	}

	/**
	 * Removing a work order from an invoice unlinks
	 */
	@Test
	void testUnlinkOnInvoice() {
		invoice.removeWorkOrder(workOrder);
		
		assertFalse( invoice.getWorkOrders().contains( workOrder ));
		assertTrue( invoice.getWorkOrders().isEmpty() );
		assertNull( workOrder.getInvoice());
	}
	
	/**
	 * Removing a work order from an invoice computes the new amount
	 */
	@Test
	void testRecomputesOnRemove() {
		invoice.removeWorkOrder(workOrder);
		
		assertEquals( 0.0, invoice.getAmount() );
		
		assertTrue( workOrder.isFinished() );
	}

	/**
	 * Removing a work order from an invoice puts backs the work order to FINISHED
	 */
	@Test
	void testBackToFinishedOnRemove() {
		invoice.removeWorkOrder(workOrder);
		
		assertTrue( workOrder.isFinished() );
	}

}