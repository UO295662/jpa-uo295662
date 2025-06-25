package uo.ri.cws.application.service.common;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.util.DbUtil;
import uo.ri.util.exception.BusinessException;

public class CommonSteps {

	private TestContext ctx;

	public CommonSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Before
	public void setUp() {
		new DbUtil().clearTables();
	}

	@After
	public void tearDown() {
		Factories.close();
	}

	@Then("argument is rejected with an explaining message")
	public void argumentIsRejectedWithAnExplainingMessage() {
		Exception ex = ctx.getException();

		assertNotNull(ex);
		assertTrue(ex instanceof IllegalArgumentException);
		assertNotNull(ex.getMessage());
		assertFalse(ex.getMessage().isBlank());
	}
	
	@Then("an error happens with an explaining message")
	public void anErrorHappensWithAnExplainingMessage() {
		Exception ex = ctx.getException();

		assertNotNull( ex );
		assertTrue(ex instanceof BusinessException);
		assertNotNull( ex.getMessage() );
		assertFalse( ex.getMessage().isBlank() );
	}

	@Then("empty is returned")
	public void emptyIsReturned() {
		Optional<?> optional = ctx.getUniqueResult();
		assertTrue( optional.isEmpty() );
	}

	@Then("an empty list is returned")
	public void anEmptyListIsReturned() {
		List<?> list = ctx.getResultList();
		assertTrue( list.isEmpty() );
	}

}