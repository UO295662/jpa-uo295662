package uo.ri.cws.application.service.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.ProvidersCrudService;
import uo.ri.cws.application.service.spare.ProvidersCrudService.ProviderDto;
import uo.ri.cws.application.service.util.OrderUtil;
import uo.ri.cws.application.service.util.ProviderUtil;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.cws.application.service.util.SupplyUtil;
import uo.ri.util.exception.BusinessException;

public class ProviderSteps {

	private TestContext ctx;
	private ProvidersCrudService service = Factories.service.forProvidersService();
	
	private ProviderDto provider;
	private List<ProviderDto> providers;

	public ProviderSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a provider with")
	public void aProviderWith(DataTable dataTable) throws BusinessException {
		List<Map<String, String>> data = dataTable.asMaps();

		provider = new ProviderUtil()
						.withNif( data.get(0).get("nif") )
						.withEmail( data.get(0).get("email") )
						.withName( data.get(0).get("name") )
						.withPhone( data.get(0).get("phone") )
						.registerIfNew()
						.get();
	}

	@Given("a provider")
	public void aProvider() throws BusinessException {
		provider = new ProviderUtil()
					.withNif( randomNif() )
					.register()
					.get();
	}

	@Given("the providers with")
	public void theProvidersWith(DataTable data) throws BusinessException {
		List<Map<String, String>> table = data.asMaps();
		for(Map<String, String> row: table) {
			new ProviderUtil()
				.withNif( row.get("nif") )
				.withEmail( row.get("email") )
				.withName( row.get("name") )
				.withPhone( row.get("phone") )
				.register();
		}
	}

	@Given("a provider with orders registered")
	public void aProviderWithOrdersRegistered() throws BusinessException {
		String nif = randomNif();
		provider = new ProviderUtil().withNif(nif).register().get();
		new OrderUtil().withRandomCode().forProviderNif(nif).register();
		new OrderUtil().withRandomCode().forProviderNif(nif).register();
	}

	@Given("a provider with supplies registered")
	public void aProviderWithSupplies() throws BusinessException {
		String nif = randomNif();
		provider = new ProviderUtil().withNif(nif).register().get();
		new SparePartUtil().withCode("SP1").register();
		new SparePartUtil().withCode("SP2").register();
		new SupplyUtil().forProviderNif( nif ).forSparePartCode( "SP1" ).register();
		new SupplyUtil().forProviderNif( nif ).forSparePartCode( "SP2" ).register();
	}

	@Given("the following relation of providers")
	public void theFollowingRelationOfProviders(DataTable dataTable) throws BusinessException {
		List<Map<String, String>> table = dataTable.asMaps();
		for(Map<String, String> row: table) {
			new ProviderUtil()
				.withNif( row.get("nif"))
				.withName( row.get("name") )
				.withEmail( row.get("email") )
				.register();
		}
	}

	@Given("the following relation of supplies code-nif")
	public void theFollowingRelationOfSupplies(DataTable dataTable) throws BusinessException {
		List<Map<String, String>> table = dataTable.asMaps();

		Set<String> nifs = uniqueNifsFrom(table);
		for(String nif: nifs) {
			new ProviderUtil().withNif(nif).register();
		}

		Set<String> codes = uniqueCodesFrom(table);
		for(String code: codes) {
			new SparePartUtil().withCode( code ).register();
		}

		for(Map<String, String> row: table) {
			new SupplyUtil()
				.forProviderNif( row.get("nif"))
				.forSparePartCode( row.get("code") )
				.register();
		}
	}

	private Set<String> uniqueCodesFrom(List<Map<String, String>> table) {
		return table.stream()
				.map( row -> row.get("code") )
				.collect( Collectors.toSet() );
	}

	private Set<String> uniqueNifsFrom(List<Map<String, String>> table) {
		return table.stream()
				.map( row -> row.get("nif") )
				.collect( Collectors.toSet() );
	}

	@When("I try to remove a non existent provider")
	public void iTryToRemoveAnonExistentProvider() {
		ctx.tryAndKeepException(() -> service.delete("does-not-exist-nif"));
	}

	@When("I try to remove the provider")
	public void iTryToRemoveTheProvider() {
		ctx.tryAndKeepException(() -> service.delete( provider.nif ));
	}

	@When("we look for providers by name {string}")
	public void weLookForAProviderByName(String name) throws BusinessException {
		providers = service.findByName(name);
	}

	@When("I add a new non existing provider")
	public void iAddAProviderWithNif() throws BusinessException {
		provider = new ProviderUtil().withNif( randomNif() ).get();
		provider = service.add( provider );
	}

	private String randomNif() {
		return RandomStringUtils.randomAlphabetic( 9 );
	}

	@When("I remove the provider")
	public void iRemoveTheProvider() throws BusinessException {
		service.delete( provider.nif );
	}

	@When("I try to add a new provider with {string}, {string}, {string}, {string}")
	public void iTryToAddANewProviderWith(String nif, String name, String email, String phone) {
		ProviderDto dto = new ProviderUtil()
			.withNif( nif )
			.withEmail( email )
			.withName( name )
			.withPhone( phone )
			.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to add a new provider with")
	public void iTryToAddANewProviderWith(DataTable dataTable) {
		List<Map<String, String>> data = dataTable.asMaps();

		ProviderDto dto = new ProviderUtil()
			.withNif( data.get(0).get("nif") )
			.withEmail( data.get(0).get("email") )
			.withName( data.get(0).get("name") )
			.withPhone( data.get(0).get("phone") )
			.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to add a new provider with same nif")
	public void iTryToAddANewProviderWithSameNif() {
		ProviderDto dto = new ProviderUtil()
				.withNif( provider.nif )
				.get();

		ctx.tryAndKeepException(() -> service.add(dto));
	}

	@When("I try to update a non existing provider")
	public void iTryToUpdateNonExistingProvider() throws BusinessException {
		ProviderDto dto = new ProviderUtil()
				.withNif("provider-does-not-exist")
				.get();

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update the provider with wrong version")
	public void iTryToUpdateWithWrongVersion() throws BusinessException {
		ProviderDto dto = new ProviderUtil()
				.loadByNif( provider.nif )
				.get();

		dto.name += " updated";
		dto.version -= 1;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update the provider with nif {string} to <name>, <email> and <phone>")
	public void iTryToUpdateTheProviderWithNifToNameEmailAndPhone(String nif,
			DataTable table) throws BusinessException {
		ProviderDto dto = new ProviderUtil().loadByNif( nif ).get();

		List<Map<String, String>> data = table.asMaps();
		dto.name = data.get(0).get("name");
		dto.email = data.get(0).get("email");
		dto.phone = data.get(0).get("phone");

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to update the provider to {string}, {string} and {string}")
	public void iTryToUpdateTheProviderToNameEmailAndPhone(
			String name, String email, String phone)
			throws BusinessException {
		ProviderDto dto = new ProviderUtil().loadByNif( provider.nif ).get();

		dto.name = name;
		dto.email = email;
		dto.phone = phone;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I update the provider for all its fields")
	public void iUpdateTheProviderForAllItsFields() throws BusinessException {
		ProviderDto dto = new ProviderUtil().loadByNif( provider.nif ).get();

		dto.id += "-updated";
		provider.name = dto.name += "-updated";
		provider.email = dto.email += "-updated";
		provider.phone = dto.phone += "-updated";

		service.update(dto);
	}

	@When("I try to update the provider with nif {string} to {string}, {string} and {string}")
	public void iTryToUpdateTheProviderWithNifToAnd(String nif, String name,
			String email, String phone) throws BusinessException {
		ProviderDto dto = new ProviderUtil().loadByNif(nif).get();

		dto.name += name;
		dto.email += email;
		dto.phone += phone;

		ctx.tryAndKeepException(() -> service.update(dto));
	}

	@When("I try to find a non existent provider")
	public void iTryToFindAProviderWithTheNif() throws BusinessException {
		ctx.setUniqueResult( service.findByNif("does-not-exist-nif") );
	}

	@When("we look for providers by spare part code {string}")
	public void weLookForAProviderBySparePartCode(String code) throws BusinessException {
		providers = service.findBySparePartCode(code);
	}

	@When("I look for a provider by nif {string}")
	public void weLookForAProviderByNif(String nif) throws BusinessException {
		provider = service.findByNif(nif).get();
	}

	@Then("the provider results updated for all the fields except the nif and id")
	public void theProviderResultsUpdatedForAllTheFields() throws BusinessException {
		ProviderDto loaded = new ProviderUtil().loadByNif( provider.nif ).get();

		assertEquals( provider.email, loaded.email );
		assertEquals( provider.name, loaded.name );
		assertEquals( provider.phone, loaded.phone );
		assertEquals( provider.phone, loaded.phone );

		assertTrue( provider.version < loaded.version);

		assertFalse( loaded.id.contains( "-updated" ) );
		assertFalse( loaded.nif.contains( "-updated" ) );
	}

	@Then("the provider results added to the system")
	public void theProviderResultsAddedToTheSystem() throws BusinessException {
		ProvidersCrudService service = Factories.service.forProvidersService();
		Optional<ProviderDto> op = service.findByNif( provider.nif );

		ProviderDto added = op.get();

		assertEquals( provider.nif, added.nif );
		assertEquals( provider.name, added.name );
		assertEquals( provider.email, added.email );
		assertEquals( provider.phone, added.phone );
		assertFalse( added.id.isBlank() );
	}

	@Then("the provider no longer exists")
	public void theProviderNoLongerExists() throws BusinessException {
		ProviderDto dto = new ProviderUtil().loadByNif( provider.nif ).get();
		assertNull( dto );
	}

	@Then("we get {int} providers with name {string} and nifs {string}")
	public void weGetProvidersWith(int qty, String name, String nifs) {
		assertEquals( qty, providers.size() );
		for (ProviderDto p : providers) {
			assertTrue( nifs.contains( p.nif ) );
			assertEquals( name, p.name );
		}
	}

	@Then("we get a list of {int} providers with nifs {string}")
	public void weGetAListOfProvidersWithNifs(int qty, String nifs) {
		assertEquals( qty, providers.size() );
		for (ProviderDto p : providers) {
			assertTrue( nifs.contains( p.nif ) );
		}
	}

	@Then("I get a provider with nif {string} and name {string}")
	public void iGetAProviderWithNifAndName(String nif, String name) {
		assertEquals( nif, provider.nif );
		assertEquals( name, provider.name );
	}


//	private void tryDeleteAndKeepException(String nif) {
//		try {
//			service.delete(nif);
//			fail();
//		} catch (BusinessException ex) {
//			ctx.setException( ex );
//		}
//	}

}

