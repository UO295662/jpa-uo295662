package uo.ri.cws.application.service.supplies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.spare.SuppliesCrudService;
import uo.ri.cws.application.service.spare.SuppliesCrudService.SupplyDto;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.cws.application.service.util.SupplyUtil;
import uo.ri.util.exception.BusinessException;

public class SupplySteps {

	private TestContext ctx;
	private SuppliesCrudService service = Factories.service.forSuppliesCrudService();

	private SparePartDto spare;
	private ProviderDto provider;
	private SupplyDto supply;
	private List<SupplyDto> supplies;

	public SupplySteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a provider for supplies")
	public void aProviderForSupplies() throws BusinessException {
	    provider = new ProviderUtil().register().get();
	}

	@Given("a registered spare part with no supplies")
	public void aSparePartWithNoSupplies() throws BusinessException {
		spare = new SparePartUtil().register().get();
	}

	@When("I add a supply for that provider and spare part for {double} € and {int} days")
	public void iAddASupplyForThatProviderAndSparePartFor€AndDays(double price, int days)
			throws BusinessException {
		supply = new SupplyUtil()
				.forProviderNif( provider.nif )
				.forSparePartCode( spare.code )
				.withPrice(price)
				.withDeliveryTerm(days)
				.register()
				.get();
	}

	@Given("a supply")
	public void aSupply() throws BusinessException {
	    provider = new ProviderUtil().register().get();
		spare = new SparePartUtil().register().get();
		supply = new SupplyUtil()
				.forProviderNif( provider.nif )
				.forSparePartCode( spare.code )
				.register()
				.get();
	}

	@When("I try to add another supply for that provider and spare part")
	public void iTryToAddASupplyForThatProviderAndSparePart() {
		SupplyDto dto = new SupplyUtil()
							.forProviderNif( provider.nif )
							.forSparePartCode( spare.code )
							.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I add a supply for that provider and spare part of {double} € and {int} days")
	public void iAddASupplyForThatProviderAndSparePartOfPriceAndDays(double price, int days)
			throws BusinessException {

		supply = new SupplyUtil()
				.forProviderNif( provider.nif )
				.forSparePartCode( spare.code )
				.withPrice(price)
				.withDeliveryTerm(days)
				.register()
				.get();
	}

	@When("I try to add a new supply with price {double} and delivery term {int}")
	public void iTryToAddANewSupplyWithData(double price, int days) {

		SupplyDto dto = new SupplyUtil()
				.forProviderNif( provider.nif )
				.forSparePartCode( spare.code )
				.withPrice( price )
				.withDeliveryTerm( days )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to add a null supply")
	public void iTryToAddANullSupply() {
		ctx.tryAndKeepException(() -> service.add( null ));
	}

	@When("I try to add a supply with null spare part")
	public void iTryToAddSupplyWithNullSpare() {
		SupplyDto dto = new SupplyUtil()
				.forProvider( null )
				.forSparePart( null )
				.withPrice( 100 )
				.withDeliveryTerm( 5 )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@Then("the supply exists with price {double} € and delivery in {int} days")
	public void theSupplyResultsAddedToTheSystemForPriceAndDays(double price, int days)
			throws BusinessException {

		SupplyDto loaded = new SupplyUtil()
							.loadByNifAndCode( provider.nif, spare.code )
							.get();

		assertEquals( price, loaded.price, 0.001 );
		assertEquals( days, loaded.deliveryTerm );
	}

	@When("I remove the supply")
	public void iRemoveTheSupply() throws BusinessException {
		service.delete(supply.provider.nif, supply.sparePart.code);
	}

	@Then("the supply gets removed")
	public void theSupplyGetsRemoved() throws BusinessException {
		SupplyDto dto = new SupplyUtil()
							.loadByNifAndCode(
								supply.provider.nif,
								supply.sparePart.code
							)
							.get();

		assertNull( dto );
	}

	@When("I try to remove a non existent supply")
	public void iTryToRemoveANonExistentSupply() {
		ctx.tryAndKeepException(() -> service.delete(
				"does-not-exist-nif",
				"does-not-exist-spare"
			)
		);
	}

	@When("I try to remove a supply with null nif")
	public void iTryToRemoveASupplyWithNullNif() {
		ctx.tryAndKeepException(() -> service.delete(null, "code") );
	}

	@When("I update that supply to {double} € and {int} days")
	public void iUpdateThatSupplyTo€AndDays(double price, int days)
			throws BusinessException {

		SupplyDto dto = new SupplyUtil()
				.loadByNifAndCode(
					supply.provider.nif,
					supply.sparePart.code
				)
				.get();

		dto.deliveryTerm = days;
		dto.price = price;

		service.update(dto);
	}

	@When("I try to update a non exisitng supply")
	public void iTryToUpdateANonExisitngSupply() {
		SupplyDto dto = new SupplyUtil()
					.forProviderId( "does-not-exist" )
					.forSparePartId( "does-not-exist" )
					.get();

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update that supply to {double} and {int}")
	public void iTryToUpdateThatSupplyToAnd(double price, int days)
			throws BusinessException {

		SupplyDto dto = new SupplyUtil()
				.loadByNifAndCode(
					supply.provider.nif,
					supply.sparePart.code
				)
				.get();

		dto.deliveryTerm = days;
		dto.price = price;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update a null supply")
	public void iTryToUpdateNullSupply() throws BusinessException {
		ctx.tryAndKeepException(() -> service.update( null ));
	}

	@When("I try to update that supply with null provider")
	public void iTryToUpdateThatSupplyWithNullProvider()
			throws BusinessException {

		SupplyDto dto = new SupplyUtil()
				.loadByNifAndCode(
					supply.provider.nif,
					supply.sparePart.code
				)
				.get();

		dto.provider = null;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update that supply with null spare")
	public void iTryToUpdateThatSupplyWithNullSpare()
			throws BusinessException {

		SupplyDto dto = new SupplyUtil()
				.loadByNifAndCode(
					supply.provider.nif,
					supply.sparePart.code
				)
				.get();

		dto.sparePart = null;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update the supply with wrong version")
	public void iTryToUpdateThatSupplyWithWrongVersion()
			throws BusinessException {

		SupplyDto dto = new SupplyUtil()
				.loadByNifAndCode(
					supply.provider.nif,
					supply.sparePart.code
				)
				.get();

		dto.deliveryTerm++;
		dto.version -= 1;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@Given("the following relation of supplies")
	public void theFollowingRelationOfSuppliesNifCode(DataTable data)
			throws BusinessException {

		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
		    String nif = row.get("nif");
			String sparePartCode = row.get("code");
			int days = getDaysOrDefault(row, 1);
			double price = getPriceOrDefault(row, 10.0);

			new ProviderUtil()
		    		.unique()
		    		.withNif( nif )
		    		.registerIfNew();

			new SparePartUtil()
					.withCode( sparePartCode )
					.registerIfNew();

			new SupplyUtil()
					.forProviderNif( nif )
					.forSparePartCode( sparePartCode )
					.withDeliveryTerm( days )
					.withPrice( price )
					.register();
		}
	}

	private int getDaysOrDefault(Map<String, String> row, int def) {
		return row.containsKey("days")
				? Integer.parseInt( row.get("days") )
				: def;
	}

	private double getPriceOrDefault(Map<String, String> row, double def) {
		return row.containsKey("price")
				? Double.parseDouble( row.get("price") )
				: def;
	}

	@When("I look for a supply by nif {string}")
	public void iLookForASupplyByNif(String nif) throws BusinessException {
		supplies = service.findByProviderNif(nif);
	}

	@Then("I get a list of supplies for provider nif {string} and spare codes {string}")
	public void iGetAListOfSuppliesForProviderNifAndSpareCodes(String nif, String codes) {
		assertTrue( supplies.stream()
						.allMatch(s -> nif.equals( s.provider.nif) )
			);

		assertTrue( supplies.stream()
				.allMatch(s -> codes.contains( s.sparePart.code ) )
			);
	}

	@When("I look for it by spare part code {string}")
	public void iLookForItBySparePartCode(String code) throws BusinessException {
		supplies = service.findBySparePartCode(code);
	}

	@Then("I get a list of supplies with code {string} and nifs {string}")
	public void iGetAListOfSuppliesWithCodeAndNifs(String code, String nifs) {
		assertTrue( supplies.stream()
				.allMatch(s -> code.equals( s.sparePart.code) )
			);

		assertTrue( supplies.stream()
				.allMatch(s -> nifs.contains( s.provider.nif ) )
			);
	}

	@When("I try to add a supply for a non existing provider")
	public void iTryToAddASupplyForANonExistingProvider() {
		SupplyDto dto = new SupplyUtil()
				.forProviderId( "does-not-exist" )
				.forSparePartCode( spare.code )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to add a supply for non existing spare part")
	public void iTryToAddASupplyForNonExistingSparePart() {
		SupplyDto dto = new SupplyUtil()
				.forProviderNif( provider.nif )
				.forSparePartId( "does-not-exist" )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to find the supplies for a non existing provider")
	public void iTryToFindASuppliesForANonExistingProvider() throws BusinessException {
		ctx.setResultList( service.findByProviderNif( "does-not-exist" ));
	}
	
	@When("I try to find a supply with null nif and spare code")
	public void iTryToFindSupplyNullNifAndSpareCode() {
		ctx.tryAndKeepException(() -> service.findByNifAndCode(null, null) );
	}

	@When("I try to find the supplies for a null nif")
	public void iTryToFindSupplyNullNif() {
		ctx.tryAndKeepException(() -> service.findByProviderNif( null) );
	}
	
	@When("I try to find the supplies for a null spare code")
	public void iTryToFindSupplyNullSpareCode() {
		ctx.tryAndKeepException(() -> service.findBySparePartCode( null) );
	}
	

	@When("I try to find the supplies for a non existing spare part")
	public void iTryToFindTheSuppliesForANonExistingSparePart() throws BusinessException {
		ctx.setResultList( service.findBySparePartCode( "does-not-exist" ));
	}

	@When("I look for a supply by nif {string} and code {string}")
	public void iLookForASupplyByNifAndCode(String nif, String code) throws BusinessException {
		supply = service.findByNifAndCode(nif, code).get();
	}

	@Then("I get the supplies for that provider nif {string} and spare code {string} with price {double} ans delivery {int} days")
	public void iGetTheSuppliesForThatProviderNifAndSpareCodeWithPriceAnsDeliveryDays(
			String nif, String code, double price, int days
		) {

		assertEquals( nif, supply.provider.nif );
		assertEquals( code, supply.sparePart.code );
		assertEquals( price, supply.price, 0.001 );
		assertEquals( days, supply.deliveryTerm );
	}

	@When("I try to find the non existing supply for that spare and provider")
	public void iTryToFindTheNonExistingSupplyForThatSpareAndProvider() throws BusinessException {
		String nif = provider.nif;
		String code = spare.code;
		ctx.setUniqueResult( service.findByNifAndCode(nif, code) );
	}

	@When("I try to find a supply with null nif and code")
	public void iTryToFindSupplyWithNullNifAndCode() throws BusinessException {
		ctx.tryAndKeepException(() -> service.findByNifAndCode(null, null) );
	}

}
