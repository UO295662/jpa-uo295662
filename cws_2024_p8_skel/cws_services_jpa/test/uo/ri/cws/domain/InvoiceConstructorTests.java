package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceConstructorTests {

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
	 * Computation of the right amount of the invoice with one work order
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add(workOrder);
		Invoice invoice = new Invoice(0L, workOrders); // 0L as dummy invoice number

		assertEquals(302.5, invoice.getAmount(), 0.001);
	}

	/**
	 * A new invoice with work orders is in NOT_YET_PAID state
	 */
	@Test
	void testNewInvoiceIsNotYetPaidState() {
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add(workOrder);
		Invoice invoice = new Invoice(0L, workOrders);

		assertTrue( invoice.isNotSettled() );
	}

	/**
	 * If the date of the invoice is before 1/7/2012 the VAT (IVA) is 18% and
	 * it amounts 250€ + VAT 18%
	 */
	@Test
	void testAmountForInvoicesPriorJuly2012() {
		LocalDate JUNE_6_2012 = LocalDate.of(2012, 6, 15);

		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add(workOrder);
		Invoice invoice = new Invoice(0L, JUNE_6_2012, workOrders); // vat 18%

		assertEquals(295.0, invoice.getAmount(), 0.001);
	}

	/**
	 * A work order, when added to an invoice, changes its state to INVOICED
	 * Added through the constructor
	 */
	@Test
	void testInvoicedWorkOrthersStateInvoiced() {
		List<WorkOrder> workOrders = Arrays.asList(workOrder);
		new Invoice(0L, workOrders);

		assertTrue(workOrder.isInvoiced());
	}

}