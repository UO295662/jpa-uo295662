package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceAddWorkOrderTests {

	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;

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
	}

	/**
	 * Computation of the new amount after adding a new work order to the invoice
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
		Invoice invoice = new Invoice(0L); // 0L as dummy invoice number
		invoice.addWorkOrder(workOrder);

		assertEquals(302.5, invoice.getAmount(), 0.001);
	}

	/**
	 * The new work order is linked to the invoice
	 */
	@Test
	void testLinkedAfterAddingWorkOrder() {
		Invoice invoice = new Invoice(0L); // 0L as dummy invoice number
		invoice.addWorkOrder(workOrder);

		assertTrue(invoice.getWorkOrders().contains(workOrder));
		assertEquals(invoice, workOrder.getInvoice());
	}

	/**
	 * Computation for two work orders added by association
	 */
	@Test
	void testInvoiceAmountAddingTwoWorkOrders() {
		Invoice invoice = new Invoice(0L);
		invoice.addWorkOrder(workOrder);
		invoice.addWorkOrder(createAnotherWorkOrder());

		assertEquals(468.88, invoice.getAmount(), 0.001); // 2 cents rounding
	}

	/**
	 * A work order, when added to an invoice, changes its state to INVOICED
	 * Added by association
	 */
	@Test
	void testWorkorderChangedToInvoiced() {
		new Invoice(0L).addWorkOrder(workOrder);

		assertTrue(workOrder.isInvoiced());
	}

	/**
	 * All the work orders changes its state to INVOICED when added to
	 * an invoice
	 */
	@Test
	void testAllWorkOrdersChangedToInvoiced() {
		WorkOrder otherWorkOrther = createAnotherWorkOrder();

		Invoice f = new Invoice(0L);
		f.addWorkOrder(workOrder);
		f.addWorkOrder(otherWorkOrther);

		assertTrue(workOrder.isInvoiced());
		assertTrue(otherWorkOrther.isInvoiced());
	}

	/**
	 * Creates a new invoice with a delay of 100 milliseconds to avoid a
	 * collision in the dates field (same millisecond)
	 *
	 * It could be problematic if the identity of the work order depends on
	 * the date
	 * @return the newly created work order
	 */
	private WorkOrder createAnotherWorkOrder() {
		sleep(100);
		
		WorkOrder workOrder = new WorkOrder(vehicle, "falla la junta la trocla otra vez");
		workOrder.assignTo(mechanic);

		Intervention i = new Intervention(mechanic, workOrder, 45);
		new Substitution(sparePart, i, 1);

		workOrder.markAsFinished();

		// amount = 100 € spare part + 37.5 laboring time
		return workOrder;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			// dont't care if this occurs
		}
	}

}