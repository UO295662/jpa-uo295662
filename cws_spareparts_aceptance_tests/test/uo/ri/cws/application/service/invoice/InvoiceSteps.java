package uo.ri.cws.application.service.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.Assertions;
import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.InvoiceUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.VehicleTypeUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.math.Round;

public class InvoiceSteps {

	private TestContext ctx;

	private InvoicingService service = Factories.service.forCreateInvoiceService();

	// context fields to keep data between steps
	private ClientDto client;
	private VehicleDto vehicle;
	private InvoiceDto invoice;
	private MechanicDto mechanic;
	private List<String> workordersIds = new ArrayList<>();
	private List<WorkOrderDto> finishedWorkOrders = new ArrayList<>();
	private List<InvoicingWorkOrderDto> found = new ArrayList<>();

	public InvoiceSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a client registered")
	public void aClientRegistered() {
		client = new ClientUtil().register().get();
	}
	
	@Given("a vehicle")
	public void aVehicle() {
		VehicleTypeDto vType = new VehicleTypeUtil().register().get();
		vehicle = new VehicleUtil()
				.forClient( client.id )
				.forVehicleType(vType.id)
				.register()
				.get();
	}

	@Given("one finished workorder")
	public void aListOfOneFinishedWorkorderId() {
		WorkOrderDto w = createWorkOrderWithState("FINISHED");
		workordersIds.add(w.id);
		finishedWorkOrders.add(w);
	}

	private WorkOrderDto createWorkOrderWithState(String state) {
		delay(); // to avoid same timestamp
		return new WorkOrderUtil()
				.forVehicle( vehicle.id )
				.forMechanic( mechanic != null ? mechanic.id : null)
				.withState(state)
				.withAmount( randomAmount(1.0, 100.0) )
				.register()
				.get();
	}

	@Given("a list of several finished workorder ids")
	public void aListOfSeveralFinishedWorkorderIds() {
		int min = 2;
		int max = 5;
		int num = new Random().nextInt(max - min + 1) + min;

		for (int x = 0; x < num; x++) {
			WorkOrderDto w = createWorkOrderWithState("FINISHED");
			workordersIds.add(w.id);
			finishedWorkOrders.add(w);
		}
	}

	private void delay() {
		try {
			Thread.sleep(4);
		} catch (InterruptedException e) {
			// ignored on purpose
		}
	}

	@Given("one non existent workorder")
	public void oneNonExistentWorkorder() {
		workordersIds.add("non-existing-workorderID");
	}

	@Given("one ASSIGNED workorder")
	public void oneASSIGNEDWorkorder() {
		mechanic = new MechanicUtil().register().get();
		WorkOrderDto w = createWorkOrderWithState("ASSIGNED");
		workordersIds.add(w.id);
	}

	@Given("one OPEN workorder")
	public void oneOPENWorkorder() {
		WorkOrderDto w = createWorkOrderWithState("OPEN");
		workordersIds.add(w.id);
	}

	@Given("one INVOICED workorder")
	public void oneINVOICEDWorkorder() {
		WorkOrderDto w = createWorkOrderWithState("INVOICED");
		workordersIds.add(w.id);
	}

	@Given("a null id")
	public void aNullId() {
		workordersIds.add(null);
	}

	@When("I try to create an invoice")
	public void iTryToCreateAnInvoice() {
		ctx.tryAndKeepException(() -> service.createInvoiceFor(workordersIds));
	}

	@When("I try to create an invoice for a null list")
	public void iTryToCreateAnInvoiceForANullList() {
		workordersIds = null;
		ctx.tryAndKeepException(() -> service.createInvoiceFor(workordersIds));
	}

	@When("I try to create an invoice for an empty list")
	public void iTryToCreateAnInvoiceForAnEmptyList() {
		workordersIds.clear();
		ctx.tryAndKeepException(() -> service.createInvoiceFor(workordersIds));
	}

	@When("I try to create an invoice for a list and one of the strings is empty")
	public void iTryToCreateAnInvoiceForAListAndOneOfTheStringsIsEmpty() {
		int min = 2;
		int max = 5;
		int num = new Random().nextInt(max - min + 1) + min;

		for (int x = 0; x < num; x++) {
			createWorkOrderWithState("FINISHED");
		}
		workordersIds.add("");
	}

	@When("I search workorders for a non existent nif")
	public void iTryToFindWorkordersForANonExistentNif()
			throws BusinessException {
		found = service.findNotInvoicedWorkOrdersByClientNif(
				"non-existent-client-nif");
	}

	@When("I try to find workorders with null nif")
	public void iTryToFindWorkordersWithNullNif() {
		ctx.tryAndKeepException(
				() -> service.findNotInvoicedWorkOrdersByClientNif(null)
		);
	}

	@When("I look for a workorder by empty nif")
	public void iLookForAWorkorderByEmptyNif() throws BusinessException {
		found = service.findNotInvoicedWorkOrdersByClientNif(" ");
	}

	private double randomAmount(double min, double max) {
		return min + new Random().nextDouble() * (max - min);
	}

	@When("I create an invoice for the workorders")
	public void iCreateAnInvoiceForTheWorkorders() throws BusinessException {
		invoice = service.createInvoiceFor(workordersIds);
	}

	@When("I search not invoiced workorders by client nif")
	public void iSearchNotInvoicedWorkordersByNif() throws BusinessException {
		found = service.findNotInvoicedWorkOrdersByClientNif( client.nif );
	}

	@Then("I get only finished workorders")
	public void iGetOnlyFinishedWorkorders() {
		List<InvoicingWorkOrderDto> expected = 
				toWorkOrderForInvoicingDtoList(finishedWorkOrders);

		Assertions.sameWorkOrders(expected, found);
	}

	private List<InvoicingWorkOrderDto> toWorkOrderForInvoicingDtoList(
			List<WorkOrderDto> found) {
		return found.stream()
				.map(w -> toWorkOrderDto(w))
				.collect(Collectors.toList());
	}

	private InvoicingWorkOrderDto toWorkOrderDto(WorkOrderDto w) {
		InvoicingWorkOrderDto res = new InvoicingWorkOrderDto();
		res.id = w.id;

		res.description = w.description;
		res.date = w.date;
		res.state = w.state;
		res.amount = w.amount;
 
		return res;
	}

	@Then("I get an empty list")
	public void iGetAnEmptyList() {
		assertTrue( found.isEmpty() );
	}

	@Then("an invoice is created")
	public void anInvoiceIsCreated() {
		InvoiceDto expectedInvoice = computeInvoiceFor(finishedWorkOrders);

		/* invoice is created */
		InvoiceDto found = new InvoiceUtil().loadById(invoice.id).get();
		assertNotNull(found);
		assertEquals(expectedInvoice.amount, found.amount, 0.01);
		assertEquals(expectedInvoice.vat, found.vat, 0.01);
		assertEquals(expectedInvoice.state, found.state);

		/* workorders are updated to the proper state */
		List<WorkOrderDto> workOrders = WorkOrderUtil.loadByIds(workordersIds);
		assertTrue(workOrders.stream()
				.allMatch(wo -> wo.state.equals("INVOICED")));
		assertTrue(workOrders.stream()
				.allMatch(wo -> wo.invoiceId.equals(this.invoice.id)));
	}

	private InvoiceDto computeInvoiceFor(List<WorkOrderDto> workorders) {
		double total = calculateTotal(workorders);

		InvoiceDto result = new InvoiceDto();
		result.amount = Round.twoCents(total);
		result.vat = Round.twoCents( total * 0.21 / 1.21 );
		result.state = "NOT_YET_PAID";
		return result;
	}

	private double calculateTotal(List<WorkOrderDto> lst) {
		double VAT = 0.21; // %
		double total = lst.stream()
				.mapToDouble(wo -> wo.amount)
				.sum();
		total *= (1 + VAT); // vat included
		return total;
	}

}
