package uo.ri.cws.application.service.spareparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.spare.SparePartCrudService.SparePartDto;
import uo.ri.cws.application.service.util.OrderUtil;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.cws.application.service.util.SupplyUtil;
import uo.ri.util.exception.BusinessException;

public class SparePartSteps {

	private SparePartCrudService service = Factories.service.forSparePartCrudService();
	private SparePartDto spare;

	private TestContext ctx;

	public SparePartSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a non-registered spare part")
	public void aNonRegisteredSparePart() {
		spare = new SparePartUtil().get();
	}

	@When("I add the spare part")
	public void iAddASparePart() throws BusinessException {
		spare = service.add( spare );
	}

	@Then("the spare part gets added to the system")
	public void theSparePartGetsAddedToTheSystem() {
		SparePartDto loaded = new SparePartUtil().loadByCode( spare.code ).get();
		assertFalse( loaded.id.isBlank() );
		assertEquals( spare.id, loaded.id );
		assertEquals( spare.description, loaded.description );
		assertEquals( spare.price, loaded.price, 0.001 );
		assertEquals( spare.stock, loaded.stock );
		assertEquals( spare.minStock, loaded.minStock );
		assertEquals( spare.maxStock, loaded.maxStock );
		// version not checked as in JPA is assigned by the mapper at commit time
	}

	@When("I try to add a spare part with data")
	public void iTryToAddASparePartWithData(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row: table) {
			SparePartDto dto = new SparePartUtil()
					.withCode( row.get("code") )
					.withDescription( row.get("description") )
					.withPrice( Double.parseDouble( row.get("price")) )
					.withStock( Integer.parseInt( row.get("stock")) )
					.withMinStock( Integer.parseInt( row.get("minStock")) )
					.withMaxStock( Integer.parseInt( row.get("maxStock")) )
					.get();

			ctx.tryAndKeepException(() -> service.add(dto));
		}
	}

	@When("I try to add a null spare part")
	public void iTryToAddNullSparePart() {
		ctx.tryAndKeepException(() -> service.add(null));
	}

	@Given("the following relation of spare parts")
	public void theFollowingRelationOfSpareParts(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			new SparePartUtil()
				.withCode( row.get("code") )
				.withDescription( row.get("description"))
				.register();
		}
	}

	@Given("a registered spare part")
	public void aSparePart() {
		spare = new SparePartUtil()
				.randomCode()
				.register()
				.get();
	}

	@When("I try to add a new spare part with same code")
	public void iTryToAddASparePartWithSameCode() {
		SparePartDto dto = new SparePartUtil()
				.withCode( spare.code )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to remove a non existent spare part")
	public void iTryToRemoveANonExistentSparePart() {
		ctx.tryAndKeepException(() -> service.delete("does-not-exist-spare"));
	}

	@When("I try to remove the spare part")
	public void iTryToRemoveTheSparePart() {
		ctx.tryAndKeepException(() -> service.delete( spare.code ));
	}

	@When("I try to remove a null spare part")
	public void iTryToRemoveNullSparePart() {
		ctx.tryAndKeepException(() -> service.delete( null ));
	}

	@Given("a registered spare part that has order lines")
	public void aSparePartThatHasOrderLines() {
		String code = RandomStringUtils.randomAlphanumeric( 6 );
		String nif = RandomStringUtils.randomAlphanumeric( 9 );

		spare = new SparePartUtil().withCode( code ).register().get();
		new ProviderUtil().withNif(nif).register();
		new OrderUtil()
					.withRandomCode()
					.addOrderLine( spare.code, 1, 1.0)
					.forProviderNif(nif)
					.register();
	}

	@Given("a registered spare part that has suppliers")
	public void aSparePartThatHasSuppliers() {
		String code = RandomStringUtils.randomAlphanumeric( 6 );
		String nif = RandomStringUtils.randomAlphanumeric( 9 );

		spare = new SparePartUtil().withCode( code ).register().get();
		new ProviderUtil().withNif(nif).register();
		new SupplyUtil()
					.forProviderNif( nif )
					.forSparePartCode( code )
					.withPrice( 10 )
					.withDeliveryTerm( 5 )
					.register();
	}

	@When("I remove the spare part")
	public void iRemoveTheSparePart() throws BusinessException {
		service.delete( spare.code );
	}

	@Then("the spare part gets removed from the system")
	public void theSparePartGetsRemovedFromTheSystem() {
		SparePartDto removed = new SparePartUtil().loadByCode( spare.code ).get();

		assertNull( removed );
	}

	@When("I try to update a non existent spare part")
	public void iTryToUpdateTheSparePartWithCode() {
		SparePartDto dto = new SparePartUtil()
				.withCode("spare-part-does-not-exist")
				.get();

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I update the spare part to new values for all its fields")
	public void iUpdateTheSparePartToNewValuesForAllItsFields() 
			throws BusinessException {
		
		SparePartDto dto = new SparePartUtil().loadByCode( spare.code ).get();

		spare.id = dto.id += "-updated";
		spare.description = dto.description += "-updated";
		spare.price = dto.price += 100.0;
		spare.stock = dto.stock += 100;
		spare.minStock = dto.minStock += 100;
		spare.maxStock = dto.maxStock += 100;

		service.update(dto);
	}

	@Then("the spare changes all the fields except the code and the id")
	public void theSpareChangesAllTheFieldsExceptTheCodeAndTheId() {
		SparePartDto loaded = new SparePartUtil().loadByCode( spare.code ).get();

		assertEquals( spare.description, loaded.description );
		assertEquals( spare.price, loaded.price, 0.001 );
		assertEquals( spare.stock, loaded.stock );
		assertEquals( spare.minStock, loaded.minStock );
		assertEquals( spare.maxStock, loaded.maxStock );

		assertTrue( spare.version < loaded.version);

		assertFalse( loaded.id.contains( "-updated" ) );
		assertFalse( loaded.code.contains( "-updated" ) );
	}

	@When("I try to update the spare part with data")
	public void iTryToUpdateTheSparePartWithCodeWithData(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			spare.description = row.get("description");
			spare.price = Double.parseDouble( row.get("price") );
			spare.stock = Integer.parseInt( row.get("stock") );
			spare.minStock = Integer.parseInt( row.get("minStock") );
			spare.maxStock = Integer.parseInt( row.get("maxStock") );

			ctx.tryAndKeepException(() -> service.update(spare));
		}
	}

	@When("I try to update the spare part with data {string}, {double}, {int}, {int}, {int}")
	public void iTryToUpdateTheSparePartWithData(String description,
			double price, int stock, int minStock, int maxStock) {
		SparePartDto dto = new SparePartUtil().loadByCode( spare.code ).get();

		dto.description = description;
		dto.price = price;
		dto.stock = stock;
		dto.minStock = minStock;
		dto.maxStock = maxStock;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update a null spare part")
	public void iTryToUpdateNullSparePart() {
		ctx.tryAndKeepException(() -> service.update( null ));
	}

	@When("I try to update the spare part with wrong version")
	public void iTryToUpdateTheSparePartWithWrongVersion() {
		SparePartDto dto = new SparePartUtil().loadByCode( spare.code ).get();

		dto.description += " updated";
		dto.version -= 1;

		ctx.tryAndKeepException(() -> service.update(dto) );
	}

	@When("I look for a spare part by code {string}")
	public void iLookForASparePartByCode(String code) throws BusinessException {
		spare = service.findByCode(code).get();
	}

	@Then("I get the spare part with code {string} and description {string}")
	public void iGetTheSparePartWithCodeAndDescripction(String code,
			String description) {
		assertEquals( code, spare.code );
		assertEquals( description, spare.description );
	}

	@When("I try to find a non existing spare part")
	public void iTryToFindNonExistingSparePart() throws BusinessException {
		Optional<SparePartDto> osp = service.findByCode("does-not-exist-spare");
		ctx.setUniqueResult( osp );
	}

	@When("I try to find a null spare part")
	public void iTryToFindNullSparePart() throws BusinessException {
		ctx.tryAndKeepException( () -> service.findByCode( null ) );
	}

}
