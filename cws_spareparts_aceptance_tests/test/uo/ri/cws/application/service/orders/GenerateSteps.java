package uo.ri.cws.application.service.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.spare.OrdersService;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto;
import uo.ri.cws.application.service.spare.OrdersService.OrderDto.OrderLineDto;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.cws.application.service.util.SupplyUtil;
import uo.ri.util.exception.BusinessException;

public class GenerateSteps {

	private List<OrderDto> generatedOrders;
	private OrdersService service = Factories.service.forOrdersService();

	@Given("a relation of spare parts, providers and supplies")
	public void aRelationOfSparePartsProvidersAndSupplies(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row : table) {
			String sparePartCode = row.get("sparePart");
			String providerNif = row.get("provider");
			double price = Double.parseDouble( row.get("price") );

			new SparePartUtil().withCode( sparePartCode ).registerIfNew();
			new ProviderUtil().withNif( providerNif ).registerIfNew();
			new SupplyUtil()
					.forProviderNif( providerNif )
					.forSparePartCode( sparePartCode )
					.withPrice( price )
					.register();
		}
	}

	@Given("a stock of spare parts")
	public void aStockOfSpareParts(DataTable data) throws BusinessException {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row : table) {
			String sparePartCode = row.get("sparePart");
			int stock = Integer.parseInt( row.get("stock") );
			int minStock = Integer.parseInt( row.get("min") );
			int maxStock = Integer.parseInt( row.get("max") );

			new SparePartUtil()
					.loadByCode( sparePartCode )
					.withStock( stock )
					.withMinStock( minStock )
					.withMaxStock( maxStock )
					.update();
		}
	}

	@Given("an order is already generated")
	public void anOrderIsAlreadyGenerated() throws BusinessException {
		service.generateOrders();
	}

	@When("new orders are generated")
	public void newOrdersAreGenerated() throws BusinessException {
		generatedOrders = service.generateOrders();
	}

	@Then("we get no new orders")
	public void weGetNoNewOrders() {
		assertTrue( generatedOrders.isEmpty() );
	}

	@Then("we get the following orders")
	public void weGetTheFollowingOrders(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row : table) {
			String providerNif = row.get("provider");
			double amount = Double.parseDouble( row.get("amount") );

			OrderDto dto = findOrderByProviderNif( providerNif );

			assertEquals( amount, dto.amount, 0.001);
		}
	}

	@Then("we get the following order lines")
	public void theFollowingOrderLines(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row : table) {
			String providerNif = row.get("provider");
			String sparePartCode = row.get("sparePart");
			double price = Double.parseDouble( row.get("price") );
			int quantity = Integer.parseInt( row.get("quantity") );

			OrderDto order = findOrderByProviderNif( providerNif );
			OrderLineDto line = findOrderLineBySpareCode(order, sparePartCode);

			assertEquals( price, line.price, 0.001);
			assertEquals( quantity, line.quantity);
		}
	}

	private OrderDto findOrderByProviderNif(String nif) {
		return generatedOrders.stream()
				.filter( o -> nif.equals( o.provider.nif) )
				.findFirst()
				.get();
	}

	private OrderLineDto findOrderLineBySpareCode(OrderDto order, String code) {
		return order.lines.stream()
				.filter( ol -> ol.sparePart.code.equals( code ))
				.findFirst()
				.get();
	}

}
