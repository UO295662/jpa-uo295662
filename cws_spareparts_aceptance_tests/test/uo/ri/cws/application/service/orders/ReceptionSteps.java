package uo.ri.cws.application.service.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.OrdersService;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.OrderUtil;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.util.exception.BusinessException;

public class ReceptionSteps {

	private TestContext ctx;
	private OrderDto order;

	private OrdersService service = Factories.service.forOrdersService();
	
	public ReceptionSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("an order in state RECEIVED")
	public void anOrderInStateReceived() throws BusinessException {
		new ProviderUtil().withNif("P1-nif").registerIfNew();
		new SparePartUtil().withCode("SP1").registerIfNew();
		order = new OrderUtil()
					.withRandomCode()
					.forProviderNif("P1-nif")
					.addOrderLine("SP1", 1, 1.0)
					.received()  // <--- RECEIVED
					.register()
					.get();
	}

	@Given("a spare with code {string}, current stock {int} and price {double}")
	public void aSpareWithCodeCurrentStockOfAndPrice(String code, int stock,
			double price) throws BusinessException {

		new SparePartUtil()
				.withCode(code)
				.withStock(stock)
				.withPrice(price)
				.register();
	}

	@Given("an order with a line for spare code {string}, "
			+ "quantity {int} and supply price {double}")
	public void anOrderWithLine(String spareCode, int quantity, double price)
			throws BusinessException {

		new ProviderUtil().withNif("P1-nif").registerIfNew();
		new SparePartUtil().withCode( spareCode ).registerIfNew();
		order = new OrderUtil()
				.withRandomCode()
				.forProviderNif("P1-nif")
				.addOrderLine( spareCode, quantity, price)
				.register()
				.get();
	}

	@When("I receive the order")
	public void iReceiveTheOrder() throws BusinessException {
		OrdersService service = Factories.service.forOrdersService();
		order = service.receive( order.code );
	}

	@When("I try to receive an non existent order")
	public void iTryToReceiveNonExistentOrder() {
		String code = "does-not-exist-order-code";
		ctx.tryAndKeepException(() -> service.receive( code ));
	}

	@When("I try to receive the order")
	public void iTryToReceiveTheOrder() {
		ctx.tryAndKeepException(() -> service.receive( order.code ));
	}

	@When("I try to receive an order with null code")
	public void iTryToReceiveNullOrder() {
		ctx.tryAndKeepException(() -> service.receive( null ));
	}

	@Then("the new price for the spare part code {string} is {double}")
	public void theNewPriceForTheSparePartCodeCodeIs(String spareCode, double newPrice) throws BusinessException {
		SparePartDto spare = new SparePartUtil().loadByCode( spareCode ).get();

		assertEquals( newPrice, spare.price, 0.01 );
	}

	@Then("the new stock for the spare part code {string} is {int}")
	public void theNewStockForTheSparePartIs(String spareCode, int newStock) throws BusinessException {
		SparePartDto spare = new SparePartUtil().loadByCode( spareCode ).get();

		assertEquals( newStock, spare.stock );
	}

	@Then("the state of the order is RECEIVED")
	public void theStateOfTheOrderIsReceived() throws BusinessException {
//		OrderDto registered = new SqlOrderUtil().loadByCode( order.code ).get();
		assertEquals("RECEIVED", order.state);
	}

	@Then("the reception date of the order is today")
	public void theReceptionOfOrderDateIsToday() throws BusinessException {
//		OrderDto registered = new SqlOrderUtil().loadByCode( order.code ).get();
		assertEquals( LocalDate.now(), order.receptionDate);
	}

}
