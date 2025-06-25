package uo.ri.cws.application.service.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.OrdersService;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.Assertions;
import uo.ri.cws.application.service.util.OrderUtil;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.util.exception.BusinessException;

public class FindBySteps {
	
	private TestContext ctx;

	private OrdersService service = Factories.service.forOrdersService();
	private Optional<OrderDto> registered;
	private Map<String, OrderDto> expectedOrders = new HashMap<>();

	private List<OrderDto> ordersOfProvider; 

	public FindBySteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	@Given("the following relation of orders")
	public void theFollowingRelationOfOrders(List<Map<String, String>> data) {
		for(Map<String, String> row : data) {
			String orderCode = row.get("code");
			String providerNif = row.get("provider");
			int nLines = Integer.parseInt(row.get("order lines"));
			double amount = Double.parseDouble(row.get("amount"));

			OrderDto dto = registerOrder(orderCode, providerNif, nLines,
					amount);
			
			expectedOrders.put(orderCode, dto);
		}
	}

	private OrderDto registerOrder(String orderCode, String providerNif,
			int nLines, double amount) {
		ProviderDto provider = new ProviderUtil()
				.withNif(providerNif)
				.registerIfNew()
				.get();

		OrderUtil util = new OrderUtil()
				.withCode(orderCode)
				.forProviderNif(provider.nif)
				.withAmount(amount);
		
		for (int i = 0; i < nLines; i++) {
			SparePartDto sparePart = new SparePartUtil().register().get();
			util.addOrderLine(sparePart.code, 1, amount/nLines);
		}
		
		OrderDto dto = util.register().get();
		return dto;
	}

	@When("I look details of the order with {string}")
	public void iLookDetailsOfTheOrderWith(String code) throws BusinessException {
		registered = service.findByCode(code);
	}

	@Then("I get all the details for the order {string}")
	public void iGetAllTheDetailsForTheOrder(String code) {
		OrderDto expected = expectedOrders.get( code );
		OrderDto dto = registered.get();
		
		Assertions.sameOrder(expected, dto);
	}


	// Scenario: Find a non existing order by code
	@When("I find a non existing order with {string}")
	public void iFindANonExistingOrderWithCode(String code) throws BusinessException {
		ctx.setUniqueResult( service.findByCode(code) );
	}

	@When("I try to find a null code")
	public void iTryToFindANullCode() {
		ctx.tryAndKeepException(() -> service.findByCode(null) );
	}

	@When("I look for orders of {string}")
	public void iLookForOrdersOf(String providerNif) throws BusinessException {
		ordersOfProvider = service.findByProviderNif(providerNif);
	}

	@Then("I get a list of {int} orders with {string}")
	public void iGetAListOfOrdersWith(int quantity, String codes) {
		assertEquals(quantity, ordersOfProvider.size() );
		for (OrderDto dto : ordersOfProvider) {
			assertTrue( codes.contains( dto.code ) );
		}
	}

	@When("I find orders for a non existing provider with {string}")
	public void iFindOrdersForANonExistingProviderWith(String nif) throws BusinessException {
		ctx.setResultList( service.findByProviderNif( nif ) );
	}

	@When("I try to find orders for a null provider nif")
	public void iTryToFindOrdersForANullProviderNif() {
		ctx.tryAndKeepException(() -> service.findByProviderNif(null) );
	}
}
