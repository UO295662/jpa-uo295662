package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 */
class WorkOrderTests {

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

		vehicleType = new VehicleType("coche", 50.0 /* � /hour */);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("nif-mecanico", "nombre", "apellidos");
		workOrder.assignTo(mechanic);

		intervention = new Intervention(mechanic, workOrder, 60);

		sparePart = new SparePart("R1001", "junta la trocla", 100.0 /* � */);
		new Substitution(sparePart, intervention, 2);

		workOrder.markAsFinished(); // changes state & computes price
	}

	/**
	 * The amount of the work order is 250.0 �
	 */
	@Test
	void testBasicWorkOrderAmount() {
		assertEquals(250.0, workOrder.getAmount(), 0.001);
	}

	/**
	 * With two interventions the amount is computed correctly
	 */
	@Test
	void testComputeAmountForTwoInterventions() {
		workOrder.reopen();  // changes from FINISHED to OPEN again
		Mechanic another = new Mechanic("1", "a", "n");
		workOrder.assignTo(another);
		new Intervention(another, workOrder, 30);

		workOrder.markAsFinished();

		assertEquals(275.0, workOrder.getAmount(), 0.001);
	}

	/**
	 * Removing one intervention the amount is correctly recomputed
	 * The (re)computation is done when the work order passes to FINISHED
	 */
	@Test
	void testRcomputeAmountWhenRemoveingIntervention() {
		workOrder.reopen();
		Mechanic another = new Mechanic("1", "a", "n");
		workOrder.assignTo(another);
		new Intervention(another, workOrder, 30);

		Associations.Intervene.unlink(intervention);
		workOrder.markAsFinished(); // <-- recomputes here

		assertEquals(25.0, workOrder.getAmount(), 0.001);
	}

	/**
	 * An invoice cannot be added to a non FINISHED work order
	 * @throws IllegalStateException
	 */
	@Test
	void testNotFinishedWorkOrderException() {
		workOrder.reopen();
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add(workOrder);
		assertThrows(IllegalStateException.class, 
				() -> new Invoice(0L, workOrders) 
			); 
	}

	/**
	 * A just created invoice is in NOT_YET_PAID state
	 */
	@Test
	void testNotYetPaidForNewInvoice() {
		List<WorkOrder> averias = new ArrayList<>();
		averias.add(workOrder);
		Invoice invoice = new Invoice(0L, averias);

		assertTrue( invoice.isNotSettled() );
	}

	/**
	 * The date of a work order must be stored truncated to milliseconds
	 */
	@Test
	void testTimestampStoredTruncatedToMillisDefault() {
		LocalDateTime now = LocalDateTime.now();
		WorkOrder wo = new WorkOrder(vehicle);
		LocalDateTime expected = now.truncatedTo( ChronoUnit.MILLIS );

		LocalDateTime date = wo.getDate().truncatedTo( ChronoUnit.MILLIS );
		
		assertEquals( expected, date );
	}	
	
	/**
	 * The date of a work order must be stored truncated to milliseconds also 
	 * for constructors receiving the date
	 */
	@Test
	void testTimestampStoredTruncatedToMillisReceiving2() {
		LocalDateTime now = LocalDateTime.now();
		WorkOrder wo = new WorkOrder(vehicle, now);
		LocalDateTime expected = now.truncatedTo(ChronoUnit.MILLIS);

		LocalDateTime date = wo.getDate().truncatedTo( ChronoUnit.MILLIS );
		
		assertEquals( expected, date );
	}	
	
	/**
	 * The date of a work order must be stored truncated to milliseconds also 
	 * for constructors receiving the date
	 */
	@Test
	void testTimestampStoredTruncatedToMillisReceiving3() {
		LocalDateTime now = LocalDateTime.now();
		WorkOrder wo = new WorkOrder(vehicle, now, "desc");
		LocalDateTime expected = now.truncatedTo(ChronoUnit.MILLIS);

		LocalDateTime date = wo.getDate().truncatedTo( ChronoUnit.MILLIS );
		
		assertEquals( expected, date );
	}	
	
	/**
	 * A work order cannot be marked as INVOICED without an assigned invoice
	 * @throws IllegalStateException
	 */
	@Test
	void testCannotBeMarkedAsInvoiced() {
		
		assertThrows(IllegalStateException.class, 
			() -> workOrder.markAsInvoiced()  // must throw exception
		);
	}

}