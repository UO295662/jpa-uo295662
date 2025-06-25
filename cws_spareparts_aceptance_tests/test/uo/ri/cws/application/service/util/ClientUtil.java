package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.util.sql.AddClientSqlUnitOfWork;

public class ClientUtil {

	private ClientDto dto = createDefaultDto();

	public ClientUtil randomNif() {
		dto.nif = RandomStringUtils.randomAlphanumeric(9);
		return this;
	}

	public ClientUtil register() {
		new AddClientSqlUnitOfWork(dto).execute();
		return this;
	}

	public ClientDto get() {
		return dto;
	}

	private ClientDto createDefaultDto() {
		ClientDto res = new ClientDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1;

		res.nif = RandomStringUtils.randomAlphanumeric(9);
		res.name = RandomStringUtils.randomAlphanumeric(5) + " name";
		res.surname = RandomStringUtils.randomAlphanumeric(5) + " surname";
		res.phone = RandomStringUtils.randomNumeric(9);
		res.addressCity = RandomStringUtils.randomAlphabetic(5) + " city";
		res.addressStreet = RandomStringUtils.randomAlphabetic(5) + " street";
		res.addressZipcode = RandomStringUtils.randomNumeric(5);
		res.email = RandomStringUtils.randomNumeric(5) + "@mail.com";

		return res;
	}

}
