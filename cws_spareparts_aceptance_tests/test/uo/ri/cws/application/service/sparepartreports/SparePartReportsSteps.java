package uo.ri.cws.application.service.sparepartreports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.spare.SparePartReportService;
import uo.ri.cws.application.service.spare.SparePartReportService.SparePartReportDto;
import uo.ri.cws.application.service.util.SparePartUtil;
import uo.ri.cws.application.service.util.SubstitutionUtil;
import uo.ri.util.exception.BusinessException;

public class SparePartReportsSteps {

	private SparePartReportService service = Factories.service.forSparePartReportService();
	private SparePartReportDto report;

	private TestContext ctx;
	private List<SparePartReportDto> reports;

	public SparePartReportsSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("the following relation of spare parts with sells")
	public void theFollowingRelationOfSpareParts(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			String code = row.get("code");
			int sells = Integer.parseInt( row.get("sells") );

			addSparePart(row, code);
			addSells(code, sells);
		}
	}

	private void addSparePart(Map<String, String> row, String code) {
		new SparePartUtil()
				.withCode( code )
				.withDescription( row.get("desc") )
				.withPrice( Double.parseDouble( row.get("price") ))
				.withStock( Integer.parseInt( row.get("stock")))
				.withMinStock( Integer.parseInt( row.get("minStock")))
				.withMaxStock( Integer.parseInt( row.get("maxStock")))
				.register();
	}

	/**
	 * Add as many substitutions as needed of max 2 spare parts each one
	 * @param code
	 * @param c
	 */
	private void addSells(String code, int sells) {
		while( sells > 0 ) {
			int quantity = Math.min(2, sells);
			String interventionId = new ReportHelper().addInterventionContext();
			new SubstitutionUtil()
				.forSparePartCode( code )
				.forIntervention( interventionId )
				.withQuantity( quantity )
				.register();

				sells -= quantity;
		}
	}

	@When("I look for a spare part report by code {string}")
	public void iLookForASparePartByCode(String code) throws BusinessException {
		report = service.findByCode(code).get();
	}

	@Then("I get the spare part report with {string}, {double}, {int}, {int}, {int} and {int}")
	public void iGetTheSparePartWithCodeAnd(
			String desc,
			double price,
			int stock, int minStock, int maxStock,
			int sells) {

		assertEquals( desc, report.description );
		assertEquals( price, report.price, 0.001 );
		assertEquals( stock, report.stock );
		assertEquals( minStock, report.minStock );
		assertEquals( maxStock, report.maxStock );
		assertEquals( sells, report.totalUnitsSold );
	}

	@When("I try to find a non existing spare part report")
	public void iTryToFindANonExistingSparePartReport() throws BusinessException {
		ctx.setUniqueResult( service.findByCode( "does-not-exsist-code" ));
	}

	@When("I try to find a spare part report with null code")
	public void iTryToFindNullSparePartReport() throws BusinessException {
		ctx.tryAndKeepException( () -> service.findByCode( null ));
	}

	@When("I look for spare part reports by description {string}")
	public void iLookForASparePartByDescription(String description) throws BusinessException {
		reports = service.findByDescription(description);
	}

	@Then("I get {int} spare parts with description {string} and codes {string}")
	public void iGetSparePartsWithDescriptionAndCodes(int qty, String description, String codes) {
		assertEquals( qty, reports.size() );
		assertTrue( reports
				.stream()
				.allMatch(r -> description.equals( r.description ))
		);
		assertTrue( reports
				.stream()
				.allMatch(r -> codes.contains( r.code ))
		);
	}

	@Given("the following relation of spare parts with stock")
	public void theFollowingRelationOfSparePartsWithStock(DataTable data) {
		List<Map<String, String>> table = data.asMaps();
		for (Map<String, String> row : table) {
			new SparePartUtil()
					.withCode( row.get("code"))
					.withStock( Integer.parseInt( row.get("stock")) )
					.withMinStock( Integer.parseInt( row.get("minStock")) )
					.register();
		}
	}

	@When("I look for spare parts under stock")
	public void iLookForSparePartsUnderStock() throws BusinessException {
		reports = service.findUnderStock();
	}

	@Then("I get the following relation of spare parts")
	public void iGetTheFollowingRelationOfSpareParts(DataTable data) {
		List<String> expectedCodes = extractCodes( data );

		assertEquals( expectedCodes.size(), reports.size() );
		assertTrue( reports.stream()
				.allMatch( r -> expectedCodes.contains( r.code ))
			);
	}

	private List<String> extractCodes(DataTable data) {
		return data.asMaps().stream()
					.map(r -> r.get("code"))
					.collect( Collectors.toList() );
	}

	@When("I try to find a spare part report with non existing description")
	public void iTryToFindNonExistingDesciption() throws BusinessException {
		ctx.setResultList(
			service.findByDescription( "does-not-exsist-description" )
		);
	}

	@When("I try to find a spare part report with null description")
	public void iTryToFindNullDescriptionSparePartReport() throws BusinessException {
		ctx.tryAndKeepException( () -> service.findByDescription( null ));
	}

}
