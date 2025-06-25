package uo.ri.cws.application.service.mechanic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.InterventionUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.util.exception.BusinessException;

public class MechanicSteps {
	private TestContext ctx;
	private MechanicCrudService service = Factories.service
			.forMechanicCrudService();
	private MechanicDto mechanic;
	private MechanicDto found;
	private List<MechanicDto> mechanics;

	public MechanicSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a mechanic")
	public void aMechanic() throws BusinessException {
		mechanic = new MechanicUtil()
				.register()
				.get();
	}

	@Given("a mechanic {string}")
	public void aMechanicWithNif(String nif) throws BusinessException {
		mechanic = new MechanicUtil()
				.withNif(nif)
				.register()
				.get();
	}

	@Given("a mechanic with work orders registered")
	public void aMechanicWithWorkOrdersRegistered() throws BusinessException {
		mechanic = new MechanicUtil().register().get();
		new WorkOrderUtil().forMechanic(mechanic.id).register();
	}

	@Given("a mechanic with interventions registered")
	public void aMechanicWithInterventionsRegistered() throws BusinessException {
		mechanic = new MechanicUtil().register().get();
		WorkOrderDto wo = new WorkOrderUtil().register().get();
		new InterventionUtil()
			.forMechanic(mechanic.id)
			.forWorkOrder(wo.id)
			.register();
	}

	@Given("the following relation of mechanics")
	public void theFollowingRelationOfMechanics(DataTable dataTable)
			throws BusinessException {
		List<Map<String, String>> table = dataTable.asMaps();
		String id = null;
		for (Map<String, String> row : table) {
			id = row.get("id");
			if (id == null) {
				id = UUID.randomUUID().toString();
			}
			new MechanicUtil()
					.withId(id)
					.withNif(row.get("nif"))
					.withName(row.get("name"))
					.withSurname(row.get("surname"))
					.register();
		}
	}

	@Given("the following mechanic")
	public void theFollowingMechanics(DataTable dataTable)
			throws BusinessException {
		List<Map<String, String>> table = dataTable.asMaps();
		Map<String, String> row = table.get(0);
		String id = row.get("id");
		if (id == null) {
			id = UUID.randomUUID().toString();
		}

		mechanic = new MechanicUtil()
				.withId(id)
				.withNif(row.get("nif"))
				.withName(row.get("name"))
				.withSurname(row.get("surname"))
				.register()
				.get();
	}

	@When("I try to remove a non existent mechanic")
	public void iTryToRemoveAnonExistentMechanic() {
		ctx.tryAndKeepException(() -> service.deleteMechanic("non-existing-mechanic"));
	}

	@When("I try to remove the mechanic")
	public void iTryToRemoveTheMechanic() {
		ctx.tryAndKeepException(() -> service.deleteMechanic( mechanic.id ));
	}

	@When("I try to remove a mechanic with null argument")
	public void iTryToRemoveAMechanicWithNullArgument() {
		ctx.tryAndKeepException(() -> service.deleteMechanic( null ));
	}

	@When("we read all mechanics")
	public void weReadAllMechanics() throws BusinessException {
		mechanics = service.findAllMechanics();
	}

	@When("I add a new non existing mechanic")
	public void iAddANewMechanic() throws BusinessException {
		mechanic = new MechanicUtil().get();
		mechanic.id = service.addMechanic(mechanic).id;
	}

	@When("I remove the mechanic")
	public void iRemoveTheMechanic() throws BusinessException {
		service.deleteMechanic(mechanic.id);
	}

	@When("I try to add a new mechanic with null argument")
	public void iTryToAddANewMechanicWithNullArgument() {
		MechanicDto dto = null;

		ctx.tryAndKeepException(() -> service.addMechanic(dto));
	}

	@When("I try to add a new mechanic with null nif")
	public void iTryToAddANewMechanicWithNullNif() {
		MechanicDto dto = new MechanicUtil()
				.withNif(null)
				.get();

		ctx.tryAndKeepException(() -> service.addMechanic(dto));
	}

	@When("I try to add a new mechanic with {string}, {string}, {string}")
	public void iTryToAddANewMechanicWith(String nif, String name,
			String surname) {
		MechanicDto dto = new MechanicUtil()
				.withNif(nif)
				.withSurname(surname)
				.withName(name)
				.get();

		ctx.tryAndKeepException(() -> service.addMechanic(dto));
	}

	@When("I try to add a new mechanic with same nif")
	public void iTryToAddANewMechanicWithSameNif() throws BusinessException {
		MechanicDto dto = new MechanicUtil()
				.withNif(mechanic.nif)
				.get();

		ctx.tryAndKeepException(() -> service.addMechanic(dto));
	}

	@When("I try to update a non existing mechanic")
	public void iTryToUpdateNonExistingMechanic() throws BusinessException {
		MechanicDto dto = new MechanicUtil()
				.withId("mechanic-does-not-exist")
				.get();

		ctx.tryAndKeepException(() -> service.updateMechanic(dto));
	}

	@When("I try to update a mechanic updated in the while")
	public void iTryToUpdateAMechanicUpdatedInTheWhile()
			throws BusinessException {
		mechanic.version--;
		ctx.tryAndKeepException(() -> service.updateMechanic( mechanic ));
	}

	@When("I try to update a mechanic with null name")
	public void iTryToUpdateAMechanicWithNullName() {
		MechanicDto dto = new MechanicUtil()
				.withName(null)
				.get();

		ctx.tryAndKeepException(() -> service.updateMechanic(dto));
	}

	@When("I try to update a mechanic with null surname")
	public void iTryToUpdateAMechanicWithNullSurname() {
		MechanicDto dto = new MechanicUtil()
				.withSurname(null)
				.get();

		ctx.tryAndKeepException(() -> service.updateMechanic(dto));
	}

	@When("I try to update a mechanic with null nif")
	public void iUpdateAMechanicWithNullNif() throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(mechanic.id).get();
		dto.nif = null;
		dto.name = dto.name + "-updated";
		dto.surname = dto.surname + "-updated";

		ctx.tryAndKeepException(() -> service.updateMechanic(dto));
	}

	@When("I try to update the mechanic to {string}, {string} and {string}")
	public void iTryToUpdateTheMechanicToNifNameAndSurname(
			String name, String surname, String nif)
			throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(mechanic.id).get();

		dto.name = name;
		dto.surname = surname;
		dto.nif = nif;

		ctx.tryAndKeepException(() -> service.updateMechanic(dto));
	}

	@When("I update the mechanic")
	public void iUpdateTheMechanic() throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(mechanic.id).get();

		dto.name = dto.name += "-updated";
		dto.surname = dto.surname += "-updated";
		dto.nif = dto.nif += "-updated";

		service.updateMechanic(dto);
	}

	@When("I try to find a mechanic with null argument")
	public void iTryToFindAMechanicWithNullArgument() {
		ctx.tryAndKeepException(() -> service.findMechanicById( null ));
	}

	@When("I try to find a mechanic with null nif")
	public void iTryToFindAMechanicWithNullNif() {
		ctx.tryAndKeepException(() -> service.findMechanicByNif( null ));
	}

	@When("I try to find a non existent mechanic")
	public void iTryToFindAMechanicByTheId() throws BusinessException {
		Optional<MechanicDto> om = service
				.findMechanicById("does-not-exist-id");
		ctx.setUniqueResult(om);
	}

	@When("I try to find a mechanic by a non existent nif")
	public void iTryToFindAMechanicByANonExistentNif()
			throws BusinessException {
		Optional<MechanicDto> om = service
				.findMechanicByNif("does-not-exist-nif");
		ctx.setUniqueResult(om);
	}

	@When("I look for a mechanic by wrong id {string}")
	public void iLookForAMechanicById(String id) throws BusinessException {
		ctx.tryAndKeepException(() -> service.findMechanicById(id));
	}

	@When("I look for mechanic")
	public void iLookForMechanic() throws BusinessException {
		found = service.findMechanicById(mechanic.id).get();
	}

	@When("I look for mechanic by nif")
	public void iLookForMechanicByNif() throws BusinessException {
		found = service.findMechanicByNif(mechanic.nif).get();
	}

	@Then("the mechanic results updated for fields name and surname")
	public void theMechanicResultsUpdatedForNameAndSurnameOnly()
			throws BusinessException {
		MechanicDto loaded = new MechanicUtil().loadById(mechanic.id).get();

		assertEquals(mechanic.nif, loaded.nif); // nif not updated
		assertEquals(mechanic.name + "-updated", loaded.name);
		assertEquals(mechanic.surname + "-updated", loaded.surname);
	}

	@Then("the mechanic results added to the system")
	public void theMechanicResultsAddedToTheSystem() throws BusinessException {
		Optional<MechanicDto> op = service.findMechanicById(mechanic.id);

		MechanicDto added = op.get();

		assertEquals(mechanic.nif, added.nif);
		assertEquals(mechanic.name, added.name);
		assertEquals(mechanic.surname, added.surname);
		assertFalse(added.id.isBlank());
	}

	@Then("mechanic is not found")
	public void weGetNoMechanic() throws BusinessException {
		assertTrue(ctx.getUniqueResult().isEmpty());
	}

	@Then("the mechanic no longer exists")
	public void theMechanicNoLongerExists() throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(mechanic.id).get();
		assertNull(dto);
	}

	@Then("we get the following list of mechanics")
	public void iGetTheFollowingRelationOfMechanics(DataTable data) {
		List<String> expectedMechanics = extractMechanics(data);

		assertEquals(expectedMechanics.size(), mechanics.size());
		for (MechanicDto mechanic : mechanics) {
			String mString = new StringBuilder()
					.append(mechanic.nif)
					.append(",")
					.append(mechanic.name)
					.append(",")
					.append(mechanic.surname)
					.toString();
			
			assertTrue( expectedMechanics.contains(mString) );
		}
	}

	@Then("I get mechanic")
	public void iGetTheMechanic() {
		assertEquals(mechanic.nif, found.nif);
		assertEquals(mechanic.name, found.name);
		assertEquals(mechanic.surname, found.surname);
	}

	private List<String> extractMechanics(DataTable data) {
		return data.asMaps().stream()
				.map(r -> r.get("mechanic"))
				.collect(Collectors.toList());
	}

}
