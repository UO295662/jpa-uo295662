package uo.ri.cws.application.service;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;

@Suite
@SelectClasspathResource("uo/ri/cws/application/service/mechanic")
@SelectClasspathResource("uo/ri/cws/application/service/invoice")
@SelectClasspathResource("uo/ri/cws/application/service/orders/Generate.feature")
@SelectClasspathResource("uo/ri/cws/application/service/spareparts")
@SelectClasspathResource("uo/ri/cws/application/service/sparepartreports")
@SelectClasspathResource("uo/ri/cws/application/service/orders/FindByProviderNif.feature")
@SelectClasspathResource("uo/ri/cws/application/service/orders/FindDetailByCode.feature")
@ConfigurationParameter(
	key = Constants.PLUGIN_PROPERTY_NAME, 
	value = "pretty"
)
@ConfigurationParameter(
	key = Constants.SNIPPET_TYPE_PROPERTY_NAME, 
	value = "camelcase"
)
@ConfigurationParameter(
	key = Constants.GLUE_PROPERTY_NAME, 
	value = "uo.ri.cws.application.service"
)
public class RunCucumberTests_0 {}
